package Interfaces;

import Manager.ManagerSaveException;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.List;

public interface TaskManager {

    void createNewTask(Task task);

    void createNewSubtask(Subtask subtask);

    void createNewEpic(Epic epic);

    List<Task> getAllTasks();

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

    void clearAllTasks();

    void clearAllEpics();

    void clearAllSubtasks();

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    List<Subtask> getSubtasksByEpicId(int id);

    List<Task> getHistory();

    void save() throws ManagerSaveException;
}
