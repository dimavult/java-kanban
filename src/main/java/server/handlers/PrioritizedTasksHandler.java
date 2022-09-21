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

public class PrioritizedTasksHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public PrioritizedTasksHandler(TaskManager manager, Gson gson) {
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

        if (!ServerMethod.GET.equals(method) || !"tasks".equals(lastValue)) {
            httpExchange.sendResponseHeaders(400, 0);
            response = "Неверная команда";
        } else {
            List<Task> prioritizedTasks = manager.getPrioritizedTasks();
            if (!prioritizedTasks.isEmpty()) {
                response = gson.toJson(prioritizedTasks);
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                httpExchange.sendResponseHeaders(204, 0);
            }
        }

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
