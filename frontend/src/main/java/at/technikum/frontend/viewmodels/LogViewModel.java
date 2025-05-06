package at.technikum.frontend.viewmodels;

import at.technikum.common.models.Logs;
import javafx.beans.property.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public class LogViewModel {
  // TODO: change the Date to LocalDateTime
  private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDateTime> startDateTime = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDateTime> endDateTime = new SimpleObjectProperty<>();
  private final ObjectProperty<TourViewModel> tour = new SimpleObjectProperty<>();
  private final StringProperty comment = new SimpleStringProperty();
  private final IntegerProperty difficulty = new SimpleIntegerProperty();
  private final IntegerProperty totalDistance = new SimpleIntegerProperty();
  private final IntegerProperty rating = new SimpleIntegerProperty();

  public LogViewModel(final Logs log) {
    this.id.set(log.getId());
    this.startDateTime.set(log.getStart_date_time());
    this.endDateTime.set(log.getEnd_date_time());
    this.tour.set(new TourViewModel(log.getTour()));
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
    this.startDateTime.set(log.getStartDateTime());
    this.endDateTime.set(log.getEndDateTime());
    this.tour.set(log.getTour());
  }

  public Logs toLog() {
    Logs log = new Logs();
    log.setId(this.getId());
    log.setStart_date_time(this.getStartDateTime());
    log.setEnd_date_time(this.getEndDateTime());
    log.setComment(this.commentProperty().get());
    log.setDifficulty(this.difficultyProperty().get());
    log.setTotal_distance(this.totalDistanceProperty().get());
    log.setRating(this.ratingProperty().get());
    log.setTour(this.tour.get().toTour());
    return log;
  }


  // Value getters
  public UUID getId() {
    return id.get();
  }

  public LocalDateTime getStartDateTime() {
    return startDateTime.get();
  }

  public LocalDateTime getEndDateTime() {
    return endDateTime.get();
  }

  public TourViewModel getTour() {
    return tour.get();
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

  public int getRating() {
    return rating.get();
  }

  // Property getters
  public ObjectProperty<UUID> idProperty() {
    return id;
  }

  public ObjectProperty<LocalDateTime> startDateProperty() {
    return startDateTime;
  }

  public ObjectProperty<LocalDateTime> endDateProperty() {
    return endDateTime;
  }
  
  public ObjectProperty<TourViewModel> tourProperty() {
    return tour;
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

  public IntegerProperty ratingProperty() {
    return rating;
  }

  public void setStartDateTime(LocalDateTime date) {
    this.startDateTime.set(date);
  }

  public void setEndDateTime(LocalDateTime date) {
    this.endDateTime.set(date);
  }
  
  public void setTour(TourViewModel tour) {
    this.tour.set(tour);
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

  public void setRating(int rating) {
    this.rating.set(rating);
  }
}