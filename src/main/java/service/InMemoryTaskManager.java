package service;

import com.sun.source.tree.IfTree;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager history = Managers.getDefaultHistory();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
        if (task1.getStartTime() == null && task2.getStartTime() == null) {
            return -1;
        } else if (task1.getStartTime() == null) {
            return 1;
        } else if (task2.getStartTime() == null) {
            return -1;
        } else {
            return task1.getStartTime().compareTo(task2.getStartTime());
        }
    });

    private int identifier = 1;

    protected HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    protected HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    protected HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    private int getIdAndIncrement() {
        return identifier++;
    }

    @Override
    public List<Task> getHistory() {
        return history.getHistory();
    }

    private boolean getIntersectionTestResult(Task inputTask) {
        boolean isNotIntersection = true;
        if (inputTask.getDuration() != null && inputTask.getStartTime() != null) {
            for (Task taskFromSet : prioritizedTasks) {
                if (taskFromSet.getStartTime() != null && taskFromSet.getDuration() != null) {
                    isNotIntersection = hasNoIntersection(inputTask, taskFromSet);
                }
            }
        }
        return isNotIntersection;
    }

    private boolean hasNoIntersection(Task inputTask, Task taskFromSet) {
        LocalDateTime anotherStartTime = taskFromSet.getStartTime();
        LocalDateTime anotherEndTime = taskFromSet.getEndTime();
        LocalDateTime inputStartTime = inputTask.getStartTime();
        LocalDateTime inputEndTime = inputTask.getEndTime();

        boolean check1 = anotherStartTime.isAfter(inputEndTime) &&
                anotherEndTime.isAfter(inputEndTime); // задача левее на временной линии
        boolean check2 = anotherEndTime.isBefore(inputStartTime) &&
                anotherStartTime.isBefore(inputStartTime); // задача правее на временной линии
        return check1 || check2;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // МЕТОДЫ ПО ПОЛУЧЕНИЯ СПИСКА ЗАДАЧ ОПРЕДЕЛЕННОГО ТИПА

    @Override
    public List<Task> getTasksList() {
        if (tasks.isEmpty()) {
            return null;
        } else {
            return new ArrayList<>(tasks.values());
        }
    }

    @Override
    public List<Epic> getEpicsList() {
        if (epics.isEmpty()) {
            return null;
        } else {
            return new ArrayList<>(epics.values());
        }
    }

    @Override
    public List<SubTask> getSubTasksList() {
        if (subTasks.isEmpty()) {
            return null;
        } else {
            return new ArrayList<>(subTasks.values());
        }
    }


    // ПОЛУЧЕНИЕ ВСЕХ САБТАСКОВ НУЖНОГО ЭПИКА

    @Override
    public List<SubTask> getAllEpicsSubtasks(int epicId) {
        List<SubTask> subTasksWithCurrentEpicId = new ArrayList<>();
        Epic epic = epics.get(epicId);

        if (epic != null) {
            epic.getSubtaskIds().forEach(integer -> {
                SubTask subTask = subTasks.get(integer);
                subTasksWithCurrentEpicId.add(subTask);
            });

            return subTasksWithCurrentEpicId;
        } else {
            return null;
        }
    }

    // МЕТОДЫ ПО ДОБАВЛЕНИЮ ЗАДАЧ

    @Override
    public void addNewTask(Task task) {
        if (task != null) {
            if (getIntersectionTestResult(task)) {
                task.setId(getIdAndIncrement());
                tasks.put(task.getId(), task);
                prioritizedTasks.add(task);
            }
        } else {
            throw new RuntimeException("Задачи с указанным ID не существует.");
        }
    }

    @Override
    public void addNewEpic(Epic epic) {
        if (epic != null) {
            epic.setId(getIdAndIncrement());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        if (subTask != null) {
            int epicsId = subTask.getEpicsId();

            if (epics.get(epicsId) == null) {
                throw new RuntimeException("Эпика с указанным ID не существует.");
            } else {
                if (getIntersectionTestResult(subTask)) {
                    subTask.setId(identifier);
                    epics.get(epicsId).addSubtaskId(identifier);
                    subTasks.put(getIdAndIncrement(), subTask);
                    prioritizedTasks.add(subTask);

                    updateEpicsInfo(epicsId);
                }
            }
        }
    }

    // МЕТОДЫ ПО УДАЛЕНИЮ ВСЕХ ЗАДАЧ

    @Override
    public void removeAllTasks() {
        tasks.values().forEach(task -> history.removeTask(task.getId()));
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        epics.values().forEach(epic -> history.removeTask(epic.getId()));
        subTasks.values().forEach((subTask -> history.removeTask(subTask.getId())));
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        HashSet<Integer> epicsIds = new HashSet<>();

        subTasks.values().forEach(subTask -> epicsIds.add(subTask.getEpicsId()));
        subTasks.values().forEach(subTask -> history.removeTask(subTask.getId()));
        subTasks.clear();
        epicsIds.forEach(id -> {
            epics.get(id).getSubtaskIds().clear();
            updateEpicsInfo(id);
        });
    }

    // МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ЗАДАЧЕ ПО ИДЕНТИФИКАТОРУ

    @Override
    public Task getTaskById(int id) {
        if (tasks.get(id) != null) {
            history.addTask(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) != null) {
            history.addTask(epics.get(id));
            return epics.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.get(id) != null) {
            history.addTask(subTasks.get(id));
            return subTasks.get(id);
        } else {
            return null;
        }
    }

    // МЕТОДЫ ДЛЯ УДАЛЕНИЯ ЗАДАЧ ПО ИДЕНТИФИКАТОРУ

    @Override
    public void removeTaskByIdentifier(int id) {
        if (tasks.containsKey(id)) {
            history.removeTask(id);
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
        } else {
            throw new RuntimeException("Задачи с таким ID нет в программе.");
        }
    }

    @Override
    public void removeEpicByIdentifier(int id) {
        if (epics.containsKey(id)) {

            for (Integer integer : epics.get(id).getSubtaskIds()) {
                history.removeTask(integer);
                prioritizedTasks.remove(subTasks.get(integer));
                subTasks.remove(integer);
            }

            history.removeTask(id);
            epics.remove(id);
        } else {
            throw new RuntimeException("Задачи с таким ID нет в программе.");
        }
    }

    @Override
    public void removeSubTaskByIdentifier(int id) {

        if (subTasks.containsKey(id)) {
            int epicsId = subTasks.get(id).getEpicsId();

            epics.get(epicsId).removeSubTaskId(id);
            history.removeTask(id);
            prioritizedTasks.remove(subTasks.get(id));
            subTasks.remove(id);


            updateEpicsInfo(epicsId);
        } else {
            throw new RuntimeException("Задачи с таким ID нет в программе.");
        }
    }

    // МЕТОДЫ ДЛЯ ОБНОВЛЕНИЯ ЗАДАЧИ ПО ИДЕНТИФИКАТОРУ

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId()) && getIntersectionTestResult(task)) {
            tasks.put(task.getId(), task);
        } else {
            throw new RuntimeException("Возникла ошибка при обновлении задачи.");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int subTaskId = subTask.getId();
        int epicsId = subTask.getEpicsId();

        if (subTasks.containsKey(subTaskId) && getIntersectionTestResult(subTask)) {
            if (epics.containsKey(epicsId)) {
                subTasks.put(subTaskId, subTask);// обновляем данные сабтаска
                updateEpicsInfo(epicsId);// обновляем данные эпика
            }
        } else {
            throw new RuntimeException("Возникла ошибка при обновлении задачи.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();

        if (epics.containsKey(id)) {
            epics.get(id).setName(epic.getName());
            epics.get(id).setDescription(epic.getDescription());
        } else {
            throw new RuntimeException("Возникла ошибка при обновлении задачи.");
        }
    }

    // Вспомогательные методы для обновления эпика

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

    private void updateEpicsStartTime(int id) {
        Epic epic = epics.get(id);

        if (epic.getSubtaskIds().isEmpty()) {
            epic.setStartTime(null);
        } else {
            List<SubTask> subTaskList = getAllEpicsSubtasks(id);
            LocalDateTime epicStartTime = subTaskList.get(0).getStartTime();

            for (SubTask subTask : subTaskList) {
                if (subTask.getStartTime() != null) {
                    if (epicStartTime.isAfter(subTask.getStartTime())) {
                        epicStartTime = subTask.getStartTime();
                    }
                }
            }
            epic.setStartTime(epicStartTime);
        }
    }

    private void updateEpicsEndTime(int id) {
        Epic epic = epics.get(id);

        if (epic.getSubtaskIds().isEmpty()) {
            epic.setEndTime(null);
        } else {
            List<SubTask> subTaskList = getAllEpicsSubtasks(id);
            LocalDateTime subtaskEndTime = subTaskList.get(0).getEndTime();

            for (SubTask subTask : subTaskList) {
                if (subTask.getEndTime() != null) {
                    if (subTask.getEndTime().isAfter(subtaskEndTime)) {
                        subtaskEndTime = subTask.getEndTime();
                    }
                }
            }
            epic.setEndTime(subtaskEndTime);
        }
    }

    private void setEpicsDuration(int id) {
        Epic epic = epics.get(id);
        if (epic.getSubtaskIds().isEmpty()) {
            epic.setEndTime(null);
        } else {
            LocalDateTime epicStart = epic.getStartTime();
            LocalDateTime epicEnd = epic.getEndTime();

            if (epicStart != null && epicEnd != null) {
                epic.setDuration(Duration.between(epicStart, epicEnd));
            }
        }
    }

    private void updateEpicsInfo(int id) {
        updateEpicStatus(id);
        updateEpicsStartTime(id);
        updateEpicsEndTime(id);
        setEpicsDuration(id);
    }
}