package task;

import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static DateTimeFormatter getDatePattern(){
        return DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
    }
}
