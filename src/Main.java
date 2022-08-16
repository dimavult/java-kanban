import interfaces.TaskManager;
import service.FileBackedTasksManager;
import service.InMemoryTaskManager;
import service.Managers;
import task.Epic;
import task.SubTask;
import task.Task;
import task.Status;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(new File("data.txt"));

        manager.addNewEpic(new Epic("--", "--"));
        manager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1));
        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1));
        manager.addNewEpic(new Epic("--", "--"));
        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 4));
        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 4));
        manager.addNewSubTask(new SubTask("--", "--", Status.NEW, 4));

//        manager.getEpicById(1);
//        manager.getEpicById(4);
//        manager.getSubTaskById(2);
//        manager.getSubTaskById(3);
//        manager.getSubTaskById(5);
//        manager.getSubTaskById(6);
//        manager.getSubTaskById(7);

//        manager.removeEpicByIdentifier(4);

//        System.out.println(manager.getHistory());
//        System.out.println(manager.getEpicById(1));
//        System.out.println(manager.getHistory());
    }
}
