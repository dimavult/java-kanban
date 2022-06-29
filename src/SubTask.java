public class SubTask extends Task{
    private int epicsId;

    public SubTask(String name, String description, String status) {
        super(name, description, status);
    }

    public int getEpicsId() {
        return epicsId;
    }

    public void setEpicsId(int epicsId) {
        this.epicsId = epicsId;
    }

    @Override
    public String getTaskType() {
        return "Подзадача";
    }
}