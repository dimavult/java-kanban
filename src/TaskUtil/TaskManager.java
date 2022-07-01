package TaskUtil;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.*;

public class TaskManager { /*Поместил класс в отдельный пакет, изменил метод по сохранению Эпиков, изменил логику
                             обновления статуса эпика*/
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

    public HashMap<Integer, Task> getTasksList() {
        if (tasks.isEmpty()) {
            return null;
        } else {
            return tasks;
        }
    }

    public HashMap<Integer, Epic> getEpicsList() {
        if (epics.isEmpty()) {
            return null;
        } else {
            return epics;
        }
    }

    public HashMap<Integer, SubTask> getSubTasksList() {
        if (subTasks.isEmpty()) {
            return null;
        } else {
            return subTasks;
        }
    }

    //                                  ПОЛУЧЕНИЕ ВСЕХ САБТАСКОВ НУЖНОГО ЭПИКА

    public ArrayList<SubTask> getAllEpicsSubtasks(int epicId) {
        ArrayList<SubTask> subTasksWithCurrentEpicId = new ArrayList<>();
        for (Integer integer : subTasks.keySet()) {
            if (subTasks.get(integer).getEpicsId() == epicId) {
                subTasksWithCurrentEpicId.add(subTasks.get(integer));
            }
        }
        return subTasksWithCurrentEpicId;
    }

    //                                   МЕТОДЫ ПО ДОБАВЛЕНИЮ ЗАДАЧ

    public void addNewTask(Task task) {
        task.setId(counter++);
        tasks.put(task.getId(), task);
    }

    public void addNewEpic(Epic epic) {
        epic.setId(counter++);
        epic.setStatus(Task.Status.NEW);
        epics.put(epic.getId(), epic);
    }

    public void addNewSubTask(SubTask subTask, int epicId) {
        if (epics.containsKey(epicId)) {
            subTask.setEpicsId(epicId);
            subTask.setId(counter);
            subTasks.put(counter++, subTask);
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
            updateEpicStatus(integer);// потому просто обновлю его статус на NEW
        }

    }

    //                            МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ЗАДАЧЕ ПО ИДЕНТИФИКАТОРУ

    public Task getTaskById(int id) {
        return tasks.getOrDefault(id, null); // Идея сама предложила на это поменять.
    }

    public Epic getEpicByIdentifier(int id) {
        return epics.getOrDefault(id, null);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.getOrDefault(id, null);
    }

    //                            МЕТОДЫ ДЛЯ УДАЛЕНИЯ ЗАДАЧ ПО ИДЕНТИФИКАТОРУ

    public void removeTaskByIdentifier(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задачи с таким ID нет в программе.");
        }
    }

    public void removeEpicByIdentifier(int id) {  /*Метод вместе с эпиком удаляет сабтаски, у которых Id эпика - это id
                                                    удаляемого эпика. Посчитал нужным сделать так, потому что как мне
                                                    кажется, не может быть сабтасков без эпика.*/
        if (epics.containsKey(id)) {
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
        } else {
            System.out.println("Эпика с таким ID нет в программе.");
        }
    }

    public void removeSubTaskByIdentifier(int id) {
        if (subTasks.containsKey(id)) {
            int epicsId = subTasks.get(id).getEpicsId();
            subTasks.remove(id);
            updateEpicStatus(epicsId);
        } else {
            System.out.println("Подзадачи с таким ID нет в программе.");
        }
    }

    //                              МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ

    public void updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        } else {
            System.out.println("Задачи с таким ID нет в программе.");
        }
    }

    public void updateSubTask(int subTaskId, SubTask subTask) {
        if (subTasks.containsKey(subTaskId)) {
            int epicsId = subTasks.get(subTaskId).getEpicsId();
            subTask.setEpicsId(epicsId);// сохраянем id эпика
            subTask.setId(subTasks.get(subTaskId).getId()); // сохраняем сам id
            subTasks.put(subTaskId, subTask);// обновляем данные сабтаска
            updateEpicStatus(epicsId);// обновляем данные эпика
        } else {
            System.out.println("Подзадачи с таким ID нет в программе.");
        }
    }

    public void updateEpic(int id, Epic epic) {
        if (epics.containsKey(id)) {
            epic.setStatus(epics.get(id).getStatus());
            epic.setId(id);
            epics.put(id, epic);
        } else {
            System.out.println("Эпика с таким ID нет в программе.");
        }
    }

    // Вспомогательные методы для обновления статуса эпика

    private void updateEpicStatus(int epicId) { /*Разделил метод по обновлению статуса эпика на два, потому что один
                                                  метод отвечает за поиск изменения в статусе, а второй за создание
                                                  нового объекта класса с новыми данными по статусу.*/
        String epicsName = epics.get(epicId).getName();
        String epicsDescription = epics.get(epicId).getDescription();

        epics.put(epicId, new Epic(epicsName, epicsDescription, getUpdatedEpicsStatus(epicId)));
        epics.get(epicId).setId(epicId);
    }

    private Task.Status getUpdatedEpicsStatus(int epicId) {
        Task.Status epicsStatus;
        Set<Task.Status> subTasksStatuses = new HashSet<>();/*узнал, что в этой коллекции хранятся уникальные значения и
                                                              решил использовать.*/

        for (Integer subId : subTasks.keySet()) {
            if (subTasks.get(subId).getEpicsId() == epicId) {
                subTasksStatuses.add(subTasks.get(subId).getStatus());
            }
        }

        if (subTasksStatuses.isEmpty()) {
            epicsStatus = Task.Status.NEW;
        } else if (subTasksStatuses.contains(Task.Status.IN_PROGRESS)) {
            epicsStatus = Task.Status.IN_PROGRESS;
        } else if (subTasksStatuses.size() == 1) {
            if (subTasksStatuses.contains(Task.Status.NEW)) {
                epicsStatus = Task.Status.NEW;
            } else {
                epicsStatus = Task.Status.DONE;
            }
        } else {
            epicsStatus = Task.Status.IN_PROGRESS;
        }
        return epicsStatus;
    }
}
