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

public class EpicHandlerTest {
    private static final int PORT = 8080;
    // создаём экземпляр InMemoryTaskManager
    private final TaskManager taskManager = Manager.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    private final HttpTaskServer server = new HttpTaskServer(taskManager);
    private final Gson gson = HttpTaskServer.getGson();

    protected EpicHandlerTest() throws IOException {
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
    protected void getEpicWith200CodeTest() throws InterruptedException, IOException {
        URI uri = URI.create("http://localhost:" + PORT + "/epics");
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
    protected void getEpicByIdWith200CodeTest() throws InterruptedException, IOException {
        Epic epic = new Epic("Epic Test");
        epic.setId(1);
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        Subtask task = new Subtask(1, "Test", "Testing Subtask", localDateTime, 5);
        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    protected void getEpicByIdWith404CodeTest() throws InterruptedException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/epics/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    protected void createEpicWith201CodeTest() throws InterruptedException, IOException {
        Epic epic = new Epic("Epic Test");
        Subtask subtask = new Subtask(epic.getId(), "Subtask", "Description", LocalDateTime.now().format(DATE_TIME_FORMATTER), 10);
        epic.setStartTime(subtask.getStartTime());
        epic.setDuration(subtask.getDuration());
        String taskJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/epics/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    protected void getSubtasksByEpicIdWith200CodeTest() throws InterruptedException, IOException {
        //POST Subtask
        String localDateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).format(DATE_TIME_FORMATTER);
        Epic epic = new Epic("Epic Test");
        epic.setId(1);
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
        //POST Epic
        String taskJson1 = gson.toJson(epic);
        HttpClient client1 = HttpClient.newHttpClient();
        URI uri1 = URI.create("http://localhost:" + PORT + "/epics");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri1)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();
        HttpResponse<String> response1 = client1.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        HttpClient client2 = HttpClient.newHttpClient();
        URI uri2 = URI.create("http://localhost:" + PORT + "/epics/1/subtasks");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uri2)
                .GET()
                .build();
        HttpResponse<String> response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response2.statusCode());
    }

    @Test
    protected void getSubtasksByEpicIdWith404CodeTest() throws InterruptedException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/epics/0/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    protected void deleteEpicWith200CodeTest() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/epics?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        assertEquals(200, response.statusCode(), "Задача удалена");
    }
}