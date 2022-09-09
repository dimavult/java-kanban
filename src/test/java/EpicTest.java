import interfaces.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import task.Epic;
import task.Status;
import task.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private TaskManager taskManager;

    @BeforeEach
    public void createManager(){
        taskManager = Managers.getDefault();
    }

    @Test
    public void epicsStatusShouldBeNewIfItHasNoSubtasks(){
        Epic epic = new Epic("--", "--");
        taskManager.addNewEpic(epic);
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void epicsStatusShouldBeNewIfSubtasksAreNew(){
        Epic epic = new Epic("--", "--");
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2022, Month.AUGUST, 30, 19, 3)));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 30, 22, 23)));
        assertEquals(Status.NEW, epic.getStatus(), "epic's status should be NEW");
    }

    @Test
    public void epicsStatusShouldBeDoneIfSubtasksAreDone(){
        Epic epic = new Epic("--", "--");
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1, Duration.ofHours(2), LocalDateTime.of(2022, Month.AUGUST, 30, 19, 3)));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 30, 22, 23)));
        assertEquals(Status.DONE, epic.getStatus(), "epic's status should be DONE");
    }

    @Test
    public void epicsStatusShouldBeDoneIfSubtasksAreNewAndDone(){
        Epic epic = new Epic("--", "--");
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(new SubTask("--", "--", Status.NEW, 1, Duration.ofHours(2), LocalDateTime.of(2022, Month.AUGUST, 30, 19, 3)));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.DONE, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 30, 22, 23)));
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "epics status should be IN_PROGRESS");
    }

    @Test
    public void epicsStatusShouldBeInProgressIfSubtasksAreInProgress(){
        Epic epic = new Epic("--", "--");
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(new SubTask("--", "--", Status.IN_PROGRESS, 1, Duration.ofHours(2), LocalDateTime.of(2022, Month.AUGUST, 30, 19, 3)));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.IN_PROGRESS, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 30, 22, 23)));
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "epics status should be IN_PROGRESS");
    }

    @Test
    public void epicsSTartTImeShouldBeEqualToLowestSubTaskStartTime() {
        Epic epic = new Epic("--", "--");
        taskManager.addNewEpic(epic);
        taskManager.addNewSubTask(new SubTask("--", "--", Status.IN_PROGRESS, 1, Duration.ofHours(2), LocalDateTime.of(2022, Month.AUGUST, 30, 19, 3)));
        taskManager.addNewSubTask(new SubTask("--", "--", Status.IN_PROGRESS, 1, Duration.ofHours(1), LocalDateTime.of(2022, Month.AUGUST, 30, 22, 23)));
        assertEquals(LocalDateTime.of(2022, Month.AUGUST, 30, 19, 3), epic.getStartTime());
    }
}