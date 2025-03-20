package at.technikum.tourplanner.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Logs {
  private UUID id;
  private LocalDate date_time;
  private String comment;
  private int difficulty;
  private int total_distance;
  private int total_time;
  private int rating;

}


