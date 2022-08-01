import service.InMemoryTaskManager;
import service.Managers;
import task.Epic;
import task.SubTask;
import task.Task;
import task.Status;

public class Main {
    public static void main(String[] args) {
        Managers.getDefault().addNewEpic(new Epic("--", "--"));
        Managers.getDefault().addNewSubTask(new SubTask("--", "--", Status.NEW, 1));//Добавляем сабтаски по epicID
        Managers.getDefault().addNewSubTask(new SubTask("--", "--", Status.DONE, 1));
        Managers.getDefault().addNewEpic(new Epic("--", "--"));
        Managers.getDefault().addNewSubTask(new SubTask("--", "--", Status.DONE, 4));
        Managers.getDefault().addNewSubTask(new SubTask("--", "--", Status.DONE, 4));
        Managers.getDefault().addNewSubTask(new SubTask("--", "--", Status.NEW, 4));

        Managers.getDefault().getEpicById(1);
        Managers.getDefault().getEpicById(4);
        Managers.getDefault().getSubTaskById(2);
        Managers.getDefault().getSubTaskById(3);
        Managers.getDefault().getSubTaskById(5);
        Managers.getDefault().getSubTaskById(6);
        Managers.getDefault().getSubTaskById(7);
        Managers.getDefault().getEpicById(1);
        Managers.getDefault().removeSubTaskByIdentifier(5);
        Managers.getDefault().removeEpicByIdentifier(1);

        System.out.println(Managers.getDefault().getHistory());



        int a = 1;
        for (int i = 1; i <= 5000000; i++){
            Managers.getDefault().addNewEpic(new Epic("O", "P"));
            Managers.getDefault().getEpicById(a++);
        }

        final long startTime = System.nanoTime();
        final long endTime = System.nanoTime();

        System.out.println(endTime-startTime);
    }
}
