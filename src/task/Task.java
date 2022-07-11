package task;

import java.util.Objects;

public class Task {/*Изменил содержание Класса. Добавил конструктор для работы с вводом пользователя для работы с
                     классом Epic, изменил тип переменной status на enum, поместил классы с тасками в отдельный пакет*/
    protected String name;
    protected String description;
    protected Status status;
    protected int id;

    public Task(String name, String description, Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public Task(String name, String description, Status status) { /*Перегрузил конструктор для создания
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "ID задачи - " + id + "\n  Тип задачи - " + getTaskType() + "\n  Название задачи: " + name +
                "\n  Статус задачи: " + status +
                "\n  Описание задачи: " + description;
    }

    public String getTaskType(){
        return "задача";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }
}