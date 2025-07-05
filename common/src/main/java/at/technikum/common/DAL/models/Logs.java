package at.technikum.common.DAL.models;

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

  @Column(name = "tour_id", nullable = false)
  private UUID tour_id;

  @Column(name = "start_date_time", nullable = false)
  private LocalDateTime start_date_time;

  @Column(name = "end_date_time", nullable = false)
  private LocalDateTime end_date_time;

  @Column(name = "distance_km")
  private double total_distance;

  @Column(name = "comment", nullable = false)
  private String comment;

  @Column(name = "difficulty", nullable = false)
  private int difficulty;

  @Column(name = "rating", nullable = false)
  private int rating;
}
