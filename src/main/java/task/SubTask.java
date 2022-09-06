package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    protected int epicsId;

    public SubTask(String name, String description, Status status, int epicsId) {
        super(name, description, status);
        this.epicsId = epicsId;
    }

    public SubTask(String name, String description, Status status, int id, int epicsId) {
        super(name, description, status, id);
        this.epicsId = epicsId;
    }

    public SubTask(String name, String description, Status status, int id, int epicsId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
        this.epicsId = epicsId;
    }

    public SubTask(String name, String description, Status status, int epicsId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicsId = epicsId;
    }

    public int getEpicsId() {
        return epicsId;
    }

    public void setEpicsId(int epicsId) {
        this.epicsId = epicsId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        if (duration != null && startTime != null) {
            return id + "," +
                    getTaskType() + "," +
                    name + "," +
                    status + "," +
                    description + "," +
                    startTime + "," +
                    duration + "," +
                    epicsId;
        } else {
            return id + "," +
                    getTaskType() + "," +
                    name + "," +
                    status + "," +
                    description + "," +
                    epicsId;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicsId == subTask.epicsId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicsId);
    }
}