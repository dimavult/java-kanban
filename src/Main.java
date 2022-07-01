import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();



        taskManager.addNewTask(new Task("Помыть полы", "Взять тряпку, ведро, мыть полы", "NEW"));
        taskManager.addNewTask(new Task("Почистить картошку", "Для пюре", "NEW"));
        taskManager.addNewEpic(new Epic("Переезд", "Переехать в другую страну", "NEW" ));
        taskManager.addNewSubTask(new SubTask("Собрать коробки", "Положить в коробки вещи", "NEW"),
                                              3);
        taskManager.addNewSubTask(new SubTask("Упаковать кота", "Кинуть его в переноску", "NEW"),
                                              3);
        taskManager.addNewEpic(new Epic("Гнаться за преступником", "Он убил члоевка, нужно догнать",
                "NEW"));
        taskManager.addNewSubTask(new SubTask("Стрелять", "Нужно его подстрелить", "NEW"), 6);
        taskManager.updateTask(1, new Task("Помыть полы", "Взять тряпку, ведро, мыть полы", "DONE"));
        taskManager.updateTask(2, new Task("Почистить картошку", "Для пюре", "IN_PROGRESS"));
        taskManager.updateSubTask(7, new SubTask("Стрелять", "Нужно его подстрелить", "DONE"));
        taskManager.updateSubTask(4, new SubTask("Собрать коробки", "Положить в коробки вещи", "IN_PROGRESS"));
        taskManager.getTasksList();
        taskManager.getEpicsList();
        taskManager.getSubTasksList();

        taskManager.removeTaskByIdentifier(1);
        taskManager.removeSubTaskByIdentifier(4);
        System.out.println("---------------------------");
        taskManager.getTasksList();
        taskManager.getEpicsList();
        taskManager.getSubTasksList();
        System.out.println("---------------------------");
        taskManager.getEpicByIdentifier(6);
    }
}
