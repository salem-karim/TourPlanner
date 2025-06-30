package at.technikum.common.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tour_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Logs {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "tour_id")
  private UUID tour_id;

  @Column(name = "start_date_time")
  private LocalDateTime start_date_time;

  @Column(name = "end_date_time")
  private LocalDateTime end_date_time;

  @Column(name = "distance_km")
  private double total_distance;

  private String comment;

  private int difficulty;

  private int rating;
}
