package HttpTaskServer.Handlers;

import Interfaces.TaskManager;
import Manager.Exeptions.SaveException;
import Manager.Exeptions.TimeException;
import Manager.InMemoryTaskManager;
import Tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    TaskManager taskManager;
    Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /task запроса");
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();
        InputStream inputStream = httpExchange.getRequestBody();
        String bodyString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        int responseCode = 0;
        String response = "";
        switch (method) {
            case "GET":
                try {
                    if (path.contains("/tasks/") && path.split("/").length == 3) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        if (taskManager.getTaskById(id) == null) {
                            responseCode = 404;
                            response = "Not Found";
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getTaskById(id));
                        }
                    } else if (path.equals("/tasks")) {
                        responseCode = 200;
                        response = gson.toJson(taskManager.getAllTasks());
                    } else {
                        responseCode = 404;
                        response = "Not Found";
                    }
                    break;
                } catch (NumberFormatException exception) {
                    responseCode = 404;
                    response = "Not Found";
                }
            case "POST":
                try {
                    Task taskFromJson = gson.fromJson(bodyString, Task.class);
                    if (taskFromJson.getId() == 0) {
                        responseCode = 201;
                        taskManager.createNewTask(taskFromJson);
                        response = gson.toJson(taskManager.getTaskById(taskFromJson.getId()));
                    } else {
                        taskManager.updateTask(taskFromJson);
                        responseCode = 201;
                        response = gson.toJson(taskManager.getTaskById(taskFromJson.getId()));
                    }
                    break;
                } catch (TimeException exception) {
                    responseCode = 406;
                    response = "Not Acceptable";
                    break;
                } catch (SaveException exception) {
                    responseCode = 500;
                    response = "Internal Server Error";
                    break;
                }
            case "DELETE":
                int id = Integer.parseInt(query.substring(3));
                taskManager.removeTaskById(id);
                responseCode = 200;
                response = "Задача удалена";
                break;
        }
        httpExchange.sendResponseHeaders(responseCode, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("Код ответа: " + responseCode + "; тело ответа: " + response);
    }
}
