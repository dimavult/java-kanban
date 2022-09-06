import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    protected void createManagerBeforeTests(){
        taskManager = new InMemoryTaskManager();
        setUp();
    }

    @Test
    @DisplayName("Getting a list of prioritized tasks")
    void getPrioritizedTasks() {
        assertEquals(taskManager.getSubTaskById(3),
                taskManager.getPrioritizedTasks().get(0),
                "wrong subsequence in prioritized list");
        assertThrows(IndexOutOfBoundsException.class,() -> taskManager.getPrioritizedTasks().get(112));
    }

    @Test
    @DisplayName("Checker of history getter")
    public void getHistory() {
        Epic task = taskManager.getEpicById(1);
        Task task1 = taskManager.getEpicById(2);
        int size = taskManager.getHistory().size();

        assertEquals(size, taskManager.getHistory().size(), "wrong number of tasks in history list");
        assertEquals(task, taskManager.getHistory().get(size-2), "wrong task subsequence");
        assertEquals(task1, taskManager.getHistory().get(size-1), "wrong task subsequence");
        taskManager.removeAllEpics();
        taskManager.removeAllTasks();
        assertTrue(taskManager.getHistory().isEmpty(), "history should be empty");
    }

    @Test
    @DisplayName("getting a list of tasks")
    public void getTasksList() {
        Task task = new Task("--", "--", Status.DONE);
        taskManager.addNewTask(task);
        assertEquals(task, taskManager.getTasksList().get(4), "tasks are not equal");
        assertEquals(5, taskManager.getTasksList().size(), "wrong number of tasks");
        taskManager.removeAllTasks();
        assertNull(taskManager.getTasksList(), "the list of tasks isn't empty");
    }

    @Test
    @DisplayName("getting a list of epics")
    public void getEpicsList() {
        Epic task = new Epic("--", "--");
        taskManager.addNewEpic(task);
        assertEquals(task, taskManager.getEpicsList().get(2), "Epics are not equal");
        assertEquals(3, taskManager.getEpicsList().size(), "wrong number of Epics");
        taskManager.removeAllEpics();
        assertNull(taskManager.getEpicsList(), "the list of Epics isn't empty");
    }

    @Test
    @DisplayName("getting a list of subtasks")
    public void getSubTasksList() {
        SubTask task = new SubTask("--", "--", Status.DONE, 1);
        taskManager.addNewSubTask(task);
        assertEquals(task, taskManager.getSubTasksList().get(5), "SubTasks are not equal");
        assertEquals(6, taskManager.getSubTasksList().size(), "wrong number of SubTasks");
        taskManager.removeAllSubTasks();
        assertNull(taskManager.getSubTasksList(), "the list of SubTasks isn't empty");
    }

    @Test
    @DisplayName("getting all epics subtasks")
    public void getAllEpicsSubTasks() {
        assertNull(taskManager.getAllEpicsSubtasks(5));
        assertEquals(2, taskManager.getAllEpicsSubtasks(1).size(), "wrong number of subtasks");
        taskManager.removeAllSubTasks();
        assertEquals(0,
                taskManager.getAllEpicsSubtasks(1).size(),
                "the list of Epics subtasks isn't empty");
    }

    @Test
    @DisplayName("adding a task")
    public void addNewTask() {
        Task task = new Task("--", "--", Status.NEW);
        taskManager.addNewTask(task);
        assertTrue(taskManager.getTasksList().contains(task), "task hasn't been added");
        Task task1 = null;
        assertThrows(RuntimeException.class, () -> taskManager.addNewTask(task1), "null task has been added");
    }

    @Test
    @DisplayName("adding an epic")
    public void addNewEpic() {
        Epic task = new Epic("--", "--");
        taskManager.addNewEpic(task);
        assertTrue(taskManager.getEpicsList().contains(task), "epic hasn't been added");
        Epic task1 = null;
        taskManager.addNewEpic(task1);
        assertFalse(taskManager.getEpicsList().contains(task1), "null epic has been added");
    }

    @Test
    @DisplayName("adding a subtask")
    public void addNewSubTask() {
        SubTask task = new SubTask("--", "--", Status.NEW, 1);
        taskManager.addNewSubTask(task);
        assertTrue(taskManager.getSubTasksList().contains(task), "subtask hasn't been added");
        SubTask task1 = null;
        taskManager.addNewSubTask(task1);
        assertFalse(taskManager.getSubTasksList().contains(task1), "null subtask has been added");
        SubTask task2 = new SubTask("--", "--", Status.NEW, 150);
        assertThrows(RuntimeException.class,
                () -> taskManager.addNewSubTask(task2),
                "cant add subtask with non-existent epic");
    }

    @Test
    @DisplayName("clear task map")
    public void removeAllTasks() {
        taskManager.removeAllTasks();
        assertNull(taskManager.getTasksList(), "remove method didn't clear a map of tasks");
        List<Task> filteredHistory = taskManager.getHistory().stream()
                .filter(task -> task.getTaskType() != TaskType.EPIC)
                .filter(task -> task.getTaskType() != TaskType.SUBTASK)
                .collect(Collectors.toList());
        assertTrue(filteredHistory.isEmpty(), "remove method didn't delete tasks from history");
    }

    @Test
    @DisplayName("clear epic map")
    public void removeAllEpics() {
        taskManager.removeAllEpics();
        assertNull(taskManager.getEpicsList(), "remove method didn't clear a map of epics");
        List<Task> filteredHistory = taskManager.getHistory().stream()
                .filter(task -> task.getTaskType() != TaskType.TASK)
                .collect(Collectors.toList());
        assertTrue(filteredHistory.isEmpty(), "remove method didn't delete tasks from history");
    }

    @Test
    @DisplayName("clear subtasks map")
    public void removeAllSubTasks() {
        taskManager.removeAllSubTasks();
        assertNull(taskManager.getSubTasksList(), "remove method didn't clear a map of epics");
        List<Task> filteredHistory = taskManager.getHistory().stream()
                .filter(task -> task.getTaskType() != TaskType.TASK)
                .filter(task -> task.getTaskType() != TaskType.EPIC)
                .collect(Collectors.toList());
        assertTrue(filteredHistory.isEmpty(), "remove method didn't delete tasks from history");
    }

    @Test
    @DisplayName("getting tasks by id")
    public void getTaskById() {
        Task task = new Task("--", "--", Status.NEW);
        taskManager.addNewTask(task);
        assertEquals(task, taskManager.getTaskById(12), "getter returns wrong value");
        assertNull(taskManager.getTaskById(999999), "task with incorrect ID should be null");
        taskManager.removeAllTasks();
        assertNull(taskManager.getTaskById(1), "get method should return null if task map is empty");
    }

    @Test
    @DisplayName("getting epic by id")
    public void getEpicById() {
        Epic task = new Epic("--", "--");
        taskManager.addNewEpic(task);
        assertEquals(task, taskManager.getEpicById(12), "getter returns wrong value");
        assertNull(taskManager.getEpicById(999999), "epic with incorrect ID should be null");
        taskManager.removeAllEpics();
        assertNull(taskManager.getEpicById(1), "get method should return null if epic map is empty");
    }

    @Test
    @DisplayName("getting epic by id")
    public void getSubTaskById() {
        SubTask task = new SubTask("--", "--", Status.IN_PROGRESS, 1);
        taskManager.addNewSubTask(task);
        assertEquals(task, taskManager.getSubTaskById(12), "getter returns wrong value");
        assertNull(taskManager.getSubTaskById(999999), "subtask with incorrect ID should be null");
        taskManager.removeAllSubTasks();
        assertNull(taskManager.getSubTaskById(1), "get method should return null if subtask map is empty");
    }

    @Test
    @DisplayName("removing task by ID")
    public void removeTaskByIdentifier() {
        Task task = taskManager.getTaskById(8);
        taskManager.removeTaskByIdentifier(8);
        assertFalse(taskManager.getTasksList().contains(task), "task map shouldn't contain deleted task");
        taskManager.removeAllTasks();
        assertThrows(RuntimeException.class,
                () -> taskManager.removeTaskByIdentifier(1),
                "get method should return null if task map is empty");
    }

    @Test
    @DisplayName("removing epic by ID")
    public void removeEpicByIdentifier() {
        taskManager.removeEpicByIdentifier(1);

        assertThrows(RuntimeException.class, () ->
                taskManager.removeEpicByIdentifier(1),
                "epic map shouldn't contain deleted epic");

        taskManager.removeAllEpics();

        assertThrows(RuntimeException.class,
                () -> taskManager.removeEpicByIdentifier(1),
                "get method should return null if task map is empty");

    }

    @Test
    @DisplayName("removing subTask by ID")
    public void removeSubTaskByIdentifier() {
        SubTask task = new SubTask("--", "--", Status.NEW, 1);
        taskManager.addNewSubTask(task);
        taskManager.removeSubTaskByIdentifier(12);

        assertFalse(taskManager.getSubTasksList().contains(task),
                "subtask map shouldn't contain deleted subtask");

        taskManager.removeAllSubTasks();

        assertThrows(RuntimeException.class,
                () -> taskManager.removeSubTaskByIdentifier(1),
                "get method should return null if subtask map is empty");
    }

    @Test
    @DisplayName("updating task")
    public void updateTask() {
        Task task = null;

        assertThrows(RuntimeException.class,
                () -> taskManager.updateTask(task),
                "cant put null in method");

        Task task1 = new Task("anotherName",
                "anotherDesc", Status.IN_PROGRESS,
                8,
                Duration.ofMinutes(50),
                LocalDateTime.of(4000, 6, 12, 12, 0));
        Task oldTask = taskManager.getTaskById(8);
        taskManager.updateTask(task1);

        assertNotEquals(task1, oldTask, "tasks are equal");
        /*
        checking each updated field for inequality
         */
        assertNotEquals(task1.getStartTime(), oldTask.getStartTime());
        assertNotEquals(task1.getDuration(), oldTask.getDuration());
        assertNotEquals(task1.getDescription(), oldTask.getDescription());
        assertNotEquals(task1.getName(), oldTask.getName());
        assertNotEquals(task1.getStatus(), oldTask.getStatus());

        Task task2 = new Task("anotherName",
                "anotherDesc", Status.IN_PROGRESS,
                3123,
                Duration.ofMinutes(60),
                LocalDateTime.of(5000, 6, 12, 12, 0));

        assertThrows(RuntimeException.class,
                () -> taskManager.updateTask(task2),
                "cant update non-existent task");
    }

    @Test
    @DisplayName("updating epic")
    public void updateEpic() {
        Epic task = null;

        assertThrows(RuntimeException.class,
                () -> taskManager.updateEpic(task),
                "cant put null in method");

        Epic task1 = new Epic("anotherName",
                "anotherDesc", 1);
        Epic oldTask = taskManager.getEpicById(1);
        taskManager.updateEpic(task1);

        assertNotEquals(task1, oldTask, "epics are equal");
        /*
        checking each updated field for equality
         */
        assertEquals(task1.getDescription(), oldTask.getDescription());
        assertEquals(task1.getName(), oldTask.getName());

        Epic task2 = new Epic("anotherName",
                "anotherDesc", 14124);

        assertThrows(RuntimeException.class,
                () -> taskManager.updateTask(task2),
                "cant update non-existent epic");
    }

    @Test
    @DisplayName("updating task")
    public void updateSubTask() {
        SubTask task = null;

        assertThrows(RuntimeException.class,
                () -> taskManager.updateSubTask(task),
                "cant put null in method");

        SubTask task1 = new SubTask("anotherName",
                "anotherDesc", Status.DONE,
                6,
                1,
                Duration.ofMinutes(50),
                LocalDateTime.of(1000, 6, 12, 12, 0));
        SubTask oldTask = taskManager.getSubTaskById(6);
        taskManager.updateSubTask(task1);

        assertNotEquals(task1, oldTask, "SubTasks are equal");
        /*
        checking each updated field for inequality
         */
        assertNotEquals(task1.getStartTime(), oldTask.getStartTime());
        assertNotEquals(task1.getDuration(), oldTask.getDuration());
        assertNotEquals(task1.getDescription(), oldTask.getDescription());
        assertNotEquals(task1.getName(), oldTask.getName());
        assertNotEquals(task1.getStatus(), oldTask.getStatus());

        SubTask task2 = new SubTask("anotherName",
                "anotherDesc", Status.IN_PROGRESS,
                3123,
                Duration.ofMinutes(60),
                LocalDateTime.of(5000, 6, 12, 12, 0));

        assertThrows(RuntimeException.class,
                () -> taskManager.updateSubTask(task2),
                "cant update non-existent SubTask");
    }

}