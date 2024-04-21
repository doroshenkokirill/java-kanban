package Tests.ManagerTests;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Manager.Exeptions.TimeException;
import Manager.FileBackedTasksManager;
import Manager.InMemoryHistoryManager;
import Manager.Exeptions.SaveException;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest {
    protected File file;
    protected TaskManager taskManager;
    protected HistoryManager historyManager;

    @BeforeEach
    protected void createNewFile() throws IOException {
        file = File.createTempFile("tasksInfo", ".csv");
        taskManager = new FileBackedTasksManager(file);
        historyManager = new InMemoryHistoryManager();
    }

    // проверка работы с пустым файлом
    @Test
    protected void saveEmptyFile() throws SaveException {
        taskManager.save();
        assertTrue(file.exists());
    }

    // проверка работы с записью файлов
    @Test
    protected void testSaveAndLoadTasks() throws TimeException, IOException, SaveException {
        FileBackedTasksManager taskManagerToFile = new FileBackedTasksManager(file);

        Task task1ForTest = new Task("Test Task1",
                "Description Task1 for Test","10:00 11.11.11", 10);
        taskManagerToFile.createNewTask(task1ForTest);
        Task task2ForTest = new Task("Test Task2",
                "Description Task2 for Test","15:00 11.11.11", 20);
        taskManagerToFile.createNewTask(task2ForTest);
        Epic epic1ForTest = new Epic("Test Epic1");
        epic1ForTest.setDescription("Description Epic for Test");
        taskManagerToFile.createNewEpic(epic1ForTest);
        Subtask subtask1ForTest = new Subtask(epic1ForTest.getId(),
                "Test Subtask1", "Description Subtask1 for Test", "17:00 11.11.11", 10);
        taskManagerToFile.createNewSubtask(subtask1ForTest);
        Subtask subtask2ForTest = new Subtask(epic1ForTest.getId(),
                "Test Subtask1", "Description Subtask1 for Test", "19:00 11.11.11", 10);
        taskManagerToFile.createNewSubtask(subtask2ForTest);
        taskManagerToFile.setTimeForEpic(epic1ForTest);
        taskManagerToFile.save();

        FileBackedTasksManager taskManagerFromFile = FileBackedTasksManager.loadFromFile(file);
        Epic epicFromFile = taskManagerFromFile.getEpicById(epic1ForTest.getId());
        Subtask subtaskFromFile1 = taskManagerFromFile.getSubtaskById(subtask1ForTest.getId());
        Subtask subtaskFromFile2 = taskManagerFromFile.getSubtaskById(subtask2ForTest.getId());

        System.out.println(task1ForTest);
        System.out.println(task2ForTest);
        System.out.println(epicFromFile.toString());
        System.out.println(subtaskFromFile1.toString());
        System.out.println(subtaskFromFile2.toString());
    }

    @Test
    protected void testHistory() throws SaveException {
        FileBackedTasksManager taskManagerToFile = new FileBackedTasksManager(file);

        Task task1ForTest = new Task("Test Task1",
                "Description Task1 for Test", "11:00 20.10.20", 10);
        taskManagerToFile.createNewTask(task1ForTest);
        Task task2ForTest = new Task("Test Task2",
                "Description Task2 for Test", "11:20 20.10.20", 10);
        taskManagerToFile.createNewTask(task2ForTest);
        Epic epic1ForTest = new Epic("Test Epic1");
        epic1ForTest.setDescription("Description Epic for Test");
        taskManagerToFile.createNewEpic(epic1ForTest);
        Subtask subtask1ForTest = new Subtask(epic1ForTest.getId(),
                "Test Subtask1", "Description Subtask1 for Test", "11:40 20.10.20", 10);
        taskManagerToFile.createNewSubtask(subtask1ForTest);
        taskManagerToFile.save();

        historyManager.add(task1ForTest);
        historyManager.add(task2ForTest);
        historyManager.add(epic1ForTest);
        historyManager.add(subtask1ForTest);
        String historyToFile = FileBackedTasksManager.historyToString(historyManager);
        List<Integer> historyFromFile = FileBackedTasksManager.historyFromString(historyToFile);
        assertEquals(1, historyFromFile.get(0));
        assertEquals(epic1ForTest, taskManagerToFile.getEpicById(historyFromFile.get(2)));
    }
}