package Manager;

import Tasks.Task;
import Tasks.Epic;
import Tasks.Subtask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{
    private final HashMap<Integer, Task> allTasks = new HashMap<>();
    private final HashMap<Integer, Epic> allEpics = new HashMap<>();
    private final HashMap<Integer, Subtask> allSubtasks = new HashMap<>();
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
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(allSubtasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
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
    public void updateTask(Task task, TaskStatusList status) {
        task.setStatus(status);
        allTasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask, TaskStatusList status) {
        subtask.setStatus(status);
        allSubtasks.put(subtask.getId(), subtask);
        allEpics.get(subtask.getEpicId()).checkEpicStatus(allSubtasks);
    }

    @Override
    public void updateEpic(Epic epic, TaskStatusList status) {
        epic.setStatus(status);
        allEpics.put(epic.getId(), epic);
        for (int id : epic.getSubtasksList()) {
            if (getTaskById(id) == null) {
                continue;
            }
            getTaskById(id).setStatus(status);
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (allTasks.get(id) == null) {
            return null;
        }
        historyManager.add(allTasks.get(id));
        return allTasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (allEpics.get(id) == null) {
            return null;
        }
        historyManager.add(allEpics.get(id));
        return allEpics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (allSubtasks.get(id) == null) {
            return null;
        }
        historyManager.add(allSubtasks.get(id));
        return allSubtasks.get(id);
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
    public HashMap<Integer, Subtask> getSubtasksByEpicId(int id) {
        HashMap<Integer, Subtask> subtasks = new HashMap<>();

        if (allEpics.get(id).getSubtasksList().isEmpty()) {
            return null;
        }
        for (int i = 0; i < allEpics.get(id).getSubtasksList().size(); i++) {
            subtasks.put(allEpics.get(id).getSubtasksList().get(i),
                    allSubtasks.get(allEpics.get(id).getSubtasksList().get(i)));
        }
        return subtasks;
    }
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}