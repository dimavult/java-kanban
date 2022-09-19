package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
    private ArrayList<Integer> subtaskIds;
    private LocalDateTime endTime;

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int id){
        subtaskIds.add(id);
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public Epic(String name, String description) {
        super(name, description);
        this.status = Status.NEW;
        subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id);
        this.status = status;
    }

    public Epic(String name, String description, Status status, int id, Duration duration, LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public void removeSubTaskId(int id){
        subtaskIds.remove((Integer) id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}