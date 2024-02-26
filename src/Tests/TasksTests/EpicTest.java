package Tests.TasksTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class EpicTest {
    TaskManager taskManager = Manager.getDefault();
    Epic epic = new Epic("Epic description");


    @Test
    void compareEpicsById() { // Сравниваем Epic`и с одинаковым Id
        taskManager.createNewTask(epic);
        Assertions.assertEquals(epic.getId(), taskManager.getEpicById(epic.getId()).getId(), "Одинаковые Epic`и c одинаковыми Id");
    }

    @Test
    void addEpicLikeSubtask() {
        taskManager.createNewEpic(epic);
        //taskManager.createNewSubtask(epic);
    }
}