package interfaces;

import task.*;

import java.util.*;

public interface TaskManager {

    //                                      Счетчик ID задачи

    int getIdAndIncrement();

    //                                      МЕТОДЫ ПО ПОЛУЧЕНИЯ СПИСКА ЗАДАЧ ОПРЕДЕЛЕННОГО ТИПА


    HashMap<Integer, Task> getTasksList();


    HashMap<Integer, Epic> getEpicsList();


    HashMap<Integer, SubTask> getSubTasksList();

    //                                  ПОЛУЧЕНИЕ ВСЕХ САБТАСКОВ НУЖНОГО ЭПИКА


    ArrayList<SubTask> getAllEpicsSubtasks(int epicId);

    //                                   МЕТОДЫ ПО ДОБАВЛЕНИЮ ЗАДАЧ


    void addNewTask(Task task);


    void addNewEpic(Epic epic);


    void addNewSubTask(SubTask subTask);

    //                                      МЕТОДЫ ПО УДАЛЕНИЮ ВСЕХ ЗАДАЧ


    void removeAllTasks();


    void removeAllEpics();


    void removeAllSubTasks();

    //                            МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ЗАДАЧЕ ПО ИДЕНТИФИКАТОРУ


    Task getTaskById(int id);

    Epic getEpicByIdentifier(int id);


    SubTask getSubTaskById(int id);

    //                            МЕТОДЫ ДЛЯ УДАЛЕНИЯ ЗАДАЧ ПО ИДЕНТИФИКАТОРУ


    void removeTaskByIdentifier(int id);


    void removeEpicByIdentifier(int id);


    void removeSubTaskByIdentifier(int id);

    //                              МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ


    void updateTask(Task task);


    void updateSubTask(SubTask subTask);


    void updateEpic(Epic epic);

    // Вспомогательные методы для обновления статуса эпика

    void updateEpicStatus(int id);

}
