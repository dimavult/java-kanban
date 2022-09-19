import service.HTTPTaskManager;

import java.net.URI;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {
    @Override
    public HTTPTaskManager initManager() {
        return new HTTPTaskManager(URI.create("http://localhost:8078/"));
    }
}