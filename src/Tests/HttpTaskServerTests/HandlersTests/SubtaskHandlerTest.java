package Tests.HttpTaskServerTests.HandlersTests;

import HttpTaskServer.HttpTaskServer;
import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
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

public class SubtaskHandlerTest {
    private static final int PORT = 8080;
    // создаём экземпляр InMemoryTaskManager
    private final TaskManager taskManager = Manager.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    private final HttpTaskServer server = new HttpTaskServer(taskManager);
    private final Gson gson = HttpTaskServer.getGson();

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
    protected void getSubtaskWith200CodeTest() throws InterruptedException, IOException {
        URI uri = URI.create("http://localhost:" + PORT + "/subtasks");
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
    protected void getSubtaskWith404CodeTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/subtasks/");
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
    protected void getSubtaskByIdWith200CodeTest() throws InterruptedException, IOException {
        // создаём задачу 1
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        Epic epic = new Epic("Epic Test");
        taskManager.createNewEpic(epic);
        Subtask task = new Subtask(epic.getId(), "Test", "Testing Subtask", localDateTime, 5);
        task.setEndTime(task.getEndTime());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/subtasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        // вызываем Task
        URI uri1 = URI.create("http://localhost:" + PORT + "/subtasks?id=1");
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
    protected void createSubtaskWith201CodeTest() throws IOException, InterruptedException {
        // создаём задачу 1
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        Epic epic = new Epic("Epic Test");
        taskManager.createNewEpic(epic);
        Subtask task = new Subtask(epic.getId(), "Test", "Testing task", localDateTime, 5);
        task.setEndTime(task.getEndTime());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    protected void updateSubtaskWith201CodeTest() throws IOException, InterruptedException {
        // создаём задачу 1
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        Epic epic = new Epic("Epic Test");
        taskManager.createNewEpic(epic);
        Subtask task = new Subtask(epic.getId(), "Test", "Testing task", localDateTime, 5);
        task.setEndTime(task.getEndTime());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/subtasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // обновляем задачу 1
        task.setDuration(Duration.ofMinutes(10));
        task.setId(1);
        String taskJson1 = gson.toJson(task);
        HttpClient client1 = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response1.statusCode());
    }

    @Test
    protected void deleteSubtaskWith200CodeTest() throws IOException, InterruptedException {
        // создаём задачу
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        Epic epic = new Epic("Epic Test");
        taskManager.createNewEpic(epic);
        Subtask task = new Subtask(epic.getId(), "Test", "Testing Subtask", localDateTime, 5);
        task.setEndTime(task.getEndTime());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/subtasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        // удаляем задачу
        URI uri1 = URI.create("http://localhost:" + PORT + "/subtasks?id=1");
        HttpRequest request1 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri1)
                .build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response1 = client1.send(request1, handler);
        assertEquals(200, response1.statusCode(), "Задача удалена");
    }
}