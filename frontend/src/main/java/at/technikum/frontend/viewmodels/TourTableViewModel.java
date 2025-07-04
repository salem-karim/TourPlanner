package at.technikum.frontend.viewmodels;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import at.technikum.common.models.Logs;
import at.technikum.common.models.Tour;
import at.technikum.common.models.TransportType;
import at.technikum.frontend.utils.RequestHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

/**
 * ViewModel for the TourTableView. This class is responsible for managing the
 * data and logic of the TourTableView. It
 * handles the creation, updating, and deletion of tours.
 */
@Getter
public class TourTableViewModel {

  private final ObjectProperty<TourViewModel> selectedTour = new SimpleObjectProperty<>();
  private final ObservableList<TourViewModel> data = FXCollections.observableArrayList();

  /**
   * Loads all Tours (with Logs) or create Sample Data for Tests
   */
  public TourTableViewModel() {
    // Touren direkt beim Initialisieren laden
    if (System.getProperty("app.test") == null) {
      loadTours();
      return;
    }
    data.add(new TourViewModel(new Tour(
        UUID.randomUUID(),
        "Kahlsberg Wanderung",
        "Schöne Wanderung aufm Kahlsberg",
        "Nußdorf",
        "Kahlsberg",
        TransportType.CAR,
        100,
        120,
        new byte[0],
        new ArrayList<>())));

    data.add(new TourViewModel(new Tour(
        UUID.randomUUID(),
        "Schneeberg Wanderung",
        "Schöne Wanderung aufm Schneeberg",
        "Schneeberg Startpunkt",
        "Schneeberg Spitze",
        TransportType.BIKE,
        100,
        120,
        new byte[0],
        new ArrayList<>())));

    // Add sample logs to each tour
    for (final TourViewModel tour : data) {
      addSampleLogsToTour(tour);
    }
  }

  /**
   * @param tvm The View Model to add the List View and post to the API
   */
  public void newTour(final TourViewModel tvm) {
    data.add(tvm);
    final Tour tour = tvm.toTour();
    RequestHandler.getInstance().postTour(tour);
  }

  /**
   * @param otherViewModel of which Members gets copied over
   * @param tourViewModel  of which Members gets written over by the other
   */
  public void updateTour(final TourViewModel otherViewModel, final TourViewModel tourViewModel) {
    // Update local data (UI)
    tourViewModel.setName(otherViewModel.getName());
    tourViewModel.setDescription(otherViewModel.getDescription());
    tourViewModel.setFrom(otherViewModel.getFrom());
    tourViewModel.setTo(otherViewModel.getTo());
    tourViewModel.setTransportType(otherViewModel.getTransportType());
    // tourViewModel.setDistance(otherViewModel.getDistance());
    // tourViewModel.setEstimatedTime(otherViewModel.getEstimatedTime());
    // tourViewModel.setRouteInfo(otherViewModel.getRouteInfo());

    RequestHandler.getInstance().putTour(tourViewModel);
  }

  /**
   * @param tourViewModel to delete
   */
  public void deleteTour(final TourViewModel tourViewModel) {
    data.remove(tourViewModel);

    RequestHandler.getInstance().deleteTour(tourViewModel.getId());
  }

  public void deleteTour(final int index) {
    final TourViewModel removed = data.remove(index);
    if (removed != null) {
      RequestHandler.getInstance().deleteTour(removed.getId());
    }
  }

  /**
   * @param tour to be selected from the List
   */
  public void setSelectedTour(final TourViewModel tour) {
    selectedTour.set(tour);
  }

  /**
   * @param tour The tour which gets Logs inserted into its List of Logs
   *             generates Sample Logs for a tour
   */
  private void addSampleLogsToTour(final TourViewModel tour) {
    // Create first sample log
    final LogViewModel log1 = new LogViewModel();
    log1.idProperty().set(UUID.randomUUID());
    log1.startDateProperty().set(LocalDate.now());
    log1.endDateProperty().set(LocalDate.now().plusDays(1));
    log1.startTimeProperty().set(LocalTime.now());
    log1.endTimeProperty().set(LocalTime.now().plusHours(2));
    log1.commentProperty().set("Great weather, enjoyed the hike!");
    log1.difficultyProperty().set(3);
    log1.totalDistanceProperty().set(8);
    log1.ratingProperty().set(4);

    // Create second sample log
    final LogViewModel log2 = new LogViewModel();
    log2.idProperty().set(UUID.randomUUID());
    log2.startDateProperty().set(LocalDate.now());
    log2.endDateProperty().set(LocalDate.now().plusDays(4));
    log2.startTimeProperty().set(LocalTime.now());
    log2.endTimeProperty().set(LocalTime.now().plusHours(7));
    log2.commentProperty().set("Rainy day but still fun");
    log2.difficultyProperty().set(4);
    log2.totalDistanceProperty().set(8);
    log2.ratingProperty().set(3);

    // Add logs to the tour
    tour.getLogs().newLog(log1);
    tour.getLogs().newLog(log2);
  }

  /**
   * Fetches Tours with all Logs from Backend API
   */
  public void loadTours() {
    // sets callback for the request handler to load tours
    RequestHandler.getInstance().loadTours(tourList -> {
      for (final Tour tour : tourList) {
        final TourViewModel tvm = new TourViewModel(tour);
        data.add(tvm);
      }
    });

    // Load logs for each tour and add to the TourViewModel
    final List<LogViewModel> all_logs = new ArrayList<>(List.of());
    RequestHandler.getInstance().loadLogs(logs -> {
      for (final Logs log : logs) {
        final LogViewModel lvm = new LogViewModel(log);
        all_logs.add(lvm);
      }
    });

    while (!all_logs.isEmpty()) {
      for (final TourViewModel tour : data) {
        for (final LogViewModel log : all_logs) {
          if (tour.getId().equals(log.getTourId())) {
            tour.getLogs().getData().add(log);
            all_logs.remove(log);
            break;
          }
        }
      }
    }
  }

}
