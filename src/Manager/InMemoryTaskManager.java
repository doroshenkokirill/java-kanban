package Manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Tasks.Task;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.TaskStatusList;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> allTasks = new HashMap<>();
    private final Map<Integer, Epic> allEpics = new HashMap<>();
    private final Map<Integer, Subtask> allSubtasks = new HashMap<>();
    private int id = 0;
    private final HistoryManager historyManager = Manager.getDefaultHistory();

    @Override
    public void createNewTask(Task task) {
        id++;
        task.setId(id);
        allTasks.put(id, task);
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        allSubtasks.put(id, subtask);
        allEpics.get(subtask.getEpicId()).setSubtaskIdList(id);
    }

    @Override
    public void createNewEpic(Epic epic) {
        id++;
        epic.setId(id);
        allEpics.put(id, epic);
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
        allTasks.clear();
    }

    @Override
    public void clearAllEpics() {
        allEpics.clear();
    }

    @Override
    public void clearAllSubtasks() {
        allSubtasks.clear();
        for (Epic epic : allEpics.values()) {
            epic.clearAllSubtasks();
        }
    }

    @Override
    public void updateTask(Task task) {
        allTasks.replace(task.getId(), task);
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
    }

    @Override
    public void removeEpicById(int id) { // нужно удалить все Subtask которые относились к Epiс'у
        if (!allEpics.containsKey(id)) {
            return;
        }
        for (int i = 0; i < allEpics.get(id).getSubtasksList().size(); i++) {
            allSubtasks.remove(allEpics.get(id).getSubtasksList().get(i));
        }
        allEpics.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        if (!allSubtasks.containsKey(id)) {
            return;
        }
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
    public void save() throws ManagerSaveException {
    }
}