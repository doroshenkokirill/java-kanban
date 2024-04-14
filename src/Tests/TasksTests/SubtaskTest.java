package Tests.TasksTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubtaskTest {
    protected TaskManager taskManager = Manager.getDefault();
    protected Epic epic = new Epic( "Subtask Test");

    @Test
    protected void compareSubtasksById() { // Сравниваем Epic`и с одинаковым Id
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(taskManager.getEpicById(epic.getId()).getId(), "Subtask test", "Description of Subtask test");
        taskManager.createNewSubtask(subtask);
        Assertions.assertEquals(subtask.getId(), taskManager.getSubtaskById(subtask.getId()).getId(), "Одинаковые Epic`и c одинаковыми Id");
    }

    @Test
    protected void addSubtaskLikeEpic() {
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(taskManager.getEpicById(epic.getId()).getId(), "Subtask test", "Description of Subtask test");
        taskManager.createNewSubtask(subtask);
        //taskManager.createNewEpic(subtask);
    }
}