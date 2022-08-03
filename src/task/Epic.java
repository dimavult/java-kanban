package task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int id){
        subtaskIds.add(id);
    }

    public Epic(String name, String description) {
        super(name, description);
        this.status = Status.NEW;
        subtaskIds = new ArrayList<>();
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    @Override
    public String getTaskType() {
        return "эпик";
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
        return super.toString() + "\n  ID подзадач эпика " + getSubtaskIds();
    }
}