package Tests.ManagerTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class InMemoryHistoryManagerTest {
    TaskManager taskManager = Manager.getDefault();
    List<Task> browsingHistory = taskManager.getHistory();


    @Test
    void checkHistoryManagerSave() {
        Task task = new Task("Task Test", "Task description");
        Epic epic = new Epic("Epic test");
        taskManager.createNewTask(task);
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(epic.getId(), "Subtask test", "Subtask description");
        taskManager.createNewSubtask(subtask);
        browsingHistory.add(task);
        browsingHistory.add(epic);
        browsingHistory.add(subtask);
        Assertions.assertEquals(task, browsingHistory.get(0));
        Assertions.assertEquals(epic, browsingHistory.get(1));
        Assertions.assertEquals(subtask, browsingHistory.get(2));
    }
}