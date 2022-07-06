package task;

import java.util.Objects;

public class SubTask extends Task{
    protected int epicsId;

    /*контруктор создан для метода по созданию сабтаски, так как нам нужно знать только epicId для создания*/
    public SubTask(String name, String description, Status status, int epicsId) {
        super(name, description, status);
        this.epicsId = epicsId;
    }

    public SubTask(String name, String description, Status status, int id, int epicsId) {
        super(name, description, status, id);
        this.epicsId = epicsId;
    }

    public int getEpicsId() {
        return epicsId;
    }

    public void setEpicsId(int epicsId) {
        this.epicsId = epicsId;
    }

    @Override
    public String getTaskType() {
        return "подзадача";
    }

    @Override
    public String toString() {
        return super.toString() + "\n  ID эпика " + epicsId;
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