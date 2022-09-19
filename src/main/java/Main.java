import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.HttpTaskServer;
import server.KVServer;
import task.Status;
import task.Task;

import java.io.IOException;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Task task = new Task("--", "--",  Status.NEW, 1, Duration.ofMinutes(30), LocalDateTime.of(3000, 10, 10, 10, 0));
//        Task task1 = new Task("--", "--", Status.NEW, 2, Duration.ofMinutes(30), LocalDateTime.of(3000, 11, 10, 10, 0));
//        Task task2 = new Task("--", "--", Status.NEW, 3,Duration.ofMinutes(30), LocalDateTime.of(3000, 12, 10, 10, 0));
//        Task task3 = new Task("--", "--", Status.NEW, 4,Duration.ofMinutes(30), LocalDateTime.of(3000, 9, 10, 10, 0));
//
//        new KVServer().start();
//        HttpTaskServer httpTaskServer = new HttpTaskServer();
//        httpTaskServer.startServer();

//        manager.addNewEpic(new Epic("--", "--"));
//        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 31, 10, 0)));
//        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1, Duration.ofHours(2), LocalDateTime.of(2022, Month.AUGUST, 31, 12, 0)));
//        manager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 31, 15, 0)));
//        manager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 10, 10, 10, 0)));
//        manager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 11, 10, 10, 0)));
//        manager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 12, 10, 10, 0)));
//        manager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 9, 10, 10, 0)));
//        manager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1));
//        manager.addNewTask(new Task("--", "--"));
//
//        manager.getEpicById(1);
//        manager.getSubTaskById(2);
//        manager.getSubTaskById(3);
//        manager.getSubTaskById(4);
//        manager.getTaskById(5);
//        manager.getTaskById(6);
//        manager.getTaskById(7);
//        manager.getTaskById(8);
//        manager.getSubTaskById(9);
//        manager.getTaskById(10);

//        HttpTaskServer httpTaskServer = new HttpTaskServer();
//        httpTaskServer.startServer();
//        File file = new File("data.txt");
//        TaskManager manager = new FileBackedTasksManager(file);
//
//        manager.addNewEpic(new Epic("--", "--"));
//        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 31, 10, 0)));
//        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1, Duration.ofHours(2), LocalDateTime.of(2022, Month.AUGUST, 31, 12, 0)));
//        manager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 31, 15, 0)));
//        manager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 10, 10, 10, 0)));
//        manager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 11, 10, 10, 0)));
//        manager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 12, 10, 10, 0)));
//        manager.addNewTask(new Task("--", "--", Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(3000, 9, 10, 10, 0)));
//        manager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1));
//        manager.addNewTask(new Task("--", "--"));
//
//        manager.getEpicById(1);
//        manager.getEpicById(4);
//        manager.getSubTaskById(2);
//        manager.getSubTaskById(3);
//        manager.getSubTaskById(5);
//        manager.getSubTaskById(6);
//        manager.getSubTaskById(7);
//        manager.getTaskById(9);

//        System.out.println(manager.getEpicById(1).hashCode());
//
//        manager.getPrioritizedTasks().forEach(System.out::println);
//
//        manager.removeAllEpics();
//        manager.removeTaskByIdentifier(9);
//        System.out.println(manager.getTasksList());
//        manager.addNewTask(new Task("1", "--"));
//        manager.addNewTask(new Task("2", "--"));
//        manager.addNewTask(new Task("3", "--"));
//        manager.addNewTask(new Task("4", "--"));
//        manager.addNewTask(new Task("5", "--"));
//        manager.addNewTask(new Task("6", "--"));
//        manager.addNewTask(new Task("7", "--"));
//        manager.addNewTask(new Task("8", "--"));
//        manager.addNewTask(new Task("9", "--"));
//        manager.addNewTask(new Task("10", "--"));
//        System.out.println("--------------------------");
//        manager.getTasksList().forEach(System.out::println);
//        manager.getTaskById(1);
//        manager.getTaskById(2);
//        manager.getTaskById(3);
//        manager.getTaskById(4);
//        manager.getTaskById(5);
//        manager.getTaskById(6);
//        manager.getTaskById(7);
//        manager.getTaskById(8);
//        manager.getTaskById(9);
//        manager.getTaskById(10);
//
//        manager.removeAllTasks();
//        manager.getPrioritizedTasks().forEach(System.out::println);
//
//        System.out.println(manager.getHistory());
//
//
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
