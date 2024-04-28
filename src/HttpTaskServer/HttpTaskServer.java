package HttpTaskServer;

import HttpTaskServer.Adapters.DurationAdapter;
import HttpTaskServer.Adapters.LocalDateTimeAdapter;
import HttpTaskServer.Handlers.*;
import Manager.Manager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

import static Manager.FileBackedTasksManager.loadFromFile;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static HttpServer httpServer;

    public static void main(String[] args) {

    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

    public void start() throws IOException {
        HttpServer httpServer = HttpServer.create();
        Gson gson = getGson();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        File file = File.createTempFile("tasksInfo", ".csv");
        httpServer.createContext("/tasks", new TaskHandler(loadFromFile(file), gson));
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(2);
    }
}
