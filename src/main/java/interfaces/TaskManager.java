package interfaces;

import task.*;

import java.util.List;

public interface TaskManager{

    List<Task> getPrioritizedTasks();
    List<Task> getHistory();

    // МЕТОДЫ ПО ПОЛУЧЕНИЯ СПИСКА ЗАДАЧ ОПРЕДЕЛЕННОГО ТИПА

    List<Task> getAllTasksList();

    List<Task> getTasksList();

    List<Epic> getEpicsList();

    List<SubTask> getSubTasksList();

    // ПОЛУЧЕНИЕ ВСЕХ САБТАСКОВ НУЖНОГО ЭПИКА


    List<SubTask> getAllEpicsSubtasks(int epicId);

    // МЕТОДЫ ПО ДОБАВЛЕНИЮ ЗАДАЧ


    void addNewTask(Task task);


    void addNewEpic(Epic epic);


    void addNewSubTask(SubTask subTask);

    // МЕТОДЫ ПО УДАЛЕНИЮ ВСЕХ ЗАДАЧ


    void removeAllTasks();


    void removeAllEpics();


    void removeAllSubTasks();

    // МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ЗАДАЧЕ ПО ИДЕНТИФИКАТОРУ


    Task getTaskById(int id);

    Epic getEpicById(int id);


    SubTask getSubTaskById(int id);

    // МЕТОДЫ ДЛЯ УДАЛЕНИЯ ЗАДАЧ ПО ИДЕНТИФИКАТОРУ


    void removeTaskByIdentifier(int id);


    void removeEpicByIdentifier(int id);


    void removeSubTaskByIdentifier(int id);

    // МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ


    void updateTask(Task task);


    void updateSubTask(SubTask subTask);


    void updateEpic(Epic epic);

}
