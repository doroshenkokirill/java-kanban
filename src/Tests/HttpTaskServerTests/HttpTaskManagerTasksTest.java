package Tests.HttpTaskServerTests;

import HttpTaskServer.HttpTaskServer;
import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Task;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static Tasks.Task.DATE_TIME_FORMATTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {
    private static final int PORT = 8080;

    // создаём экземпляр InMemoryTaskManager
    protected TaskManager taskManager = Manager.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    protected HttpTaskServer taskServer = new HttpTaskServer();
    protected Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        taskManager.clearAllSubtasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2", localDateTime, 5);
        task.setEndTime(task.getEndTime());
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = taskManager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldReturnCode404WhenGETRequest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(404, response.statusCode(), "Not Found");
    }
}