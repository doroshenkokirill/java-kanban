package Tests.ManagerTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest {
    protected TaskManager taskManager = Manager.getDefault();

    @Test
    protected void checkInMemoryTaskManagerByAddEachTypeOfTask() {
        Task task = new Task("Task Test", "Task description", "12:11 11.11.11", 11);
        taskManager.createNewTask(task);
        Epic epic = new Epic("Epic for Subtask test");
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(epic.getId(),
                "Subtask test", "Description of Subtask test", "11:11 11.11.11", 12);
        taskManager.createNewSubtask(subtask);
        Assertions.assertEquals(taskManager.getTaskById(task.getId()), task);
        Assertions.assertEquals(taskManager.getEpicById(epic.getId()), epic);
        Assertions.assertEquals(taskManager.getSubtaskById(subtask.getId()), subtask);
    }

    @Test
    protected void checkIdOfSubtasks() {
        Epic epic = new Epic("Epic for Subtask test");
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(epic.getId(), "Subtask test", "Description of Subtask test","11:00 20.10.20", 10);
        taskManager.createNewSubtask(subtask);
        Subtask subtask1 = new Subtask(1, "Subtask test", "Description of Subtask test", "11:20 20.10.20", 10);
        taskManager.createNewSubtask(subtask1);
        System.out.println(epic.getSubtasksList());
    }

    @Test
    protected void checkFieldsAfterAddInTaskManager() {
        // Проверка Epic`а
        Epic epic = new Epic("Epic for Subtask test");
        String nameOfEpicBeforeTaskManager = epic.getName();
        String nameOfEpicStatusBeforeTaskManager = String.valueOf(epic.getStatus());
        int IdOfEpicBeforeTaskManager = epic.getId();
        taskManager.createNewEpic(epic);
        String nameOfEpicAfterTaskManager = taskManager.getEpicById(epic.getId()).getName();
        String nameOfEpicStatusAfterTaskManager = String.valueOf(taskManager.getEpicById(epic.getId()).getStatus());
        Assertions.assertEquals(nameOfEpicAfterTaskManager, nameOfEpicBeforeTaskManager);
        Assertions.assertEquals(nameOfEpicStatusBeforeTaskManager, nameOfEpicStatusAfterTaskManager);
        Assertions.assertEquals(IdOfEpicBeforeTaskManager, 0); // до добавления задач в TaskManager id = 0 всегда
        // Проверка Task`a
        Task task = new Task("Task test", "Task description", "11:00 20.10.20", 10);
        taskManager.createNewTask(task);
        Assertions.assertEquals(task.getName(), taskManager.getTaskById(task.getId()).getName());
        Assertions.assertEquals(task.getDescription(), taskManager.getTaskById(task.getId()).getDescription());
        Assertions.assertEquals(task.getStatus(), taskManager.getTaskById(task.getId()).getStatus());
        // Проверка Subtask`a
        Subtask subtask = new Subtask(epic.getId(), "Subtask test", "Description of Subtask test", "11:30 20.10.20", 10);
        taskManager.createNewSubtask(subtask);
        Assertions.assertEquals(subtask.getName(), taskManager.getSubtaskById(subtask.getId()).getName());
        Assertions.assertEquals(subtask.getDescription(), taskManager.getSubtaskById(subtask.getId()).getDescription());
        Assertions.assertEquals(subtask.getStatus(), taskManager.getSubtaskById(subtask.getId()).getStatus());
    }
}