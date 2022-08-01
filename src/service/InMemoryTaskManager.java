package service;

import interfaces.TaskManager;
import task.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int identifier = 1;

    private int getIdAndIncrement() {
        return identifier++;
    }

    @Override
    public List<Task> getHistory() {
        return Managers.getDefaultHistory().getHistory();
    }

    //                                      МЕТОДЫ ПО ПОЛУЧЕНИЯ СПИСКА ЗАДАЧ ОПРЕДЕЛЕННОГО ТИПА

    @Override
    public List<Task> getTasksList() {
        List<Task> taskList = new ArrayList<>();
        if (tasks.isEmpty()) {
            return null;
        } else {
            for (Integer id: tasks.keySet()){
                taskList.add(tasks.get(id));
            }
            return taskList;
        }
    }

    @Override
    public List<Epic> getEpicsList() {
        List<Epic> epicList = new ArrayList<>();
        if (epics.isEmpty()) {
            return null;
        } else {
            for (Integer id: epics.keySet()){
                epicList.add(epics.get(id));
            }
            return epicList;
        }
    }

    @Override
    public List<SubTask> getSubTasksList() {
        List<SubTask> subTaskList = new ArrayList<>();
        if (subTasks.isEmpty()) {
            return null;
        } else {
            for (Integer id: subTasks.keySet()){
                subTaskList.add(subTasks.get(id));
            }
            return subTaskList;
        }
    }


    //                                  ПОЛУЧЕНИЕ ВСЕХ САБТАСКОВ НУЖНОГО ЭПИКА

    @Override
    public List<SubTask> getAllEpicsSubtasks(int epicId) {
        List<SubTask> subTasksWithCurrentEpicId = new ArrayList<>();

        for (Integer integer : epics.get(epicId).getSubtaskIds()) {
            SubTask subTask = subTasks.get(integer);
            subTasksWithCurrentEpicId.add(subTask);
        }

        return subTasksWithCurrentEpicId;
    }

    //                                   МЕТОДЫ ПО ДОБАВЛЕНИЮ ЗАДАЧ

    @Override
    public int addNewTask(Task task) {
        task.setId(getIdAndIncrement());
        tasks.put(task.getId(), task);
        return identifier - 1; // вернул ID добавленной задачи
    }

    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(getIdAndIncrement());
        epics.put(epic.getId(), epic);
        return identifier - 1; // вернул ID добавленной задачи
    }

    @Override
    public int addNewSubTask(SubTask subTask) {
        int epicsId = subTask.getEpicsId();

        if (epics.get(epicsId) == null) {
            System.out.println("Эпика с указанным ID не существует.");
            return 0;
        } else {

            subTask.setId(identifier);
            epics.get(epicsId).addSubtaskId(identifier);
            subTasks.put(getIdAndIncrement(), subTask);

            updateEpicStatus(epicsId);
            return identifier - 1; // вернул ID добавленной задачи
        }
    }

    //                                      МЕТОДЫ ПО УДАЛЕНИЮ ВСЕХ ЗАДАЧ

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks() { // При удалении всех сабтасок, статусы всех эпиков сетятся на NEW
        HashSet<Integer> epicsIds = new HashSet<>();

        for (Integer integer : subTasks.keySet()) {
            epicsIds.add(subTasks.get(integer).getEpicsId());
        }

        subTasks.clear();

        for (Integer integer : epicsIds) {
            epics.get(integer).getSubtaskIds().clear();// почистил список сабтасок
            updateEpicStatus(integer);// обнвил его статус на NEW
        }

    }

    //                            МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ЗАДАЧЕ ПО ИДЕНТИФИКАТОРУ

    @Override
    public Task getTaskById(int id) {
        Managers.getDefaultHistory().addTask(tasks.get(id));
        return tasks.getOrDefault(id, null);
    }

    @Override
    public Epic getEpicById(int id) {
        Managers.getDefaultHistory().addTask(epics.get(id));
        return epics.getOrDefault(id, null);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        Managers.getDefaultHistory().addTask(subTasks.get(id));
        return subTasks.getOrDefault(id, null);
    }

    //                            МЕТОДЫ ДЛЯ УДАЛЕНИЯ ЗАДАЧ ПО ИДЕНТИФИКАТОРУ

    @Override
    public void removeTaskByIdentifier(int id) {
        if (tasks.containsKey(id)) {
            Managers.getDefaultHistory().removeTask(id);
            tasks.remove(id);
        } else {
            System.out.println("Задачи с таким ID нет в программе.");
        }
    }

    @Override
    public void removeEpicByIdentifier(int id) {
        if (epics.containsKey(id)) {
            for (Integer integer : epics.get(id).getSubtaskIds()) {
                Managers.getDefaultHistory().removeTask(integer);
                subTasks.remove(integer);
            }
            Managers.getDefaultHistory().removeTask(id);
            epics.remove(id);
        }
    }

    @Override
    public void removeSubTaskByIdentifier(int id) {

        if (subTasks.containsKey(id)) {
            int epicsId = subTasks.get(id).getEpicsId();

            epics.get(epicsId).removeSubTaskId(id);
            Managers.getDefaultHistory().removeTask(id);
            subTasks.remove(id);

            updateEpicStatus(epicsId);
        } else {
            System.out.println("Подзадачи с таким ID нет в программе.");
        }
    }

    //                              МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задачи с таким ID нет в программе.");
        }
    }

    @Override
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

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();

        if (epics.containsKey(id)) {
            epics.get(id).setName(epic.getName());
            epics.get(id).setDescription(epic.getDescription());
        } else {
            System.out.println("Эпика с таким ID нет в программе.");
        }
    }

    // Вспомогательные методы для обновления статуса эпика

    private void updateEpicStatus(int id) {
        Epic epic = epics.get(id);
        List<SubTask> subTasksList = getAllEpicsSubtasks(id);
        int doneCounter = 0;

        if (subTasksList.size() != 0) {
            for (SubTask subTask : subTasksList) {
                if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                } else if (subTask.getStatus().equals(Status.DONE)) {
                    doneCounter++;
                    if (doneCounter == subTasksList.size()) {
                        epic.setStatus(Status.DONE);
                    } else if (doneCounter > 0 && doneCounter < subTasksList.size()) {
                        epic.setStatus(Status.IN_PROGRESS);
                    } else {
                        epic.setStatus(Status.NEW);
                    }
                }
            }
        } else {
            epic.setStatus(Status.NEW);
        }
    }
}