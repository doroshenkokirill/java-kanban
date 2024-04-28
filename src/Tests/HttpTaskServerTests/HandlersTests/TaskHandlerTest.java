package Tests.HttpTaskServerTests.HandlersTests;

import HttpTaskServer.HttpTaskServer;
import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Task;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static Tasks.Task.DATE_TIME_FORMATTER;
import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest {
    private static final int PORT = 8080;
    // создаём экземпляр InMemoryTaskManager
    private final TaskManager taskManager = Manager.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    private final HttpTaskServer server = new HttpTaskServer(taskManager);
    private final Gson gson = HttpTaskServer.getGson();

    protected TaskHandlerTest() throws IOException {
    }

    @BeforeEach
    protected void setUp() throws IOException {
        taskManager.clearAllTasks();
        taskManager.clearAllSubtasks();
        taskManager.clearAllEpics();
        HttpTaskServer.start();
    }

    @AfterEach
    protected void shutDown() {
        HttpTaskServer.stop();
    }

    @Test
    protected void getTasksWith200CodeTest() throws InterruptedException, IOException {
        URI uri = URI.create("http://localhost:" + PORT + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        assertEquals(200, response.statusCode());
    }

    @Test
    protected void getTaskByIdWith200CodeTest() throws InterruptedException, IOException {
        // создаём задачу 1
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        Task task = new Task("Test", "Testing task", localDateTime, 5);
        task.setEndTime(task.getEndTime());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        // вызываем Task
        URI uri1 = URI.create("http://localhost:" + PORT + "/tasks?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .GET()
                .uri(uri1)
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response1 = httpClient.send(request1, handler);
        assertEquals(200, response1.statusCode());
    }

    @Test
    protected void getTasksByIdTestWith404CodeTest() throws IOException, InterruptedException {
        //вызываем задачу с Id которого нет
        URI uri = URI.create("http://localhost:" + PORT + "/tasks/0");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(404, response.statusCode(), "Not Found");
    }

    @Test
    protected void UpdateTaskWith201CodeTest() throws IOException, InterruptedException {
        // создаём задачу 1
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        Task task = new Task("Test", "Testing task", localDateTime, 5);
        task.setEndTime(task.getEndTime());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // обновляем задачу 1
        task.setDuration(Duration.ofMinutes(10));
        String taskJson1 = gson.toJson(task);
        HttpClient client1 = HttpClient.newHttpClient();
        URI uri1 = URI.create("http://localhost:" + PORT + "/tasks/");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri1)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response1.statusCode());
    }

    @Test
    protected void createTaskWith201CodeTest() throws IOException, InterruptedException {
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        // создаём задачу
        Task task = new Task("Test", "Testing task", localDateTime, 5);
        task.setEndTime(task.getEndTime());
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());
    }

    @Test
    protected void createTaskWith406CodeTest() throws IOException, InterruptedException {
        // создаем задачу 1
        String localDateTime = "11:11 11.11.11";
        Task task = new Task("Test", "Testing task", localDateTime, 5);
        task.setEndTime(task.getEndTime());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // создаем задачу c пересечением по времени
        String localDateTime1 = "11:12 11.11.11";
        Task task1 = new Task("Test", "Testing task", localDateTime1, 5);
        task1.setEndTime(task1.getEndTime());
        String taskJson1 = gson.toJson(task1);

        HttpClient client1 = HttpClient.newHttpClient();
        URI uri1 = URI.create("http://localhost:" + PORT + "/tasks/1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri1)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response1.statusCode());
    }
    @Test
    protected void deleteTaskWith200CodeTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/tasks?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request1, handler);
        assertEquals(200, response.statusCode(), "Задача удалена");
    }
}