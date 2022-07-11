package service;

import interfaces.HistoryManager;
import task.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    @Override
    public void addTask(Task task) {
        if (browsingHistory.size() == BROWSING_HISTORY_MAX_SIZE){
            browsingHistory.remove(0);
            browsingHistory.add(task);
        } else if (browsingHistory.size() <= BROWSING_HISTORY_MAX_SIZE) {
            browsingHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }
}
