package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {
    void createNewTask(Task task);

    void createNewSubtask(Subtask subtask);

    void createNewEpic(Epic epic);
    ArrayList<Task> getAllTasks();
    ArrayList<Subtask> getAllSubtasks();
    ArrayList<Epic> getAllEpics();
    void clearAllTasks();
    void clearAllEpics();
    void clearAllSubtasks();
    void updateTask(Task task, String status);
    void updateSubtask(Subtask subtask, String status);
    void updateEpic(Epic epic, String status);
    Task getTaskById(int id);
    Epic getEpicById(int id);
    Subtask getSubtaskById(int id);
    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(int id);
    HashMap<Integer, Subtask> getSubtasksByEpicId(int id);
}
