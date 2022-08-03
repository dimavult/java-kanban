import interfaces.TaskManager;
import service.Managers;
import task.Epic;
import task.SubTask;
import task.Status;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        manager.addNewEpic(new Epic("--", "--"));
        manager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1));//Добавляем сабтаски по epicID
        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1));
        manager.addNewEpic(new Epic("--", "--"));
        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 4));
        manager.addNewSubTask(new SubTask("--", "--", Status.DONE, 4));
        manager.addNewSubTask(new SubTask("--", "--", Status.NEW, 4));

        manager.getEpicById(1);
        manager.getEpicById(4);
        manager.getSubTaskById(2);
        manager.getSubTaskById(3);
        manager.getSubTaskById(5);
        manager.getSubTaskById(6);
        manager.getSubTaskById(7);
        manager.getEpicById(1);
        manager.removeSubTaskByIdentifier(5);
        manager.removeEpicByIdentifier(1);

        System.out.println(manager.getHistory());

    }
}
