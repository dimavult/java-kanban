package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import server.ServerMethod;
import task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class EpicHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public EpicHandler(TaskManager manager, Gson gson) {
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
                throw new RuntimeException("Выбран несуществующий метод");
        }
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void delete(String lastValue, HttpExchange h) throws IOException{
        if (lastValue.equals("epic")) {
            manager.removeAllSubTasks();
            h.sendResponseHeaders(200, 0);
        } else {
            String[] parsedId = lastValue.split("=");
            int id = Integer.parseInt(parsedId[1]);
            manager.removeEpicByIdentifier(id);
            h.sendResponseHeaders(200, 0);
        }
    }

    private String get(String lastValue, HttpExchange h) throws  IOException{
        String response = "";
        if (lastValue.equals("epic")) {
            List<Epic> epics = manager.getEpicsList();
            response = gson.toJson(epics);
            h.sendResponseHeaders(200, 0);
        } else {
            String[] parsedId = lastValue.split("=");
            int id = Integer.parseInt(parsedId[1]);
            Epic epic = manager.getEpicById(id);
            if (epic != null) {
                response = gson.toJson(epic);
                h.sendResponseHeaders(200, 0);
            } else {
                h.sendResponseHeaders(204, 0);
            }
        }
        return response;
    }

    private void post(HttpExchange h) throws IOException{
        InputStream inputStream = h.getRequestBody();
        String body = new String(inputStream.readAllBytes());
        JsonElement jsonElement = JsonParser.parseString(body);

        if (!jsonElement.isJsonObject()) {
            System.out.println("Передан не jsonObject");
            return;
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Epic epic = gson.fromJson(jsonObject, Epic.class);
        if (manager.getEpicById(epic.getId()) == null) {
            manager.addNewEpic(epic);
            h.sendResponseHeaders(201, 0);
        } else {
            manager.updateEpic(epic);
            h.sendResponseHeaders(201, 0);
        }
    }
}
