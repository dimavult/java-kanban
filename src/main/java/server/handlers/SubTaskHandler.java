package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import server.ServerMethod;
import task.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class SubTaskHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public SubTaskHandler(TaskManager manager, Gson gson) {
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
                response = get(lastValue, splitString, httpExchange);
                break;
            case POST:
                post(httpExchange);
                break;
            case DELETE:
                delete(lastValue,httpExchange);
                break;
            default:
                throw new RuntimeException("Incorrect method");
        }
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String get(String lastValue, String[] splitString, HttpExchange h) throws IOException {
        String response = "";
        if (lastValue.equals("subtask")) {
            List<SubTask> subTasks = manager.getSubTasksList();
            response = gson.toJson(subTasks);
            h.sendResponseHeaders(200, 0);
        } else if (splitString.length == 4) {
            String[] parsedId = lastValue.split("=");
            int id = Integer.parseInt(parsedId[1]);
            SubTask subTasks = manager.getSubTaskById(id);
            if (subTasks != null) {
                response = gson.toJson(subTasks);
                h.sendResponseHeaders(200, 0);
            } else {
                h.sendResponseHeaders(204, 0);
            }
        } else if (splitString.length == 5) {
            String[] parsedId = lastValue.split("=");
            int id = Integer.parseInt(parsedId[1]);
            List<SubTask> epicsSubtasks = manager.getAllEpicsSubtasks(id);
            response = gson.toJson(epicsSubtasks);

            h.sendResponseHeaders(200, 0);
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
        SubTask subTask = gson.fromJson(jsonObject, SubTask.class);

        if (manager.getSubTaskById(subTask.getId()) == null) {
            manager.addNewSubTask(subTask);
            h.sendResponseHeaders(201, 0);
        } else {
            manager.updateSubTask(subTask);
            h.sendResponseHeaders(200, 0);
        }
    }

    private void delete(String lastValue, HttpExchange h) throws IOException {
        if (lastValue.equals("subtask")) {
            manager.removeAllSubTasks();
            h.sendResponseHeaders(200, 0);
        } else {
            String[] parsedId = lastValue.split("=");
            int id = Integer.parseInt(parsedId[1]);
            manager.removeSubTaskByIdentifier(id);
            h.sendResponseHeaders(200, 0);
        }
    }
}