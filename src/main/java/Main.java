import interfaces.TaskManager;
import service.FileBackedTasksManager;
import service.InMemoryTaskManager;
import service.Managers;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.File;
import java.time.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTasksManager(new File("data.txt"));

        taskManager.addNewEpic(new Epic("--", "--"));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 30, 19, 3)));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 30, 22, 23)));
        taskManager.addNewEpic(new Epic("--", "--"));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.DONE, 4, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 31, 10, 0)));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.DONE, 4, Duration.ofHours(2), LocalDateTime.of(2022, Month.AUGUST, 31, 12, 0)));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.NEW, 4, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 31, 15, 0)));
        taskManager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 10, 10, 10, 0)));
        taskManager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 11, 10, 10, 0)));
        taskManager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 12, 10, 10, 0)));
        taskManager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 9, 10, 10, 0)));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1));
        taskManager.addNewTask(new Task("--", "--"));
//
////        taskManager.removeTaskByIdentifier(8);
//
//        taskManager.getEpicById(1);
//        taskManager.getEpicById(4);
//        taskManager.getSubTaskById(2);
//        taskManager.getSubTaskById(3);
//        taskManager.getSubTaskById(5);
//        taskManager.getSubTaskById(6);
//        taskManager.getSubTaskById(7);
//        taskManager.getTaskById(9);
//
//        taskManager.getPrioritizedTasks().forEach(System.out::println);
//
//        taskManager.removeAllEpics();
//        taskManager.removeTaskByIdentifier(9);
//        System.out.println(taskManager.getTasksList());
//        taskManager.addNewTask(new Task("1", "--"));
//        taskManager.addNewTask(new Task("2", "--"));
//        taskManager.addNewTask(new Task("3", "--"));
//        taskManager.addNewTask(new Task("4", "--"));
//        taskManager.addNewTask(new Task("5", "--"));
//        taskManager.addNewTask(new Task("6", "--"));
//        taskManager.addNewTask(new Task("7", "--"));
//        taskManager.addNewTask(new Task("8", "--"));
//        taskManager.addNewTask(new Task("9", "--"));
//        taskManager.addNewTask(new Task("10", "--"));
//        System.out.println("--------------------------");
//        taskManager.getTasksList().forEach(System.out::println);
//        taskManager.getTaskById(1);
//        taskManager.getTaskById(2);
//        taskManager.getTaskById(3);
//        taskManager.getTaskById(4);
//        taskManager.getTaskById(5);
//        taskManager.getTaskById(6);
//        taskManager.getTaskById(7);
//        taskManager.getTaskById(8);
//        taskManager.getTaskById(9);
//        taskManager.getTaskById(10);

        taskManager.removeAllTasks();
        taskManager.getPrioritizedTasks().forEach(System.out::println);

        System.out.println(taskManager.getHistory());


//        FileBackedTasksManager managerNew = FileBackedTasksManager.loadFromFile(new File("data.txt"));
//
//        System.out.println(managerNew.getHistory());
//        managerNew.getTasksList().forEach(System.out::println);
//        managerNew.getEpicsList().forEach(System.out::println);
//        managerNew.getSubTasksList().forEach(System.out::println);
//        System.out.println(managerNew.getEpicById(4));
//        System.out.println(managerNew.getHistory());
//        System.out.println("---------------------------------");
//        managerNew.getPrioritizedTasks().forEach(System.out::println);
    }
}
