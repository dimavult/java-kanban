package Tasks;

import constants.TasksStatus;

public class SubTask extends Task{
    protected int epicsId;

    public SubTask(String name, String description, TasksStatus.Status status, int epicsId) {

        super(name, description, status);
        this.epicsId = epicsId;
    }

    public SubTask(int id, String name, String description, TasksStatus.Status status) {
        super(name, description, status, id);
    }

    //    public SubTask(String name, String description, TasksStatus.Status status, int id, int epicsId) {
//        super(name, description, status, id);
//        this.epicsId = epicsId;
//    }



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