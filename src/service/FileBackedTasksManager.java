package service;

import exception.ManagerSaveException;
import interfaces.HistoryManager;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class FileBackedTasksManager extends InMemoryTaskManager {
    public FileBackedTasksManager(File file) {
    }

    private static class TaskComparator implements Comparator<Task> { //
        public int compare(Task task1, Task task2) {
            return task1.getId() - task2.getId();
        }
    }

//    public static FileBackedTasksManager loadFromFile(File file) {
//        if (file != null) {
//            FileBackedTasksManager manager = new FileBackedTasksManager(file);
//            try {
//                String text = Files.readString(Path.of(file.getAbsolutePath()));
//                String[] lines = text.split("\n");
//
//                if (!lines[lines.length-2].isEmpty()) {
//                    for (int i = 1; i < lines.length-3; i++) {
//                        manager.fromString(lines[i]);
//                    }
//                    List<Integer> ids = historyFromString(lines[lines.length-1]);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return manager;
//        } else {
//            return null;
//        }
//    }

    public static FileBackedTasksManager loadFromFile(File file) {
        if (file != null) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return manager;
        } else {
            return null;
        }
    }

    private static void addTasksBackFromFile(FileBackedTasksManager manager, String value){
        Task task = manager.fromString(value);

        switch (task.getTaskType()) {
            case TASK:
                manager.addNewTask(task);
                break;
            case EPIC:
                manager.addNewEpic((Epic) task);
                break;
            case SUBTASK:
                manager.addNewSubTask((SubTask) task);
                break;
        }
    }

    private static void addHistoryBackFromFile(FileBackedTasksManager manager, String value){
        List<Integer> history = historyFromString(value);

        for (Integer id: history) {
            if (manager.getTasks().containsKey(id)) {
                manager.getTaskById(id);
            } else if (manager.getEpics().containsKey(id)){
                manager.getEpicById(id);
            } else if(manager.getSubTasks().containsKey(id)){
                manager.getSubTaskById(id);
            }
        }
    }

    private List<Task> sortTasksToList() {
        var comparator = new TaskComparator();
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

        sortedList.sort(comparator);

        return sortedList;
    }

    private void save() {
        try (Writer writer = new BufferedWriter(new FileWriter("data.txt"))) {

            writer.write("id,type,name,status,description,epic\n");

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

    private Task fromString(String value) {
        String[] partsOfLine = value.split(",");
        int id = Integer.parseInt(partsOfLine[0]);
        String type = partsOfLine[1];
        String name = partsOfLine[2];
        String status = partsOfLine[3];
        String description = partsOfLine[4];
        int epicId = 0;
        Task task = null;

        if (partsOfLine.length == 6) {
            epicId = Integer.parseInt(partsOfLine[5]);
        }
        switch (type) {
            case "TASK":
                task = new Task(name, description, Status.valueOf(status), id);
                break;
            case "EPIC":
                task = new Epic(name, description, id);
                break;
            case "SUBTASK":
                task = new SubTask(name, description, Status.valueOf(status), id, epicId);
                break;
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
