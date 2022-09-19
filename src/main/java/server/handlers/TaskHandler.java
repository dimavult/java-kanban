package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import server.ServerMethod;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class TaskHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public TaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ServerMethod method = ServerMethod.valueOf(httpExchange.getRequestMethod());
        String response = "";
        String path = httpExchange.getRequestURI().toString();
        String[] splitString = path.split("/");
        String lastValue = splitString[splitString.length - 1];

        switch (method) {
            case GET:
                response = get(lastValue, httpExchange);
                break;
            case POST:
                post(httpExchange);
                break;
            case DELETE:
                delete(lastValue, httpExchange);
                break;
            default:
                throw new RuntimeException("Incorrect method");
        }
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String get(String lastValue, HttpExchange h) throws IOException {
        String response = "";
        if (lastValue.equals("task")) {
            List<Task> tasks = manager.getTasksList();
            response = gson.toJson(tasks);
            h.sendResponseHeaders(200, 0);
        } else {
            String[] parsedId = lastValue.split("=");
            int id = Integer.parseInt(parsedId[1]);
            Task task = manager.getTaskById(id);
            if (task != null) {
                response = gson.toJson(task);
                h.sendResponseHeaders(200, 0);
            } else {
                h.sendResponseHeaders(204, 0);
            }
        }
        return response;
    }

    private void post(HttpExchange h) throws IOException {
        InputStream inputStream = h.getRequestBody();
        String body = new String(inputStream.readAllBytes());
        JsonElement jsonElement = JsonParser.parseString(body);

        if (!jsonElement.isJsonObject()) {
            System.out.println("Передан не jsonObject");
            return;
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task task = gson.fromJson(jsonObject, Task.class);

        if (manager.getTaskById(task.getId()) == null) {
            manager.addNewTask(task);
            h.sendResponseHeaders(201, 0);
        } else {
            manager.updateTask(task);
            h.sendResponseHeaders(200, 0);
        }
    }

    private void delete(String lastValue, HttpExchange h) throws IOException {
        if (lastValue.equals("task")) {
            manager.removeAllTasks();
            h.sendResponseHeaders(200, 0);
        } else {
            String[] parsedId = lastValue.split("=");
            int id = Integer.parseInt(parsedId[1]);
            manager.removeTaskByIdentifier(id);
            h.sendResponseHeaders(200, 0);
        }
    }
}