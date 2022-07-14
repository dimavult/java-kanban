package interfaces;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    List<Task> getHistory();

}
