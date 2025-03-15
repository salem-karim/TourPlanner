package at.technikum.tourplanner.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Logs {
    private int id;
    private String date_time;
    private String comment;
    private int difficulty;
    private int total_distance;
    private int total_time;
    private int rating;

    public Logs(int id, String date_time, String comment, int difficulty, int total_distance, int total_time, int rating) {
        this.id = id;
        this.date_time = date_time;
        this.comment = comment;
        this.difficulty = difficulty;
        this.total_distance = total_distance;
        this.total_time = total_time;
        this.rating = rating;
    }
}


