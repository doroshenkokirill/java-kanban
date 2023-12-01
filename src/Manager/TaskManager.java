package Manager;

import Tasks.Task;
import Tasks.Epic;
import Tasks.Subtask;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> allTasks = new HashMap<>();
    private HashMap<Integer, Epic> allEpics = new HashMap<>();
    private HashMap<Integer, Subtask> allSubtasks = new HashMap<>();
    private int id = 0;

    public void createNewTask(Task task) {
        id++;
        task.setId(id);
        allTasks.put(id, task);
    }

    public void createNewSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        allSubtasks.put(id, subtask);
        allEpics.get(subtask.getEpicId()).setSubtaskIdList(id);
    }

    public void createNewEpic(Epic epic) {
        id++;
        epic.setId(id);
        allEpics.put(id, epic);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(allSubtasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(allEpics.values());
    }

    public void clearAllTasks() {
        allTasks.clear();
    }

    public void clearAllEpics() {
        allEpics.clear();
    }

    public void clearAllSubtasks() {
        allSubtasks.clear();
        for (Epic epic : allEpics.values()) {
            epic.clearAllSubtasks();
        }
    }

    public void updateTask(Task task, String status) {
        task.setStatus(status);
        allTasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask, String status) {
        subtask.setStatus(status);
        allSubtasks.put(subtask.getId(), subtask);
        allEpics.get(subtask.getEpicId()).checkEpicStatus(allSubtasks);
    }

    public void updateEpic(Epic epic, String status) {
        epic.setStatus(status);
        allEpics.put(epic.getId(), epic);
        for (int id : epic.getSubtasksList()) {
            if (getTaskById(id) == null) {
                continue;
            }
            getTaskById(id).setStatus(status);
        }
    }

    public Task getTaskById(int id) {
        if (allTasks.get(id) == null) {
            return null;
        }
        return allTasks.get(id);
    }

    public Epic getEpicById(int id) {
        if (allEpics.get(id) == null) {
            return null;
        }
        return allEpics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        if (allSubtasks.get(id) == null) {
            return null;
        }
        return allSubtasks.get(id);
    }

    public void removeTaskById(int id) {
        if (!allTasks.containsKey(id)) {
            return;
        }
        allTasks.remove(id);
    }

    public void removeEpicById(int id) { // нужно удалить все Subtask которые относились к Epiс'у
        if (!allEpics.containsKey(id)) {
            return;
        }
        for (int i = 0; i < allEpics.get(id).getSubtasksList().size(); i++) {
            allSubtasks.remove(allEpics.get(id).getSubtasksList().get(i));
        }
        allEpics.remove(id);
    }

    public void removeSubtaskById(int id) {
        if (!allSubtasks.containsKey(id)) {
            return;
        }
        allSubtasks.remove(id);
    }

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
}