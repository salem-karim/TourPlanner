package at.technikum.tourplanner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Logs {
    private int id;
    private String date_time;
    private String comment;
    private int difficulty;
    private int total_distance;
    private int total_time;
    private int rating;

}


