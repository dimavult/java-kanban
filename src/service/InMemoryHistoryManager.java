package service;

import interfaces.HistoryManager;
import task.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int BROWSING_HISTORY_MAX_SIZE = 10;

    private static List<Task> browsingHistory = new LinkedList<>();

    public List<Task> getBrowsingHistory() {
        return browsingHistory;
    }

    @Override
    public void addTask(Task task) {
        if (browsingHistory.size() == BROWSING_HISTORY_MAX_SIZE) { // если 10 = 10
            browsingHistory.remove(BROWSING_HISTORY_MAX_SIZE - 1); // стало 9
        }
        browsingHistory.add(0, task); // снова стало 10
    }

    @Override
    public List<Task> getHistory() {
        return browsingHistory;
    }
}