package Tests.TasksTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static Tasks.Task.DATE_TIME_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {
    protected TaskManager taskManager = Manager.getDefault();
    protected Epic epic = new Epic( "Subtask Test");

    @Test
    protected void compareSubtasksById() { // Сравниваем Epic`и с одинаковым Id
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(taskManager.getEpicById(epic.getId()).
                getId(),"Subtask test", "Description of Subtask test", "11:00 20.10.21", 10);
        taskManager.createNewSubtask(subtask);
        assertEquals(subtask.getId(),
                taskManager.getSubtaskById(subtask.getId()).getId());
        assertEquals(Duration.ofMinutes(10), subtask.getDuration()); // сравнил Durations
        assertEquals("11:10 20.10.21", subtask.getEndTime().format(DATE_TIME_FORMATTER)); // проверка endTime
    }

    @Test
    protected void addSubtaskLikeEpic() {
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(taskManager.getEpicById(epic.getId()).
                getId(), "Subtask test", "Description of Subtask test","11:30 20.10.21", 10);
        taskManager.createNewSubtask(subtask);
        //taskManager.createNewEpic(subtask);
    }
}