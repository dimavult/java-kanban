package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.KVServer;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest { // сделал отдельный класс, потому что иначе проверки шли криво, сил уже нет :(

    HTTPTaskManager taskManager;
    KVServer kvServer;

    @BeforeEach
    public void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = new HTTPTaskManager(URI.create("http://localhost:8078/"));
        taskManager.addNewEpic(new Epic("epic1", "--"));
        taskManager.addNewEpic(new Epic("epic2", "--"));
        taskManager.addNewSubTask(new SubTask("subtask3",
                "--",
                Status.NEW,
                1,
                Duration.ofMinutes(30),
                LocalDateTime.of(3000, 1, 12, 12, 0)));
        taskManager.addNewSubTask(new SubTask("subtask4",
                "--",
                Status.DONE,
                1,
                Duration.ofMinutes(30),
                LocalDateTime.of(3000, 2, 12, 12, 0)));
        taskManager.addNewSubTask(new SubTask("subtask5",
                "--",
                Status.NEW,
                2,
                Duration.ofMinutes(30),
                LocalDateTime.of(3000, 3, 12, 12, 0)));
        taskManager.addNewSubTask(new SubTask("subtask6",
                "--",
                Status.IN_PROGRESS,
                2,
                Duration.ofMinutes(30),
                LocalDateTime.of(3000, 4, 12, 12, 0)));
        taskManager.addNewSubTask(new SubTask("subtask7",
                "--",
                Status.DONE,
                2,
                Duration.ofMinutes(30),
                LocalDateTime.of(3000, 5, 12, 12, 0)));
        taskManager.addNewTask(new Task("task8",
                "--",
                Status.NEW,
                Duration.ofMinutes(30),
                LocalDateTime.of(3000, 6, 12, 12, 0)));
        taskManager.addNewTask(new Task("task9",
                "--",
                Status.NEW,
                Duration.ofMinutes(30),
                LocalDateTime.of(3000, 7, 12, 12, 0)));
        taskManager.addNewTask(new Task("task10",
                "--",
                Status.NEW,
                Duration.ofMinutes(30),
                LocalDateTime.of(3000, 8, 12, 12, 0)));
        taskManager.addNewTask(new Task("task11", "--", Status.DONE));
        taskManager.getEpicById(1);
        taskManager.getEpicById(2);
        taskManager.getSubTaskById(7);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(3);
        taskManager.getTaskById(9);
        taskManager.getTaskById(8);
        taskManager.getTaskById(11);
        taskManager.getTaskById(10);
    }

    @AfterEach
    public void stop() {
        kvServer.stop();
    }

    @Test
    @DisplayName("test load method")
    void load() {
        taskManager.addNewEpic(new Epic("epic3", "epic3"));

        assertEquals(11, taskManager.getHistory().size(), "incorrect size of history");
        assertEquals(3, taskManager.getEpicsList().size(), "epic without subtasks wasn't added");
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager = new HTTPTaskManager(URI.create("http://localhost:8078/"));

        assertNull(taskManager.getTasksList(), "tasks list is not empty");
        assertNull(taskManager.getEpicsList(), "epics list is not empty");
        assertNull(taskManager.getSubTasksList(), "subtasks list is not empty");
        assertEquals(0, taskManager.getHistory().size(), "history should be empty");
    }

    @Test
    @DisplayName("test save method")
    void save() {
        Task taskTest = new Task("taskTest", "taskTest", Status.IN_PROGRESS);
        Epic epicTest = new Epic("kekw", "kekl");
        SubTask subTask = new SubTask("subTest", "subTest", Status.NEW, 13);
        taskManager.addNewTask(taskTest);
        taskManager.addNewEpic(epicTest);
        taskManager.addNewSubTask(subTask);

        assertEquals(taskTest, taskManager.getTaskById(12), "task wasn't added");
        assertEquals(epicTest.getName(), taskManager.getEpicById(13).getName(), "epic wasn't added");
        assertEquals(epicTest.getId(), taskManager.getEpicById(13).getId(), "epic wasn't added");
        assertEquals(epicTest.getDescription(), taskManager.getEpicById(13).getDescription(), "epic wasn't added");
        assertEquals(subTask, taskManager.getSubTaskById(14), "subtask wasn't added");
    }
}