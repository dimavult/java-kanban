package Tasks;

public class Task {/*Изменил содержание Класса. Добавил конструктор для работы с вводом пользователя для работы с
                     классом Epic, изменил тип переменной status на enum, поместил классы с тасками в отдельный пакет*/
    private String name;
    private String description;
    private Status status;
    private int id;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
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

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "Тип задачи - " + getTaskType() + "  Название задачи: " + name +
                "\n  Статус задачи: " + status +
                "\n  Описание задачи: " + description;
    }

    public String getTaskType(){
        return "задача";
    }

    public enum Status{
        NEW, IN_PROGRESS, DONE;
    }

}
