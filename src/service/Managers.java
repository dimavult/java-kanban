package service;

import interfaces.HistoryManager;
import interfaces.TaskManager;

public class Managers {

    private static TaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    public static TaskManager getDefault(){
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory(){
        return inMemoryHistoryManager;
    }
}
