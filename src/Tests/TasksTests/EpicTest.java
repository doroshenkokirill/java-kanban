package Tests.TasksTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EpicTest {
    protected TaskManager taskManager = Manager.getDefault();
    protected Epic epic = new Epic("Epic");

    @Test
    protected void compareEpicsById() { // Сравниваем Epic`и с одинаковым Id
        taskManager.createNewEpic(epic);
        Assertions.assertEquals(epic.getId(), taskManager.getEpicById(epic.getId()).getId());
    }

    @Test
    protected void addEpicLikeSubtask() {
        taskManager.createNewEpic(epic);
        //taskManager.createNewSubtask(epic);
    }
}