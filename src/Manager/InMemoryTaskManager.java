package Manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Manager.Exeptions.SaveException;
import Manager.Exeptions.TimeException;
import Tasks.Task;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.TaskStatusList;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> allTasks = new HashMap<>();
    private final Map<Integer, Epic> allEpics = new HashMap<>();
    private final Map<Integer, Subtask> allSubtasks = new HashMap<>();
    private int id = 0;
    private final HistoryManager historyManager = Manager.getDefaultHistory();
    private final Set<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        Comparator<Task> comparator = (t1, t2) -> {
            if (t1.getStartTime() == null) {
                return 1;
            } else if (t2.getStartTime() == null) {
                return -1;
            }
            return t1.getStartTime().compareTo(t2.getStartTime());
        };
        prioritizedTasks = new TreeSet<>(comparator);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void checkTasks(Task task) {
        if (checkTasksTime(task)) {
            throw new TimeException("Задача пересекается по времени");
        }
    }

    public void setTimeForEpic(Epic epic) {
        setStartTimeForEpic(epic);
        setDurationForEpic(epic);
        setEndTimeForEpic(epic);
    }

    private void setStartTimeForEpic(Epic epic) {
        for (Subtask someSubTask : getAllSubtasks()) {
            LocalDateTime someStartTime = someSubTask.getStartTime();
            if (epic.getStartTime() != LocalDateTime.MIN) {
                if (epic.getStartTime().isAfter(someStartTime)) {
                    epic.setStartTime(someStartTime);
                }
            } else {
                epic.setStartTime(someStartTime);
            }
        }
    }

    private void setDurationForEpic(Epic epic) {
        Duration duration = Duration.ofMinutes(0);
        epic.setDuration(duration);
        for (Subtask someSubTask : getAllSubtasks()) {
            epic.setDuration(epic.getDuration().plusMinutes(someSubTask.getDuration().toMinutes()));
        }
    }

    private void setEndTimeForEpic(Epic epic) {
        for (Subtask someSubTask : getAllSubtasks()) {
            LocalDateTime someSubTaskEndTime = someSubTask.getEndTime();
            if (epic.getEndTime() != null && epic.getEndTime().isBefore(someSubTaskEndTime)) {
                epic.setEndTime(someSubTaskEndTime);
            }
        }
    }

    public void updateEpicStatus(Epic epic) {
        if (!epic.getSubtasksList().isEmpty()) {
            int statusNew = 0;
            int statusDone = 0;
            for (Integer id : epic.getSubtasksList()) {
                switch (getSubtaskById(id).getStatus()) {
                    case NEW:
                        ++statusNew;
                        break;
                    case DONE:
                        ++statusDone;
                        break;
                    default:
                }
            }
            if (statusNew == epic.getSubtasksList().size()) {
                epic.setStatus(TaskStatusList.NEW);

            } else if (statusDone == epic.getSubtasksList().size()) {
                epic.setStatus(TaskStatusList.DONE);

            } else if (epic.getStatus() != TaskStatusList.IN_PROGRESS) {
                epic.setStatus(TaskStatusList.IN_PROGRESS);
            }
        } else {
            epic.setStatus(TaskStatusList.NEW);
        }
    }

    public boolean checkTasksTime(Task task) {
        if (getPrioritizedTasks().isEmpty()) {
            return false;
        } else if (task.getStartTime() != null) {
            return !getPrioritizedTasks().stream()
                    .filter(someTask -> someTask.getEndTime() != null && !task.equals(someTask))
                    .allMatch(someTask -> task.getStartTime().isAfter(someTask.getEndTime())
                            || task.getStartTime().equals(someTask.getEndTime())
                            || task.getEndTime().isBefore(someTask.getStartTime())
                            || task.getEndTime().equals(someTask.getStartTime()));
        }
        return true;
    }

    @Override
    public void updateTask(Task task) {
        allTasks.put(task.getId(), task);
        task.setDuration(task.getDuration());
    }

    @Override
    public void createNewTask(Task task) {
        id++;
        task.setId(id);
        allTasks.put(id, task);
        if (task.getStartTime() != null) {
            checkTasks(task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        allSubtasks.put(id, subtask);
        allEpics.get(subtask.getEpicId()).setSubtaskIdList(id);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void createNewEpic(Epic epic) {
        id++;
        epic.setId(id);
        allEpics.put(id, epic);
        prioritizedTasks.add(epic);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(allSubtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(allEpics.values());
    }

    @Override
    public void clearAllTasks() {
        allTasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
        });
        allTasks.clear();
    }

    @Override
    public void clearAllEpics() {
        allEpics.keySet().forEach(historyManager::remove);
        allSubtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        allEpics.clear();
        allSubtasks.clear();
    }

    @Override
    public void clearAllSubtasks() {
        allSubtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
        });
        allSubtasks.clear();
        allEpics.values().forEach(epic -> {
            epic.getSubtasksList().clear();
            epic.setStatus(TaskStatusList.NEW);
            epic.setStartTime(null);
            epic.setDuration(null);
        });
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        allSubtasks.replace(subtask.getId(), subtask);
        allEpics.get(subtask.getEpicId()).checkEpicStatus(allSubtasks);
    }

    @Override
    public void updateEpic(Epic epic) {
        allEpics.replace(epic.getId(), epic);
        for (int id : epic.getSubtasksList()) {
            if (epic.getStatus() == TaskStatusList.DONE) {
                getSubtaskById(id).setStatus(TaskStatusList.DONE);
                allSubtasks.replace(id, getSubtaskById(id));
            }
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task taskById = allTasks.get(id);
        if (taskById == null) {
            return null;
        }
        historyManager.add(taskById);
        return taskById;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epicById = allEpics.get(id);
        if (epicById == null) {
            return null;
        }
        historyManager.add(epicById);
        return epicById;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subTaskById = allSubtasks.get(id);
        if (subTaskById == null) {
            return null;
        }
        historyManager.add(subTaskById);
        return subTaskById;
    }

    @Override
    public void removeTaskById(int id) {
        if (!allTasks.containsKey(id)) {
            return;
        }
        allTasks.remove(id);
        prioritizedTasks.remove(allTasks.get(id));
    }

    @Override
    public void removeEpicById(int id) { // нужно удалить все Subtask которые относились к Epiс'у
        if (!allEpics.containsKey(id)) {
            return;
        }
        prioritizedTasks.remove(getEpicById(id));
        for (int i = 0; i < allEpics.get(id).getSubtasksList().size(); i++) {
            prioritizedTasks.remove(getSubtaskById(id));
            allSubtasks.remove(allEpics.get(id).getSubtasksList().get(i));
        }
        allEpics.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        if (!allSubtasks.containsKey(id)) {
            return;
        }
        prioritizedTasks.remove(getSubtaskById(id));
        allSubtasks.remove(id);
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int id) {
        List<Subtask> subtasks = new ArrayList<>();

        if (allEpics.get(id).getSubtasksList().isEmpty()) {
            return null;
        }
        for (Integer subtaskId : allEpics.get(id).getSubtasksList()) {
            Subtask subtask = allSubtasks.get(subtaskId);
            subtasks.add(subtask);
        }
        return subtasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void save() throws SaveException {
    }
}