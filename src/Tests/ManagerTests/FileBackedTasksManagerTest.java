package Tests.ManagerTests;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Manager.FileBackedTasksManager;
import Manager.InMemoryHistoryManager;
import Manager.ManagerSaveException;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest {
    private File file;
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    public void createNewFile() throws IOException {
        file = File.createTempFile("tasksInfo", ".csv");
        taskManager = new FileBackedTasksManager(file);
        historyManager = new InMemoryHistoryManager();
    }

    // проверка работы с пустым файлом
    @Test
    public void saveEmptyFile() throws ManagerSaveException {
        taskManager.save();
        assertTrue(file.exists());
    }

    // проверка работы с записью файлов
    @Test
    void testSaveAndLoadTasks() throws ManagerSaveException, IOException {
        FileBackedTasksManager taskManagerToFile = new FileBackedTasksManager(file);

        Task task1ForTest = new Task("Test Task1", "Description Task1 for Test");
        taskManagerToFile.createNewTask(task1ForTest);
        Task task2ForTest = new Task("Test Task2", "Description Task2 for Test");
        taskManagerToFile.createNewTask(task2ForTest);
        Epic epic1ForTest = new Epic("Test Epic1");
        epic1ForTest.setDescription("Description Epic for Test");
        taskManagerToFile.createNewEpic(epic1ForTest);
        Subtask subtask1ForTest = new Subtask(epic1ForTest.getId(), "Test Subtask1", "Description Subtask1 for Test");
        taskManagerToFile.createNewSubtask(subtask1ForTest);
        taskManagerToFile.save();

        FileBackedTasksManager taskManagerFromFile = FileBackedTasksManager.loadFromFile(file);
        Task taskFromFile = taskManagerFromFile.getTaskById(task1ForTest.getId());
        assertEquals(taskFromFile, task1ForTest); // Непонял как побороть
    }

    @Test
    void testHistory() throws ManagerSaveException {
        FileBackedTasksManager taskManagerToFile = new FileBackedTasksManager(file);

        Task task1ForTest = new Task("Test Task1", "Description Task1 for Test");
        taskManagerToFile.createNewTask(task1ForTest);
        Task task2ForTest = new Task("Test Task2", "Description Task2 for Test");
        taskManagerToFile.createNewTask(task2ForTest);
        Epic epic1ForTest = new Epic("Test Epic1");
        epic1ForTest.setDescription("Description Epic for Test");
        taskManagerToFile.createNewEpic(epic1ForTest);
        Subtask subtask1ForTest = new Subtask(epic1ForTest.getId(), "Test Subtask1", "Description Subtask1 for Test");
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