package Tests.TasksTests;

import Interfaces.TaskManager;
import Manager.Exeptions.TimeException;
import Manager.Manager;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static Tasks.Task.DATE_TIME_FORMATTER;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    protected TaskManager taskManager = Manager.getDefault();
    protected Task task = new Task("Task 1", "Description1", "12:00 10.10.21", 10);
    protected Task task1 = new Task("Task 2", "Description2", "12:40 10.10.21", 1);
    protected Task task2 = new Task("Task 3", "Description3", "12:40 10.10.21", 9);


    @Test
    protected void add() { // Проверка создания Task`и
        taskManager.createNewTask(task);
        taskManager.createNewTask(task1);
        List<Task> history = taskManager.getAllTasks();
        assertEquals(2, history.size());
        assertEquals(Duration.ofMinutes(10), task.getDuration());
        task.setDuration(Duration.ofMinutes(20));
        assertEquals(Duration.ofMinutes(20), task.getDuration()); // перезапись задачи
        assertEquals("12:20 10.10.21", task.getEndTime().format(DATE_TIME_FORMATTER)); // проверка endTime
        assertThrows(TimeException.class, () -> taskManager.createNewTask(task2)); //добавляем Task с пересечением по времени
    }

    @Test
    protected void compareTasksById() { // Сравниваем Task`и с одинаковым Id
        taskManager.createNewTask(task);
        assertEquals(task.getId(), taskManager.getTaskById(task.getId()).getId());
    }
}