package Interfaces;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatusList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    void updateTask(Task task, TaskStatusList status);

    void updateSubtask(Subtask subtask, TaskStatusList status);

    void updateEpic(Epic epic, TaskStatusList status);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    ArrayList<Subtask> getSubtasksByEpicId(int id);

    List<Task> getHistory();
}
