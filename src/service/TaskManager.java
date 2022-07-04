package service;

import jdk.jshell.Snippet;
import task.Epic;
import constants.TasksStatus;
import task.SubTask;
import task.Task;

import java.util.*;

public class TaskManager { /*Поместил класс в отдельный пакет, изменил метод по сохранению Эпиков, изменил логику
                             обновления статуса эпика*/
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int identifier = 1;

    private int getIdAndIncrement() {
        return identifier++;
    }

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

        for(Integer integer: epics.get(epicId).getSubtaskIds()){
            SubTask subTask = subTasks.get(integer);
            subTasksWithCurrentEpicId.add(subTask);
        }
        
        return subTasksWithCurrentEpicId;
    }

    //                                   МЕТОДЫ ПО ДОБАВЛЕНИЮ ЗАДАЧ

    public void addNewTask(Task task) {
        task.setId(getIdAndIncrement());
        tasks.put(task.getId(), task);
    }

    public void addNewEpic(Epic epic) {
        epic.setId(getIdAndIncrement());
        epic.setStatus(TasksStatus.Status.NEW);
        epics.put(epic.getId(), epic);
    }

    public void addNewSubTask(SubTask subTask) {
        int epicsId = subTask.getEpicsId();

        if (epics.get(epicsId) == null) {
            System.out.println("Эпика с указанным ID не существует.");
            return;
        }

        subTask.setId(identifier);
        epics.get(epicsId).addSubtaskId(identifier);
        subTasks.put(getIdAndIncrement(), subTask);

        updateEpicStatus(epicsId);
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

        subTasks.clear();

        for (Integer integer : epicsIds) {
            epics.get(integer).getSubtaskIds().clear();// почистил список сабтасок
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
        for (Integer integer : epics.get(id).getSubtaskIds()) {
            subTasks.remove(integer);
        }

        epics.remove(id);
    }

    public void removeSubTaskByIdentifier(int id) {

        if (subTasks.containsKey(id)) {
            int epicsId = subTasks.get(id).getEpicsId();
            subTasks.remove(id);
            epics.get(epicsId).removeSubTaskId(id);
            updateEpicStatus(epicsId);
        } else {
            System.out.println("Подзадачи с таким ID нет в программе.");
        }
    }

    //                              МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задачи с таким ID нет в программе.");
        }
    }

    public void updateSubTask(SubTask subTask) {
        int subTaskId = subTask.getId();
        int epicsId = subTask.getEpicsId();

        if (subTasks.containsKey(subTaskId)) {
            if (epics.containsKey(epicsId)) {
                subTasks.put(subTaskId, subTask);// обновляем данные сабтаска
                updateEpicStatus(epicsId);// обновляем данные эпика
            }
        } else {
            System.out.println("Подзадачи с таким ID нет в программе.");
        }
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();

        if (epics.containsKey(id)) {
            for (Integer integer: epics.get(id).getSubtaskIds()){/*Насколько я понимаю, пользователь не может передать
                                                                   передать список сабтасок метода, потому их надо
                                                                   сохранить отдельно*/
                epic.addSubtaskId(integer);
            }
            epics.put(id, epic);
            updateEpicStatus(id);
        } else {
            System.out.println("Эпика с таким ID нет в программе.");
        }
    }

    // Вспомогательные методы для обновления статуса эпика

    private void updateEpicStatus(int id) {
        Epic epic = epics.get(id);
        ArrayList<SubTask> subTasksList = getAllEpicsSubtasks(id);
        int doneCounter = 0;

        if (subTasksList.size() != 0) {
            for (SubTask subTask : subTasksList) {
                if (subTask.getStatus().equals(TasksStatus.Status.IN_PROGRESS)) {
                    epic.setStatus(TasksStatus.Status.IN_PROGRESS);
                    return;
                } else if (subTask.getStatus().equals(TasksStatus.Status.DONE)) {
                    doneCounter++;
                    if (doneCounter == subTasksList.size()) {
                        epic.setStatus(TasksStatus.Status.DONE);
                    } else if (doneCounter > 0 && doneCounter < subTasksList.size()) {
                        epic.setStatus(TasksStatus.Status.IN_PROGRESS);
                    } else {
                        epic.setStatus(TasksStatus.Status.NEW);
                    }
                }
            }
        } else {
            epic.setStatus(TasksStatus.Status.NEW);
        }
    }
}
