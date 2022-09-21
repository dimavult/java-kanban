package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import server.KVTaskClient;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskType;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class HTTPTaskManager extends FileBackedTasksManager{
    private final URI uri;
    private final KVTaskClient client;
    private final static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public HTTPTaskManager(URI kvSeverUri) {
        this.uri = kvSeverUri;
        client = new KVTaskClient(kvSeverUri);
        load();
    }

    @Override
    public void save() {
        client.put("task", gson.toJson(tasks));
        client.put("epic", gson.toJson(epics));
        client.put("subtask", gson.toJson(subTasks));
        client.put("history", gson.toJson(getHistory()));
    }

    public void load() {
        Map<Integer, Task> taskMap = gson.fromJson(client.load("task"),
                new TypeToken<Map<Integer, Task>>(){}.getType());
        Map<Integer, Epic> epicMap = gson.fromJson(client.load("epic"),
                new TypeToken<Map<Integer, Epic>>(){}.getType());
        Map<Integer, SubTask> subtaskMap = gson.fromJson(client.load("subtask"),
                new TypeToken<Map<Integer, SubTask>>(){}.getType());
        List<Task> historyList = gson.fromJson(client.load("history"),
                new TypeToken<List<Task>>(){}.getType());

        if (taskMap != null) {
            tasks.putAll(taskMap);
            prioritizedTasks.addAll(tasks.values());
        }
        if (epicMap != null) epics.putAll(epicMap);
        if (subtaskMap != null) {
            subTasks.putAll(subtaskMap);
            prioritizedTasks.addAll(subTasks.values());
        }
        if (historyList != null) {
            historyList.forEach(task -> {
                TaskType type = task.getTaskType();
                int id = task.getId();
                switch (type){
                    case TASK:
                        getTaskById(id);
                        break;
                    case EPIC:
                        getEpicById(id);
                        break;
                    case SUBTASK:
                        getSubTaskById(id);
                        break;
                    default:
                        System.out.println("Incorrect task type");
                }
            });
        }
    }
}
