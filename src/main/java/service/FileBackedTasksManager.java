package service;

import exception.ManagerSaveException;
import interfaces.HistoryManager;
import task.*;

import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path path;

    public FileBackedTasksManager(File file) {
        this.path = file.toPath();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        if (file.exists()) {
            FileBackedTasksManager manager = new FileBackedTasksManager(file);

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                reader.readLine();
                while (reader.ready()) {
                    String text = reader.readLine();
                    if (!text.isEmpty()) {
                        addTasksBackFromFile(manager, text);
                    } else if (text.isBlank()) {
                        addHistoryBackFromFile(manager, reader.readLine());
                    }
                }
                addPrioritizedTasksBackFromFile(manager);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return manager;
        } else {
            System.out.println("Файл не найден. Создайте файл путем создания задач.");
            return null;
        }
    }

    private static void addPrioritizedTasksBackFromFile(FileBackedTasksManager manager) {
        if (manager.getTasksList() != null) {
            manager.prioritizedTasks.addAll(manager.getTasksList());
        }
        if (manager.getSubTasksList() != null) {
            manager.prioritizedTasks.addAll(manager.getSubTasksList());
        }
    }

    private static void addTasksBackFromFile(FileBackedTasksManager manager, String value){
        if (value != null && manager != null) {
            Task task = manager.fromString(value);

            switch (task.getTaskType()) {
                case TASK:
                    manager.tasks.put(task.getId(), task);
                    break;
                case EPIC:
                    manager.epics.put(task.getId(), (Epic) task);
                    break;
                case SUBTASK:
                    manager.subTasks.put(task.getId(), (SubTask) task);
                    break;
            }
        }
    }

    private  Map<Integer, Task> getAllTasks(){
        Map<Integer, Task> allTasks = new HashMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(epics);
        allTasks.putAll(subTasks);

        return allTasks;
    }

    private static void addHistoryBackFromFile(FileBackedTasksManager manager, String value){
        if (value != null && manager != null) {
            List<Integer> history = historyFromString(value);

            history.forEach(integer -> {
                Task task = manager.getAllTasks().get(integer);
                manager.history.addTask(task);
            });
        }
    }

    private List<Task> sortTasksToList() {
        List<Task> sortedList = new ArrayList<>();

        if (getTasksList() != null) {
            sortedList.addAll(getTasksList());
        }

        if (getEpicsList() != null) {
            sortedList.addAll(getEpicsList());
        }

        if (getSubTasksList() != null) {
            sortedList.addAll(getSubTasksList());
        }

        sortedList.sort(Comparator.comparingInt(Task::getId));

        return sortedList;
    }

    private void save() {
        try (Writer writer = new BufferedWriter(new FileWriter(path.toString()))) {

            writer.write("id,type,name,status,description,startTime,duration,epic\n");

            for (Task task : sortTasksToList()) {
                writer.write(task.toString() + "\n");
            }

            if (!history.getHistory().isEmpty()) {
                writer.write("\n");
                writer.write(historyToString(history));
            }
        } catch (IOException exp) {
            exp.printStackTrace();
            throw new ManagerSaveException(exp.getMessage());
        }
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder history = new StringBuilder();

        for (int i = 0; i < manager.getHistory().size(); i++) {
            Task task = manager.getHistory().get(i);
            if (manager.getHistory().size() - 1 == i) {
                history.append(task.getId());
            } else {
                history.append(task.getId()).append(",");
            }
        }
        return history.toString();
    }

    private static List<Integer> historyFromString(String value) {
        if (value != null) {
            List<Integer> historyIds = new ArrayList<>();
            String[] ids = value.split(",");
            for (String line : ids) {
                historyIds.add(Integer.parseInt(line));
            }
            return historyIds;
        } else {
            return null;
        }
    }
    /*
    можно было, конечно, декомпозировать метод на три в зависимости от taskType, но я легких путей не ищу
     */
    private Task fromString(String value) {
        String[] partsOfLine = value.split(",");
        int id = Integer.parseInt(partsOfLine[0]);
        String type = partsOfLine[1];
        String name = partsOfLine[2];
        Status status = Status.valueOf(partsOfLine[3]);
        String description = partsOfLine[4];
        LocalDateTime startTime = null;
        Duration duration = null;
        int epicId = 0;

        if (partsOfLine.length == 7) {// типа если больше 6, то точно есть и стартайм и продолжительность
            startTime = LocalDateTime.parse(partsOfLine[5]);
            duration = Duration.parse(partsOfLine[6]);
        }
        if (partsOfLine.length == 6) {
            epicId = Integer.parseInt(partsOfLine[5]);
        } else if (partsOfLine.length == 8) {
            epicId = Integer.parseInt(partsOfLine[7]);
        }

        return addTaskBackFromFile(type, name, description, status, epicId, id, duration, startTime);
    }

    private static Task addTaskBackFromFile(String type,
                                            String name,
                                            String description,
                                            Status status,
                                            int epicId,
                                            int id,
                                            Duration duration,
                                            LocalDateTime startTime) {
        Task task = null;
        if (startTime != null) {
            switch (type) {
                case "TASK":
                    task = new Task(name, description, status, id, duration, startTime);
                    break;
                case "EPIC":
                    task = new Epic(name, description, status, id, duration, startTime);
                    break;
                case "SUBTASK":
                    task = new SubTask(name, description, status, id, epicId, duration, startTime);
                    break;
            }
        } else {
            switch (type) {
                case "TASK":
                    task = new Task(name, description, status, id);
                    break;
                case "EPIC":
                    task = new Epic(name, description, id, status);
                    break;
                case "SUBTASK":
                    task = new SubTask(name, description, status, id, epicId);
                    break;
            }
        }
        return task;
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addNewSubTask(SubTask subTask) {
        super.addNewSubTask(subTask);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void removeTaskByIdentifier(int id) {
        super.removeTaskByIdentifier(id);
        save();
    }

    @Override
    public void removeEpicByIdentifier(int id) {
        super.removeEpicByIdentifier(id);
        save();
    }

    @Override
    public void removeSubTaskByIdentifier(int id) {
        super.removeSubTaskByIdentifier(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

}
