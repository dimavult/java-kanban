package interfaces;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    int BROWSING_HISTORY_MAX_SIZE = 10;

    List<Task> browsingHistory = new ArrayList<>();

    void addTask(Task task);

    List<Task> getHistory();

}
