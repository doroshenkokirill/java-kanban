package HttpTaskServer.Handlers;

import Interfaces.TaskManager;
import Manager.Exeptions.SaveException;
import Manager.Exeptions.TimeException;
import Tasks.Subtask;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        InputStream inputStream = exchange.getRequestBody();
        String bodyString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        int responseCode = 0;
        String response = "";

        switch (method) {
            case "GET":
                try {
                    if (path.contains("/subtasks/") && path.split("/").length == 3) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        if (taskManager.getSubtaskById(id) == null) {
                            responseCode = 404;
                            response = "Not FoundNot Found";
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getSubtaskById(id));
                        }
                    } else if (path.equals("/subtasks")) {
                        responseCode = 200;
                        response = gson.toJson(taskManager.getAllSubtasks());
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
                    Subtask subTaskFromJson = gson.fromJson(bodyString, Subtask.class);
                    if (subTaskFromJson.getId() == 0) {
                        taskManager.createNewSubtask(subTaskFromJson);
                    } else {
                        taskManager.updateSubtask(subTaskFromJson);
                    }
                    responseCode = 201;
                    response = gson.toJson(taskManager.getSubtaskById(subTaskFromJson.getId()));
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
                taskManager.removeSubtaskById(id);
                responseCode = 200;
                response = "Задача удалена";
                break;
        }
        exchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}