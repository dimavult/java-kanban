import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.FileBackedTasksManager;
import task.Epic;
import task.Status;
import task.SubTask;
import task.Task;

import java.io.File;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    FileBackedTasksManager initManager() {
        return new FileBackedTasksManager(file);
    }

    File file = new File("tests.csv");

    @BeforeEach
    protected void createManagerBeforeTests(){
        taskManager = new FileBackedTasksManager(file);
        setUp();
    }

    @AfterEach
    protected void removeFile(){
        file.delete();
    }

    @Test
    @DisplayName("check loadFromFile method")
    public void loadFromFile() {
        taskManager.addNewEpic(new Epic("epic3", "epic3"));
        taskManager = FileBackedTasksManager.loadFromFile(file);

        assertEquals(11, taskManager.getHistory().size(), "incorrect size of history");
        assertEquals(3, taskManager.getEpicsList().size(), "epic without subtasks wasn't added");
        taskManager.removeAllTasks();
        taskManager.removeAllEpics();
        taskManager = FileBackedTasksManager.loadFromFile(file);

        assertNull(taskManager.getTasksList(), "tasks list is not empty");
        assertNull(taskManager.getEpicsList(), "epics list is not empty");
        assertNull(taskManager.getSubTasksList(), "subtasks list is not empty");
        assertEquals(0, taskManager.getHistory().size(), "history should be empty");
    }

    @Test
    @DisplayName("check save method of manager")
    public void save() {
        Task taskTest = new Task("taskTest", "taskTest", Status.IN_PROGRESS);
        Epic epicTest = new Epic("kekw", "kekl");
        SubTask subTask = new SubTask("subTest", "subTest", Status.NEW, 13);
        taskManager.addNewTask(taskTest);
        taskManager.addNewEpic(epicTest);
        taskManager.addNewSubTask(subTask);

        taskManager = FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskTest, taskManager.getTaskById(12), "task wasn't added");
        /*
        проверка эпиков в таком виде, потому что при создании эпика создается новый список id сабтасок,
        из-за чего хэшкоды эпиков не совпадают
         */
        assertEquals(epicTest.getName(), taskManager.getEpicById(13).getName(), "epic wasn't added");
        assertEquals(epicTest.getId(), taskManager.getEpicById(13).getId(), "epic wasn't added");
        assertEquals(epicTest.getDescription(), taskManager.getEpicById(13).getDescription(), "epic wasn't added");
        assertEquals(subTask, taskManager.getSubTaskById(14), "subtask wasn't added");
    }



}