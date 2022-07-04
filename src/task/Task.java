package task;

import constants.TasksStatus;

public class Task {/*Изменил содержание Класса. Добавил конструктор для работы с вводом пользователя для работы с
                     классом Epic, изменил тип переменной status на enum, поместил классы с тасками в отдельный пакет*/
    protected String name;
    protected String description;
    protected TasksStatus.Status status;
    protected int id;

    public Task(String name, String description, TasksStatus.Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public Task(String name, String description, TasksStatus.Status status) { /*Перегрузил конструктор для создания
                                                                                конструктора сабтаски*/
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, int id) { // перегрузил конструктор для создания конструктора Эпика
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public Task(String name, String description) { // перегрузил конструктор для создания конструктора Эпика
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TasksStatus.Status getStatus() {
        return status;
    }

    public void setStatus(TasksStatus.Status status) {
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
}
