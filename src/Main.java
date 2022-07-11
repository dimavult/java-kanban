import service.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.Status;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.addNewTask(new Task("Покушать", "Доесть рис", Status.NEW));
        taskManager.addNewTask(new Task("ПОчистить зубы", "Чистим зубы", Status.NEW));
        taskManager.addNewEpic(new Epic("Переезд", "Переехать в деревню"));
        taskManager.addNewSubTask(new SubTask("Собрать коробки", "Положить в коробки вещи", Status.NEW, 3));//Добавляем сабтаски по epicID
        taskManager.addNewSubTask(new SubTask("Упаковать кота", "Кинуть его в переноку", Status.DONE, 3));
        taskManager.addNewEpic(new Epic("Английский", "Выучить английский за 2 года"));
        taskManager.addNewSubTask(new SubTask("Задачники", "купить задачники по английскому", Status.DONE, 6));
        taskManager.addNewSubTask(new SubTask("Сериалы", "Посмотреть сериал 'Острые козырьки' в оригинале", Status.DONE, 6));
        taskManager.addNewSubTask(new SubTask("Книги", "Читать книги на английском", Status.NEW, 6));


        taskManager.updateTask(new Task("Покушать", "Доесть рис", Status.DONE, 1));
        taskManager.updateTask(new Task("ПОчистить зубы", "Чистим зубы", Status.IN_PROGRESS, 2));
        taskManager.updateSubTask(new SubTask("Собрать коробки", "Положить в коробки вещи", Status.IN_PROGRESS, 4, 3));
        taskManager.updateSubTask(new SubTask("Упаковать кота", "Кинуть его в переноку", Status.NEW, 5, 3));
        taskManager.updateSubTask(new SubTask("Задачники", "купить задачники по английскому", Status.NEW, 7, 6));
        taskManager.updateSubTask(new SubTask("Сериалы", "Посмотреть сериал 'Острые козырьки' в оригинале", Status.NEW, 8, 6));
        taskManager.updateSubTask(new SubTask("Книги", "Читать книги на английском", Status.DONE, 9, 6));

        taskManager.updateEpic(new Epic("Переезд", "Переехать в Африку", 3));

        System.out.println(taskManager.getTasksList());
        System.out.println("-----------------------");
        System.out.println(taskManager.getEpicsList());
        System.out.println("-----------------------");
        System.out.println(taskManager.getSubTasksList());
        System.out.println("-----------------------");
        System.out.println(taskManager.getAllEpicsSubtasks(3));
        System.out.println("-----------------------");
        System.out.println(taskManager.getSubTaskById(4));


        System.out.println(taskManager.getTasksList().hashCode());
        System.out.println(taskManager.getTasksList());

        taskManager.getTasksList().get(1).setName("блаблабла");
        System.out.println("-----------------------");
        System.out.println(taskManager.getTasksList());
//        // КОНТРАКТ EQUALS
//        System.out.println("-----------------------Рефлексивность");
//        //  1) Рефлексивность
//        System.out.println(taskManager.getEpicByIdentifier(3).equals(taskManager.getEpicByIdentifier(3)));
//        System.out.println("-----------------------Симметричность");
//
//        // 2) Симметричность
//        Epic epic = taskManager.getEpicByIdentifier(3);
//
//        System.out.println(taskManager.getEpicByIdentifier(3).equals(epic));
//        System.out.println(epic.equals(taskManager.getEpicByIdentifier(3)));
//        System.out.println("-----------------------Транзитивность");
//
//        // 3) Транзитивность
//
//        Epic epic1 = taskManager.getEpicByIdentifier(3);
//
//        System.out.println(taskManager.getEpicByIdentifier(3).equals(epic));
//        System.out.println(epic.equals(epic1));
//        System.out.println(taskManager.getEpicByIdentifier(3).equals(epic1));
//        System.out.println("-----------------------Согласованность");
//
//
//        // 4) Согласованность
//
//        System.out.println(taskManager.getEpicByIdentifier(3).equals(epic));
//        System.out.println(taskManager.getEpicByIdentifier(3).equals(epic));
//        System.out.println("-----------------------Проверка на null");
//
//        // 5) Проверка на null
//
//        System.out.println(taskManager.getEpicByIdentifier(3).equals(null));
//        System.out.println(epic.equals(null));
//        System.out.println("-----------------------");
//
//        // Контракты hashCode
//
//        // 1) Там нет названий, но это про то, что при вызове хэшкода несколько раз, значение не поменяется
//
//        System.out.println(taskManager.getTaskById(1).hashCode());
//        System.out.println(taskManager.getTaskById(1).hashCode());
//        System.out.println("-----------------------");
//
//        // 2) вызов метода hashCode над двумя объектами должен всегда возвращать одно и то же число,
//        // если эти объекты равны по equals
//
//        System.out.println(taskManager.getEpicByIdentifier(3).hashCode());
//        System.out.println(epic.hashCode());
//        System.out.println("-----------------------");
//
//        // 3) вызов метода hashCode над двумя неравными между собой объектами должен возвращать разные хэш-значения.
//
//        System.out.println(taskManager.getTaskById(1).equals(taskManager.getEpicByIdentifier(3)));
//        System.out.println(taskManager.getTaskById(1).hashCode());
//        System.out.println(taskManager.getEpicByIdentifier(3).hashCode());
    }
}
