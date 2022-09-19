package server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import interfaces.TaskManager;
import server.handlers.*;
import service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager manager = Managers.getDefault(URI.create("http://localhost:8078/"));
    private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private HttpServer httpServer;

    public void startServer() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new PrioritizedTasksHandler(manager, gson));
            httpServer.createContext("/tasks/epic/", new EpicHandler(manager, gson));
            httpServer.createContext("/tasks/task/", new TaskHandler(manager, gson));
            httpServer.createContext("/tasks/subtask/", new SubTaskHandler(manager, gson));
            httpServer.createContext("/tasks/history/", new HistoryHandler(manager, gson));
            System.out.println("Запускаем HttpTaskServer сервер на порту " + PORT);
            httpServer.start();
        } catch (IOException e) {
            System.out.println("Ошибка при запуске сервера");
        }
    }

    public void stop() {
        httpServer.stop(0);
    }
}
