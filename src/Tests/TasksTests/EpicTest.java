package Tests.TasksTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.TaskStatusList;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static Tasks.Task.DATE_TIME_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    protected TaskManager taskManager = Manager.getDefault();
    protected Epic epic = new Epic("Epic");

    @Test
    protected void compareEpicsById() { // Сравниваем Epic`и с одинаковым Id
        taskManager.createNewEpic(epic);
        assertEquals(epic.getId(), taskManager.getEpicById(epic.getId()).getId());
    }

    @Test
    protected void addEpicLikeSubtask() {
        taskManager.createNewEpic(epic);
        //taskManager.createNewSubtask(epic);
    }

    @Test
    protected void checkTime() {
        long timeDuration = 10;
        taskManager.createNewEpic(epic);
        // SubTask`ов нет -> времени нет
        assertEquals(epic.getStartTime(), LocalDateTime.MIN);

        // Добавляем SubTask -> сверяем время
        Subtask subtask = new Subtask(taskManager.getEpicById(epic.getId()).getId(),
                "Subtask test", "Description of Subtask test", "11:00 20.10.21", timeDuration);
        taskManager.createNewSubtask(subtask);
        taskManager.setTimeForEpic(epic);
        assertEquals("11:00 20.10.21", epic.getStartTime().format(DATE_TIME_FORMATTER)); //проверка startTime
        assertEquals(Duration.ofMinutes(timeDuration), epic.getDuration()); // проверка Duration
        assertEquals("11:10 20.10.21", epic.getEndTime().format(DATE_TIME_FORMATTER)); //проверка endTime

        Subtask subtask1 = new Subtask(taskManager.getEpicById(epic.getId()).getId(),
                "Subtask test1", "Description of Subtask test1", "11:20 20.10.21", timeDuration);
        taskManager.createNewSubtask(subtask1);
        taskManager.setTimeForEpic(epic);
        assertEquals("11:00 20.10.21", epic.getStartTime().format(DATE_TIME_FORMATTER)); //проверка startTime
        assertEquals(Duration.ofMinutes(timeDuration * 2), epic.getDuration()); // проверка Duration
        assertEquals("11:30 20.10.21", epic.getEndTime().format(DATE_TIME_FORMATTER)); //проверка endTime

        subtask1.setDuration(Duration.ofMinutes(40));
        taskManager.setTimeForEpic(epic);
        assertEquals("12:00 20.10.21", epic.getEndTime().format(DATE_TIME_FORMATTER)); //проверка endTime
    }

    @Test
    protected void checkStatus() {
        taskManager.createNewEpic(epic);
        assertEquals(TaskStatusList.NEW, epic.getStatus());

        epic.setStatus(TaskStatusList.DONE);
        assertEquals(TaskStatusList.DONE, epic.getStatus());

        epic.setStatus(TaskStatusList.IN_PROGRESS);
        assertEquals(TaskStatusList.IN_PROGRESS, epic.getStatus());

        Subtask subtask = new Subtask(taskManager.getEpicById(epic.getId()).getId(),
                "Subtask test", "Description of Subtask test", "11:00 20.10.21", 10);
        taskManager.createNewSubtask(subtask);
        Subtask subtask2 = new Subtask(taskManager.getEpicById(epic.getId()).getId(),
                "Subtask test2", "Description of Subtask2 test", "12:00 20.10.21", 10);
        taskManager.createNewSubtask(subtask);
        assertEquals(TaskStatusList.IN_PROGRESS, epic.getStatus());
        subtask2.setStatus(TaskStatusList.DONE);
        subtask.setStatus(TaskStatusList.DONE);
        taskManager.updateEpicStatus(epic);
        assertEquals(TaskStatusList.DONE, epic.getStatus());
    }
}