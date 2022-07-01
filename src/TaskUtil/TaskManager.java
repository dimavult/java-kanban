import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.*;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    int counter = 1;

    //                                      МЕТОДЫ ПО ПОЛУЧЕНИЯ СПИСКА ЗАДАЧ ОПРЕДЕЛЕННОГО ТИПА

    public HashMap getTasksList() {
        for (Integer integer : tasks.keySet()) {
            System.out.println(tasks.get(integer).getTaskType() + " ID - " + integer);
            System.out.println(tasks.get(integer).toString());
        }
    }

    public void getEpicsList() {
        for (Integer integer : epics.keySet()) {
            System.out.println(epics.get(integer).getTaskType() + " ID - " + integer);
            System.out.println(epics.get(integer).toString());
        }
    }

    public void getSubTasksList() {
        for (Integer integer : subTasks.keySet()) {
            System.out.println(subTasks.get(integer).getTaskType() + " ID - " + integer);
            System.out.println(subTasks.get(integer).toString());
        }
    }

    //                                   МЕТОДЫ ПО ДОБАВЛЕНИЮ ЗАДАЧ

    public void addNewTask(Task task) {
        task.setId(counter++);
        tasks.put(task.getId(), task);
    }

    public void addNewEpic(Epic epic) {
        epic.setId(counter++);
        epics.put(epic.getId(), epic);
    }

    public void addNewSubTask(SubTask subTask, int epicId) {
        if (epics.containsKey(epicId)) {
            subTask.setEpicsId(epicId);
            subTask.setId(counter);
            subTasks.put(counter++, subTask);
            epics.get(epicId).setSubTaskNumbers(subTask.getId());
        }
    }


    //                                      МЕТОДЫ ПО УДАЛЕНИЮ ВСЕХ ЗАДАЧ

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void removeAllSubTasks() { // не знаю, что должно происходить с эпиком при удалении всех подзадач
        Set<Integer> epicsIds = new HashSet<>();
        for (Integer integer : subTasks.keySet()) {
            epicsIds.add(subTasks.get(integer).getEpicsId());
        }
        for (Integer integer : epicsIds) {
            epics.get(integer).setStatus("NEW");
        }
    }

    //                            МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ЗАДАЧЕ ПО ИДЕНТИФИКАТОРУ

    public String getTaskById(int id) {
        return tasks.get(id).toString();
    }

    public String getEpicByIdentifier(int id) {
        return epics.get(id).toString();
    }

    public String getSubTaskById(int id) {
        return subTasks.get(id).toString();
    }

    //                            МЕТОДЫ ДЛЯ УДАЛЕНИЯ ЗАДАЧ ПО ИДЕНТИФИКАТОРУ

    public void removeTaskByIdentifier(int id) {
        tasks.remove(id);
    }

    public void removeEpicByIdentifier(int id) {  /*Метод вместе с эпиком удаляет сабтаски, у которых Id эпика - это id
                                                    удаляемого эпика. Посчитал нужным сделать так, потому что как мне
                                                    кажется, не может быть сабтасков без эпика.*/
        ArrayList<Integer> subTasksIds = new ArrayList<>();
        for (Integer integer : subTasks.keySet()) {
            if (subTasks.get(integer).getEpicsId() == id) {
                subTasksIds.add(integer);
            }
        }
        for (Integer integer : subTasksIds) {
            subTasks.remove(integer);
        }
        epics.remove(id);
    }

    public void removeSubTaskByIdentifier(int id) {
        int epicsId = subTasks.get(id).getEpicsId();
        subTasks.remove(id);
        checkEpicStatusWhenRemoveSubTaskByIdentifier();
    }

    //                              МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ

    public void updateTask(int id, Task task) {
        tasks.put(id, task);
    }

    public void updateSubTask(int subTaskId, SubTask subTask) {
        int epicsId = subTasks.get(subTaskId).getEpicsId(); // сохраянем id эпика
        int id = subTasks.get(subTaskId).getId(); // сохраняем сам id
        subTasks.put(subTaskId, subTask);// обновляем данные сабтаска
        subTasks.get(subTaskId).setEpicsId(epicsId);// возвращаем привязку к эпику
        subTasks.get(subTaskId).setId(id);// возвращаем коррктный id
        checkEpicStatusIsUpdated(subTaskId);// проверяем, как поменялся статус эпика
    }

    private void checkEpicStatusIsUpdated(int subTaskId) {
        int epicsId = subTasks.get(subTaskId).getEpicsId(); /*создал перменную просто чтобы каждый раз не писать
                                                                subTasks.get(subTaskId).getEpicsId()*/
        int newCounter = 0;//в этих переменных считаем количество статусов new, done и in_progress
        int inProgressCounter = 0;
        int doneCounter = 0;
        int subTasksCounter = 0;// тут считаем, сколько всего у нас сабкасков относятся к нужному эпику

        for (Integer integer : subTasks.keySet()) {
            if (subTasks.get(integer).getEpicsId() == subTasks.get(subTaskId).getEpicsId()) {
                if (subTasks.get(integer).getStatus().equals("NEW")) {
                    newCounter++;
                } else if (subTasks.get(integer).getStatus().equals("IN_PROGRESS")) {
                    inProgressCounter++;
                } else if (subTasks.get(integer).getStatus().equals("DONE")) {
                    doneCounter++;
                } else {
                    System.out.println("Неверный статус задачи.");
                }
                subTasksCounter++;
            }
        }
        updateEpicStatus(epicsId, newCounter, inProgressCounter, doneCounter, subTasksCounter);
    }

    //                      ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ ЭПИКОВ ВСВЯЗИ В ОБНОВЛЕНИЕ ПОДЗАДАЧ

    private void updateEpicStatus(int epicsId,
                                  int newCounter,
                                  int inProgressCounter,
                                  int doneCounter,
                                  int subTasksCounter) {
        if (subTasksCounter == newCounter) {
            epics.put(epicsId, new Epic(epics.get(epicsId).getName(),
                    epics.get(epicsId).getDescription(), "NEW"));
        } else if (subTasksCounter == doneCounter) {
            epics.put(epicsId, new Epic(epics.get(epicsId).getName(),
                    epics.get(epicsId).getDescription(), "DONE"));
        } else if (inProgressCounter > 0) {
            epics.put(epicsId, new Epic(epics.get(epicsId).getName(),
                    epics.get(epicsId).getDescription(), "IN_PROGRESS"));
        }
    }

    public void checkEpicStatusWhenRemoveSubTaskByIdentifier() { /*пахнет костылём, наверное, можно переделать метод
                                                                   со 144 строки, но у меня что-то не получилось*/
        for (Integer integer: subTasks.keySet()){
            checkEpicStatusIsUpdated(subTasks.get(integer).getId());
        }
    }
}