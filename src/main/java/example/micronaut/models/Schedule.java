package example.micronaut.models;

import java.util.List;
import java.util.Map;

public class Schedule {
    private List<News> day;

    public List<News> getDay() {
        return day;
    }

    public void setDay(List<News> day) {
        this.day = day;
    }

    // Add setter
}
