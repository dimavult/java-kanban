package task;

import constants.TasksStatus;

import java.util.ArrayList;

public class Epic extends Task{ /*Изменил содержание Класса. Добавил конструктор для работы с вводом пользователя,
                                  оставив второй конструктор для внутренней работы приложения.*/

    private ArrayList<Integer> subtaskIds = new ArrayList<>();


    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int id){
        subtaskIds.add(id);
    }

    public Epic(String name, String description) {
        super(name, description);
        this.status = TasksStatus.Status.NEW;
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
}
