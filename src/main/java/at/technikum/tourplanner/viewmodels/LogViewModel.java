package at.technikum.tourplanner.viewmodels;

import at.technikum.tourplanner.models.Logs;
import javafx.beans.property.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class LogViewModel {
  private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
  private final StringProperty dateTime = new SimpleStringProperty();
  private final StringProperty comment = new SimpleStringProperty();
  private final IntegerProperty difficulty = new SimpleIntegerProperty();
  private final IntegerProperty totalDistance = new SimpleIntegerProperty();
  private final IntegerProperty totalTime = new SimpleIntegerProperty();
  private final IntegerProperty rating = new SimpleIntegerProperty();

  public LogViewModel(final Logs log) {
    this.id.set(log.getId());
    this.dateTime.set(log.getDate_time());
    this.comment.set(log.getComment());
    this.difficulty.set(log.getDifficulty());
    this.totalDistance.set(log.getTotal_distance());
    this.totalTime.set(log.getTotal_time());
    this.rating.set(log.getRating());
  }

  public LogViewModel(final LogViewModel log) {
    this.id.set(log.getId());
    this.dateTime.set(log.getDateTime());
    this.comment.set(log.getComment());
    this.difficulty.set(log.getDifficulty());
    this.totalDistance.set(log.getTotalDistance());
    this.totalTime.set(log.getTotalTime());
    this.rating.set(log.getRating());
  }

  // Value getters
  public UUID getId() {
    return id.get();
  }

  public String getDateTime() {
    return dateTime.get();
  }

  public String getComment() {
    return comment.get();
  }

  public int getDifficulty() {
    return difficulty.get();
  }

  public int getTotalDistance() {
    return totalDistance.get();
  }

  public int getTotalTime() {
    return totalTime.get();
  }

  public int getRating() {
    return rating.get();
  }

  // Property getters
  public ObjectProperty<UUID> idProperty() {
    return id;
  }

  public StringProperty dateTimeProperty() {
    return dateTime;
  }

  public StringProperty commentProperty() {
    return comment;
  }

  public IntegerProperty difficultyProperty() {
    return difficulty;
  }

  public IntegerProperty totalDistanceProperty() {
    return totalDistance;
  }

  public IntegerProperty totalTimeProperty() {
    return totalTime;
  }

  public IntegerProperty ratingProperty() {
    return rating;
  }
}