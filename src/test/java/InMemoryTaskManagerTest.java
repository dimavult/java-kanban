import service.FileBackedTasksManager;
import service.InMemoryTaskManager;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager initManager() {
        return new InMemoryTaskManager();
    }
}