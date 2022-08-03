package interfaces;

import service.InMemoryHistoryManager;
import task.Task;

import java.util.LinkedHashMap;
import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    void removeTask(int id);

    List<Task> getHistory();

}
