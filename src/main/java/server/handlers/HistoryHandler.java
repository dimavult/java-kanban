package server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import server.ServerMethod;
import task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HistoryHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public HistoryHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ServerMethod method = ServerMethod.valueOf(httpExchange.getRequestMethod());
        String response = "";
        String path = httpExchange.getRequestURI().getPath();
        String[] splitString = path.split("/");
        String lastValue = splitString[splitString.length - 1];

        if (!ServerMethod.GET.equals(method) || !"history".equals(lastValue)) {
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }

        List<Task> history = manager.getHistory();
        if (!history.isEmpty()) {
            response = gson.toJson(history);
            httpExchange.sendResponseHeaders(200, 0);
        } else {
            httpExchange.sendResponseHeaders(204, 0);
        }

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
