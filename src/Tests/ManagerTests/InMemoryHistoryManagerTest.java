package Tests.ManagerTests;

import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryHistoryManagerTest {
    protected TaskManager taskManager = Manager.getDefault();
    protected List<Task> browsingHistory = taskManager.getHistory();

    @Test
    protected void checkHistoryManagerSave() {
        Task task = new Task("Task Test", "Task description", "11:00 20.10.20", 10);
        Epic epic = new Epic("Epic test");
        taskManager.createNewTask(task);
        taskManager.createNewEpic(epic);
        Subtask subtask = new Subtask(epic.getId(), "Subtask test", "Subtask description", "11:20 20.10.20", 10);
        taskManager.createNewSubtask(subtask);
        browsingHistory.add(task);
        browsingHistory.add(epic);
        browsingHistory.add(subtask);
        Assertions.assertEquals(task, browsingHistory.get(0));
        Assertions.assertEquals(epic, browsingHistory.get(1));
        Assertions.assertEquals(subtask, browsingHistory.get(2));
    }

    @Test
    protected void checkEmptyHistory() {
        System.out.println(browsingHistory.isEmpty());
    }
}