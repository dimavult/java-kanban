public class Task {
    private String name;
    private String description;
    private String status;
    private int id;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "  Название задачи: " + name +
                "\n  Статус задачи: " + status +
                "\n  Описание задачи: " + description;
    }

    public String getTaskType(){
        return "Задача";
    }

}
