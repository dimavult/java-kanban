package service;

import interfaces.HistoryManager;
import interfaces.TaskManager;

import java.net.URI;

public class Managers {

    public static TaskManager getDefault(URI uri) {
        return new HTTPTaskManager(uri);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
