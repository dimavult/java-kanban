import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();
    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    Epic epic1 = new Epic("epicName", "epicDesc");
    Epic epic2 = new Epic("epicName", "epicDesc", 1);
    Epic epic3 = new Epic("differentEpicName", "differentEpicDesc");
    SubTask subTask1 = new SubTask("subName1",
            "subDesc1",
            Status.NEW,
            1,
            Duration.ofMinutes(30),
            LocalDateTime.of(3500, 1, 1, 10, 0, 0));
    SubTask subTask2 = new SubTask("subName1",
            "subDesc1",
            Status.NEW,
            2,
            1,
            Duration.ofMinutes(30),
            LocalDateTime.of(4000, 1, 1, 10, 0, 0));
    SubTask subTask3 = new SubTask("subName1",
            "subDesc1",
            Status.NEW,
            1,
            Duration.ofMinutes(30),
            LocalDateTime.of(5000, 1, 1, 10, 0, 0));
    SubTask subTask4 = new SubTask("subName1",
            "subDesc1",
            Status.DONE,
            1,
            Duration.ofMinutes(30),
            LocalDateTime.of(3000, 1, 1, 10, 0, 0));
    Task task1 = new Task("taskName",
            "taskDesc",
            Status.IN_PROGRESS,
            Duration.ofHours(4),
            LocalDateTime.of(1000, 1, 1, 10, 0));
    Task task2 = new Task("taskName",
            "taskDesc",
            Status.DONE,
            1,
            Duration.ofHours(4),
            LocalDateTime.of(2000, 1, 1, 10, 0));
    Task task3 = new Task("taskName",
            "taskDesc",
            Status.DONE,
            Duration.ofHours(4),
            LocalDateTime.of(2000, 1, 1, 10, 0));
    String epic1Json = gson.toJson(epic1);
    String epic2Json = gson.toJson(epic2);
    String epic3Json = gson.toJson(epic3);
    String subtask1Json = gson.toJson(subTask1);
    String subtask2Json = gson.toJson(subTask2);
    String subtask3Json = gson.toJson(subTask3);
    String subtask4Json = gson.toJson(subTask4);
    String task1Json = gson.toJson(task1);
    String task2Json = gson.toJson(task2);
    String task3Json = gson.toJson(task3);

    @BeforeEach
    protected void start() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            httpTaskServer = new HttpTaskServer();
            httpTaskServer.startServer();
        } catch (IOException e) {
            throw new RuntimeException("Servers startup error");
        }
    }

    @AfterEach
    protected void stop() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    @DisplayName("task handler test")
    public void testTaskEndpoint() throws IOException, InterruptedException {
        /*
        Добавление задачи
         */
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "task wasn't added");
        /*
        Получение задачи по ID
         */
        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskFromJson = gson.fromJson(response.body(), Task.class);
        task1.setId(1);

        assertEquals(taskFromJson, task1, "task wasn't added");
        /*
        Обновление задачи
         */
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(task2Json))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "task wasn't updated");

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(task2Json, response.body(), "task wasn't updated");

        /*
        Получение списка задач
         */
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(task3Json))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "task wasn't added");

        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        task3.setId(2);
        List<Task> tasksList = List.of(task2, task3);
        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());

        assertEquals(tasksList, tasks,
                "returned invalid tasks");
        /*
        Удаление задачи по ID
         */
        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "task wasn't deleted");
        /*
        Удаление всех задач
         */
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "task wasn't deleted");
    }

    @Test
    @DisplayName("epic handler test")
    public void testEpicHandler() throws IOException, InterruptedException {
        /*
        Добавление задачи
         */
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "epic wasn't added");
        /*
        Получение задачи по ID
         */
        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicFromJson = gson.fromJson(response.body(), Epic.class);
        epic1.setId(1);

        assertEquals(epicFromJson, epic1, "epic wasn't added");
        /*
        Обновление задачи
         */
        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epic2Json))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "epic wasn't updated");

        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response.body(), Epic.class);
        epic2.setSubtaskIds(new ArrayList<>());
        epic2.setStatus(Status.NEW);
        assertEquals(epic2, epic, "epic wasn't updated");

        /*
        Получение списка задач
         */
        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epic3Json))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "epic wasn't added");

        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        epic3.setId(2);
        List<Epic> epicsList = List.of(epic2, epic3);
        List<Task> epics = gson.fromJson(response.body(), new TypeToken<List<Epic>>(){}.getType());

        assertEquals(epicsList, epics,
                "returned invalid tasks");
        /*
        Удаление задачи по ID
         */
        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "epic wasn't deleted");
        /*
        Удаление всех задач
         */
        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "epic wasn't deleted");
    }

    @Test
    @DisplayName("subtask handler test")
    public void textSubtaskHandler() throws IOException, InterruptedException {
        /*
        добавил эпик
         */
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "epic wasn't added");
         /*
        Добавление задачи
         */
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "subtask wasn't added");
        /*
        Получение задачи по ID
         */
        url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskFromJson = gson.fromJson(response.body(), SubTask.class);
        subTask1.setId(2);

        assertEquals(subTaskFromJson, subTask1, "subtask wasn't added");
        /*
        Обновление задачи
         */
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask2Json))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "subtask wasn't updated");

        url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(subtask2Json, response.body(), "subtask wasn't updated");

        /*
        Получение списка задач
         */
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask3Json))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "subtask wasn't added");

        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        subTask3.setId(3);
        List<SubTask> subtasksList = List.of(subTask2, subTask3);
        List<SubTask> subtask = gson.fromJson(response.body(), new TypeToken<List<SubTask>>(){}.getType());

        assertEquals(subtasksList, subtask,
                "returned invalid subtasks");
        /*
        Удаление задачи по ID
         */
        url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "subtask wasn't deleted");
        /*
        Удаление всех задач
         */
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "subtask wasn't deleted");
    }

    @Test
    @DisplayName("history handler test")
    public void testHistoryHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(task3Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        subTask1.setEpicsId(3);
        subtask1Json = gson.toJson(subTask1);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        subTask3.setEpicsId(3);
        subtask3Json = gson.toJson(subTask3);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask3Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        subTask4.setEpicsId(3);
        subtask4Json = gson.toJson(subTask4);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask4Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/task/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/epic/?id=3");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/subtask/?id=4");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/subtask/?id=5");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/subtask/?id=6");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/history/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "wrong status code");

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> history = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(200, response.statusCode(), "wrong status code");
        assertEquals(6, history.size(), "incorrect number of tasks received");
    }

    @Test
    @DisplayName("prioritizedTask handler test")
    public void testPrioritizedTaskHandler() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(task1Json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(task3Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epic1Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        subTask1.setEpicsId(3);
        subtask1Json = gson.toJson(subTask1);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask1Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        subTask3.setEpicsId(3);
        subtask3Json = gson.toJson(subTask3);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask3Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        subTask4.setEpicsId(3);
        subtask4Json = gson.toJson(subTask4);
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtask4Json))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "wrong status code");

        url = URI.create("http://localhost:8080/tasks/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> priorTasks = gson.fromJson(response.body(), new TypeToken<List<Task>>(){}.getType());

        assertEquals(5, priorTasks.size(), "wrong number of tasks in priorTasks list");
    }
}