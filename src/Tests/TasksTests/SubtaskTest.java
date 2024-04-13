package Tests.TasksTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.TaskStatusList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class SubtaskTest {
    TaskManager taskManager = Manager.getDefault();


    @Test
    void compareSubtasksById() { // Сравниваем Epic`и с одинаковым Id
        Epic epic = new Epic( "Subtask Test");
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(taskManager.getEpicById(epic.getId()).getId(), "Subtask test", "Description of Subtask test");
        taskManager.createNewSubtask(subtask);
        Assertions.assertEquals(subtask.getId(), taskManager.getSubtaskById(subtask.getId()).getId(), "Одинаковые Epic`и c одинаковыми Id");
    }

    @Test
    void addSubtaskLikeEpic() {
        Epic epic = new Epic("Subtask Test");
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(taskManager.getEpicById(epic.getId()).getId(), "Subtask test", "Description of Subtask test");
        taskManager.createNewSubtask(subtask);
        //taskManager.createNewEpic(subtask);
    }
}