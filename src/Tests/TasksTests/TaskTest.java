package Tests.TasksTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TaskTest {
    protected TaskManager taskManager = Manager.getDefault();
    protected Task task = new Task("Task 1", "Description");

    @Test
    protected void add() { // Проверка создания Task`и
        taskManager.createNewTask(task);
        List<Task> history = taskManager.getAllTasks();
        Assertions.assertEquals(1, history.size());
    }

    @Test
    protected void compareTasksById() { // Сравниваем Task`и с одинаковым Id
        taskManager.createNewTask(task);
        Assertions.assertEquals(task.getId(), taskManager.getTaskById(task.getId()).getId());
    }
}