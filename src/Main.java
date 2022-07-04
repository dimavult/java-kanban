import service.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import constants.TasksStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.addNewTask(new Task("Покушать", "Доесть рис", TasksStatus.Status.NEW));
        taskManager.addNewTask(new Task("ПОчистить зубы", "Чистим зубы", TasksStatus.Status.NEW));
        taskManager.addNewEpic(new Epic("Переезд", "Переехать в деревню"));
        taskManager.addNewSubTask(new SubTask("Собрать коробки", "Положить в коробки вещи", TasksStatus.Status.NEW, 3));//Добавляем сабтаски по epicID
        taskManager.addNewSubTask(new SubTask("Упаковать кота", "Кинуть его в переноку", TasksStatus.Status.DONE, 3));
        taskManager.addNewEpic(new Epic("Английский", "Выучить английский за 2 года"));
        taskManager.addNewSubTask(new SubTask("Задачники", "купить задачники по английскому", TasksStatus.Status.DONE, 6));
        taskManager.addNewSubTask(new SubTask("Сериалы", "Посмотреть сериал 'Острые козырьки' в оригинале", TasksStatus.Status.DONE, 6));
        taskManager.addNewSubTask(new SubTask("Книги", "Читать книги на английском", TasksStatus.Status.NEW, 6));


        taskManager.updateTask(new Task("Покушать", "Доесть рис", TasksStatus.Status.DONE, 1));
        taskManager.updateTask(new Task("ПОчистить зубы", "Чистим зубы", TasksStatus.Status.IN_PROGRESS, 2));
        taskManager.updateSubTask(new SubTask("Собрать коробки", "Положить в коробки вещи", TasksStatus.Status.IN_PROGRESS, 4, 3));
        taskManager.updateSubTask(new SubTask("Упаковать кота", "Кинуть его в переноку", TasksStatus.Status.NEW, 5, 3));
        taskManager.updateSubTask(new SubTask("Задачники", "купить задачники по английскому", TasksStatus.Status.NEW, 7, 6));
        taskManager.updateSubTask(new SubTask("Сериалы", "Посмотреть сериал 'Острые козырьки' в оригинале", TasksStatus.Status.NEW, 8, 6));
        taskManager.updateSubTask(new SubTask("Книги", "Читать книги на английском", TasksStatus.Status.DONE, 9, 6));

        taskManager.updateEpic(new Epic("Переезд", "Переехать в Африку", 3));

        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getEpicsList());
        System.out.println(taskManager.getSubTasksList());



    }
}
