import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{
    private ArrayList<Integer> subTaskNumbers = new ArrayList<>();

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubTaskNumbers() {
        ArrayList<Integer> numbersList = new ArrayList<>();
        for (Integer subTaskNumber : subTaskNumbers) {
            numbersList.add(subTaskNumbers.get(subTaskNumber));
        }
        return numbersList;
    }

    public void setSubTaskNumbers(int id) {
        this.subTaskNumbers = subTaskNumbers;
    }

    @Override
    public String getTaskType() {
        return "Эпик";
    }
}
