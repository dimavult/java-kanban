import service.InMemoryTaskManager;
import service.Managers;
import task.Epic;
import task.SubTask;
import task.Task;
import task.Status;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Managers.getDefault().addNewTask(new Task("Покушать", "Доесть рис", Status.NEW));
        Managers.getDefault().addNewTask(new Task("ПОчистить зубы", "Чистим зубы", Status.NEW));
        Managers.getDefault().addNewEpic(new Epic("Переезд", "Переехать в деревню"));
        Managers.getDefault().addNewSubTask(new SubTask("Собрать коробки", "Положить в коробки вещи", Status.NEW, 3));//Добавляем сабтаски по epicID
        Managers.getDefault().addNewSubTask(new SubTask("Упаковать кота", "Кинуть его в переноку", Status.DONE, 3));
        Managers.getDefault().addNewEpic(new Epic("Английский", "Выучить английский за 2 года"));
        Managers.getDefault().addNewSubTask(new SubTask("Задачники", "купить задачники по английскому", Status.DONE, 6));
        Managers.getDefault().addNewSubTask(new SubTask("Сериалы", "Посмотреть сериал 'Острые козырьки' в оригинале", Status.DONE, 6));
        Managers.getDefault().addNewSubTask(new SubTask("Книги", "Читать книги на английском", Status.NEW, 6));


        Managers.getDefault().updateTask(new Task("Покушать", "Доесть рис", Status.DONE, 1));
        Managers.getDefault().updateTask(new Task("ПОчистить зубы", "Чистим зубы", Status.IN_PROGRESS, 2));
        Managers.getDefault().updateSubTask(new SubTask("Собрать коробки", "Положить в коробки вещи", Status.IN_PROGRESS, 4, 3));
        Managers.getDefault().updateSubTask(new SubTask("Упаковать кота", "Кинуть его в переноку", Status.NEW, 5, 3));
        Managers.getDefault().updateSubTask(new SubTask("Задачники", "купить задачники по английскому", Status.NEW, 7, 6));
        Managers.getDefault().updateSubTask(new SubTask("Сериалы", "Посмотреть сериал 'Острые козырьки' в оригинале", Status.NEW, 8, 6));
        Managers.getDefault().updateSubTask(new SubTask("Книги", "Читать книги на английском", Status.DONE, 9, 6));

        Managers.getDefault().updateEpic(new Epic("Переезд", "Переехать в Африку", 3));

        System.out.println(Managers.getDefault().getTasksList());
        System.out.println("-----------------------");
        System.out.println(Managers.getDefault().getEpicsList());
        System.out.println("-----------------------");
        System.out.println(Managers.getDefault().getSubTasksList());
        System.out.println("-----------------------");
        System.out.println(Managers.getDefault().getAllEpicsSubtasks(3));
        System.out.println("-----------------------");


        System.out.println(Managers.getDefault().getTasksList().hashCode());
        System.out.println(Managers.getDefault().getTasksList());

        Managers.getDefault().getTasksList().get(1).setName("блаблабла");
        System.out.println("-----------------------");
        System.out.println(Managers.getDefault().getTasksList());
        System.out.println("-----------------------");
        Managers.getDefault().getSubTaskById(4);
        Managers.getDefault().getSubTaskById(7);
        Managers.getDefault().getSubTaskById(8);
        Managers.getDefault().getTaskById(1);
        Managers.getDefault().getTaskById(2);
        Managers.getDefault().getTaskById(1);
        Managers.getDefault().getTaskById(2);
        Managers.getDefault().getSubTaskById(4);
        Managers.getDefault().getSubTaskById(7);
        Managers.getDefault().getSubTaskById(8);
        Managers.getDefault().getTaskById(1);
        Managers.getDefault().getTaskById(2);
        Managers.getDefault().getTaskById(1);
        Managers.getDefault().getTaskById(2);
        System.out.println(Managers.getDefaultHistory().getHistory());
        System.out.println("-----------------------");
        System.out.println(Managers.getDefault().getHistory());

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
