package task;

import constants.TasksStatus;

public class SubTask extends Task{
    protected int epicsId;

    /*контруктор создан для метода по созданию сабтаски, так как нам нужно знать только epicId для создания*/
    public SubTask(String name, String description, TasksStatus.Status status, int epicsId) {
        super(name, description, status);
        this.epicsId = epicsId;
    }

    public SubTask(String name, String description, TasksStatus.Status status, int id, int epicsId) {
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
}