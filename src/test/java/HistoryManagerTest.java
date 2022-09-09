import interfaces.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    InMemoryHistoryManager manager;

    @BeforeEach
    void createManager() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    void addTask() {
        assertEquals(0, manager.getHistory().size(), "history should be empty until addTask");

        Task task1 = new Task("--", "--",1);
        Task task2 = new Task("--", "--",2);
        Task task3 = new Task("--", "--",3);
        Task task4 = new Task("--", "--",4);
        Task task5 = null;
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        assertThrows(RuntimeException.class, () -> manager.addTask(task5), "cant add task with null value" );;

        assertTrue(manager.getHistory().contains(task1), "task was not added to history");

        manager.addTask(task4);
        manager.addTask(task4);

        assertEquals(4, manager.getHistory().size(), "history shouldn't have copies of tasks");
    }

    @Test
    void removeTask() {
        assertEquals(0, manager.getHistory().size(), "history should be empty until addTask");

        Task task1 = new Task("--", "--",1);
        Task task2 = new Task("--", "--",2);
        Task task3 = new Task("--", "--",3);
        Task task4 = new Task("--", "--",4);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        
        assertTrue(manager.getHistory().contains(task4), "task was not added to history");

        manager.removeTask(2);
        assertFalse(manager.getHistory().contains(task2), "history shouldn't contain task after remove");
    }

    @Test
    void getHistory() {
        assertEquals(0, manager.getHistory().size(), "history should be empty until addTask");

        Task task1 = new Task("--", "--",1);
        Task task2 = new Task("--", "--",2);
        Task task3 = new Task("--", "--",3);
        Task task4 = new Task("--", "--",4);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);

        assertEquals(4, manager.getHistory().size(), "incorrect size of history");

        manager.addTask(task4);

        assertEquals(4, manager.getHistory().size(), "history shouldn't contain copies of tasks");
    }
}