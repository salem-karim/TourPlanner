package at.technikum.tourplanner.viewmodels;

import at.technikum.tourplanner.models.Logs;
import javafx.beans.property.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public class LogViewModel {
  private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
  private final StringProperty comment = new SimpleStringProperty();
  private final IntegerProperty difficulty = new SimpleIntegerProperty();
  private final IntegerProperty totalDistance = new SimpleIntegerProperty();
  private final IntegerProperty totalTime = new SimpleIntegerProperty();
  private final IntegerProperty rating = new SimpleIntegerProperty();

  public LogViewModel(final Logs log) {
    this.id.set(log.getId());
    this.date.set(log.getDate_time());
    this.comment.set(log.getComment());
    this.difficulty.set(log.getDifficulty());
    this.totalDistance.set(log.getTotal_distance());
    this.totalTime.set(log.getTotal_time());
    this.rating.set(log.getRating());
  }

  public LogViewModel(final LogViewModel log) {
    this.id.set(log.getId());
    this.date.set(log.getDate());
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

  public LocalDate getDate() {
    return date.get();
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

  public ObjectProperty<LocalDate> dateProperty() {
    return date;
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

  public void setDate(LocalDateTime date) {
    this.date.set(LocalDate.from(date));
  }

  public void setComment(String comment) {
    this.comment.set(comment);
  }

  public void setDifficulty(int difficulty) {
    this.difficulty.set(difficulty);
  }

  public void setTotalDistance(int totalDistance) {
    this.totalDistance.set(totalDistance);
  }

  public void setTotalTime(int totalTime) {
    this.totalTime.set(totalTime);
  }

  public void setRating(int rating) {
    this.rating.set(rating);
  }
}