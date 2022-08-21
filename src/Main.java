import service.FileBackedTasksManager;
import task.Epic;
import task.SubTask;
import task.Status;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager managerOld = new FileBackedTasksManager(new File("data.txt"));

        managerOld.addNewEpic(new Epic("--", "--"));
        managerOld.addNewSubTask(new SubTask("--", "--", Status.NEW, 1));
        managerOld.addNewSubTask(new SubTask("--", "--", Status.DONE, 1));
        managerOld.addNewEpic(new Epic("--", "--"));
        managerOld.addNewSubTask(new SubTask("--", "--", Status.DONE, 4));
        managerOld.addNewSubTask(new SubTask("--", "--", Status.DONE, 4));
        managerOld.addNewSubTask(new SubTask("--", "--", Status.NEW, 4));

        System.out.println(managerOld.getEpicById(1));
        managerOld.getEpicById(4);
        managerOld.getSubTaskById(2);
        managerOld.getSubTaskById(3);
        managerOld.getSubTaskById(5);
        managerOld.getSubTaskById(6);
        managerOld.getSubTaskById(7);

        System.out.println(managerOld.getHistory());
        System.out.println(managerOld.getTasksList());
        System.out.println(managerOld.getEpicsList());
        System.out.println(managerOld.getSubTasksList());
        System.out.println(managerOld.getEpicById(1));
        System.out.println(managerOld.getHistory());
        System.out.println("--------------------------");

        FileBackedTasksManager managerNew = FileBackedTasksManager.loadFromFile(new File("data.txt"));

        System.out.println(managerNew.getHistory());
        System.out.println(managerNew.getTasksList());
        System.out.println(managerNew.getEpicsList());
        System.out.println(managerNew.getSubTasksList());
        System.out.println(managerNew.getEpicById(4));
        System.out.println(managerNew.getHistory());

    }
}
