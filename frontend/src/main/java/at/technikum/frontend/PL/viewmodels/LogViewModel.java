package at.technikum.frontend.PL.viewmodels;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import at.technikum.common.DAL.models.Logs;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LogViewModel {
  private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalTime> endTime = new SimpleObjectProperty<>();
  private final ObjectProperty<UUID> tourId = new SimpleObjectProperty<>();
  private final StringProperty comment = new SimpleStringProperty();
  private final IntegerProperty difficulty = new SimpleIntegerProperty();
  private final DoubleProperty totalDistance = new SimpleDoubleProperty();
  private final IntegerProperty rating = new SimpleIntegerProperty();

  public LogViewModel(final Logs log) {
    this.id.set(log.getId());
    this.startDate.set(log.getStart_date_time().toLocalDate());
    this.startTime.set(log.getStart_date_time().toLocalTime());
    this.endDate.set(log.getEnd_date_time().toLocalDate());
    this.endTime.set(log.getEnd_date_time().toLocalTime());
    this.tourId.set(log.getTour_id());
    this.comment.set(log.getComment());
    this.difficulty.set(log.getDifficulty());
    this.totalDistance.set(log.getTotal_distance());
    this.rating.set(log.getRating());
  }

  public LogViewModel(final LogViewModel log) {
    this.id.set(log.getId());
    this.comment.set(log.getComment());
    this.difficulty.set(log.getDifficulty());
    this.totalDistance.set(log.getTotalDistance());
    this.rating.set(log.getRating());
    this.startDate.set(log.getStartDate());
    this.startTime.set(log.getStartTime());
    this.endDate.set(log.getEndDate());
    this.endTime.set(log.getEndTime());
    this.tourId.set(log.getTourId());
  }

  public Logs toLog() {
    final Logs log = new Logs();
    log.setId(this.getId());
    log.setStart_date_time(LocalDateTime.of(getStartDate(), getStartTime()));
    log.setEnd_date_time(LocalDateTime.of(getEndDate(), getEndTime()));
    log.setComment(this.commentProperty().get());
    log.setDifficulty(this.difficultyProperty().get());
    log.setTotal_distance(this.totalDistanceProperty().get());
    log.setRating(this.ratingProperty().get());
    log.setTour_id(this.getTourId());
    return log;
  }

  // Value getters
  public UUID getId() {
    return id.get();
  }

  public LocalDate getStartDate() {
    return startDate.get();
  }

  public LocalTime getStartTime() {
    return startTime.get();
  }

  public LocalDate getEndDate() {
    return endDate.get();
  }

  public LocalTime getEndTime() {
    return endTime.get();
  }

  public UUID getTourId() {
    return tourId.get();
  }

  public String getComment() {
    return comment.get();
  }

  public int getDifficulty() {
    return difficulty.get();
  }

  public double getTotalDistance() {
    return totalDistance.get();
  }

  public int getRating() {
    return rating.get();
  }

  // Property getters
  public ObjectProperty<UUID> idProperty() {
    return id;
  }

  public ObjectProperty<LocalDate> startDateProperty() {
    return startDate;
  }

  public ObjectProperty<LocalTime> startTimeProperty() {
    return startTime;
  }

  public ObjectProperty<LocalDate> endDateProperty() {
    return endDate;
  }

  public ObjectProperty<LocalTime> endTimeProperty() {
    return endTime;
  }

  public ObjectProperty<UUID> tourIdProperty() {
    return tourId;
  }

  public StringProperty commentProperty() {
    return comment;
  }

  public IntegerProperty difficultyProperty() {
    return difficulty;
  }

  public DoubleProperty totalDistanceProperty() {
    return totalDistance;
  }

  public IntegerProperty ratingProperty() {
    return rating;
  }

  public void setStartDate(final LocalDate date) {
    this.startDate.set(date);
  }

  public void setStartTime(final LocalTime time) {
    this.startTime.set(time);
  }

  public void setEndDate(final LocalDate date) {
    this.endDate.set(date);
  }

  public void setEndTime(final LocalTime time) {
    this.endTime.set(time);
  }

  public void setTour(final UUID tourId) {
    this.tourId.set(tourId);
  }

  public void setComment(final String comment) {
    this.comment.set(comment);
  }

  public void setDifficulty(final int difficulty) {
    this.difficulty.set(difficulty);
  }

  public void setTotalDistance(final double totalDistance) {
    this.totalDistance.set(totalDistance);
  }

  public void setRating(final int rating) {
    this.rating.set(rating);
  }
}
