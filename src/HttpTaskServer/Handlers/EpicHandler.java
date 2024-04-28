package HttpTaskServer.Handlers;

import Interfaces.TaskManager;
import Manager.Exeptions.SaveException;
import Tasks.Epic;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EpicHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    private boolean checkId(String path) {
        int Id = Integer.parseInt(path.split("/")[2]);
        return taskManager.getEpicById(Id) == null;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
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
                    Epic taskFromJson = gson.fromJson(bodyString, Epic.class);
                    if (path.contains("/epics/") && path.split("/").length == 3) {
                        if (checkId(path)) {
                            responseCode = 404;
                            response = "Not Found (epic`ов нет)";
                        } else {
                            responseCode = 200;
                            response = gson.toJson(taskManager.getEpicById(Integer.parseInt(path.split("/")[2])));
                        }
                    } else if (path.contains("/epics/") && path.split("/")[3].equals("subtasks")) {
                        if (checkId(path)) {
                            responseCode = 404;
                            response = "Not Found (subtask`ов с таким epicId нет)";
                        } else {
                            int id = Integer.parseInt(path.split("/")[2]);
                            responseCode = 200;
                            taskManager.createNewEpic(taskFromJson);
                            response = gson.toJson(taskManager.getSubtasksByEpicId(id));
                        }
                    } else if (path.equals("/epics")) {
                        responseCode = 200;
                        response = gson.toJson(taskManager.getAllEpics());
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
                    Epic epicFromJson = gson.fromJson(bodyString, Epic.class);
                    if (epicFromJson.getId() == 0) {
                        taskManager.createNewEpic(epicFromJson);
                    } else {
                        taskManager.updateEpic(epicFromJson);
                    }
                    response = gson.toJson(taskManager.getEpicById(epicFromJson.getId()));
                    responseCode = 201;
                    break;
                } catch (SaveException exception) {
                    responseCode = 500;
                    response = "Internal Server Error";
                    break;
                }
            case "DELETE":
                int id = Integer.parseInt(query.substring(3));
                taskManager.removeEpicById(id);
                responseCode = 200;
                response = "Задача удалена";
                break;
            default:
                responseCode = 404;
                response = "Not Found";
                break;
        }
        httpExchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("Код ответа: " + responseCode + "; тело ответа: " + response);
    }
}