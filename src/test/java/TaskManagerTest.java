import interfaces.TaskManager;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;

    protected void setUp() {
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
}