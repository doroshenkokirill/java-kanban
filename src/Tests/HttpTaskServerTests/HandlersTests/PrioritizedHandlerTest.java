package Tests.HttpTaskServerTests.HandlersTests;

import HttpTaskServer.HttpTaskServer;
import Interfaces.TaskManager;
import Manager.Manager;
import Tasks.Task;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static Tasks.Task.DATE_TIME_FORMATTER;

public class PrioritizedHandlerTest {

    private static final int PORT = 8080;
    // создаём экземпляр InMemoryTaskManager
    private final TaskManager taskManager = Manager.getDefault();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    private final HttpTaskServer server = new HttpTaskServer(taskManager);
    private final Gson gson = HttpTaskServer.getGson();

    protected PrioritizedHandlerTest() throws IOException {
    }

    @BeforeEach
    protected void setUp() {
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        taskManager.clearAllSubtasks();
        try {
            HttpTaskServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    protected void tearDown() {
        HttpTaskServer.stop();
    }

    @Test
    protected void handle() throws IOException, InterruptedException {
        String localDateTime = LocalDateTime.of(2020,10,10, 10, 30).format(DATE_TIME_FORMATTER);
        taskManager.createNewTask(new Task("Task", "Description 1", localDateTime, 10));
        URI uri = URI.create("http://localhost:" + PORT + "/prioritized");
        String localDateTime2 = LocalDateTime.of(2020,10,10, 10, 0).format(DATE_TIME_FORMATTER);
        taskManager.createNewTask(new Task("Task2", "Description 2", localDateTime2, 20));
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(2, response.body().split("},").length);
    }
}