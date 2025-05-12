package at.technikum.frontend.viewmodels;

import at.technikum.common.models.Tour;
import at.technikum.common.models.TransportType;
import at.technikum.frontend.utils.RequestHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * ViewModel for the TourTableView.
 * This class is responsible for managing the data and logic of the TourTableView.
 * It handles the creation, updating, and deletion of tours.
 */
@Getter
public class TourTableViewModel {

  private final ObjectProperty<TourViewModel> selectedTour = new SimpleObjectProperty<>();
  private final ObservableList<TourViewModel> data = FXCollections.observableArrayList(
          new TourViewModel(
                  new Tour(
                          UUID.randomUUID(),
                          "Kahlsberg Wanderung",
                          "Schöne Wanderung aufm Kahlsberg",
                          "Nußdorf",
                          "Kahlsberg",
                          TransportType.CAR,
                          100,
                          120,
                          new byte[0],
                          new ArrayList<>())),
          new TourViewModel(
                  new Tour(
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


  public TourTableViewModel() {
     // Touren direkt beim Initialisieren laden
    if (System.getProperty("app.test") == null) loadTours();
    // Add sample logs to each tour
    for (TourViewModel tour : data) {
      addSampleLogsToTour(tour);
    }
  }

  public void newTour(TourViewModel tvm) {
    data.add(tvm); // local list

    // Convert to Tour (model from common)
    Tour tour = tvm.toTour();

    RequestHandler.postTour(tour);
  }

  public void updateTour(final TourViewModel otherViewModel, final TourViewModel tourViewModel) {
    // Update local data (UI)
    tourViewModel.setName(otherViewModel.getName());
    tourViewModel.setDescription(otherViewModel.getDescription());
    tourViewModel.setFrom(otherViewModel.getFrom());
    tourViewModel.setTo(otherViewModel.getTo());
    tourViewModel.setTransportType(otherViewModel.getTransportType());
//    tourViewModel.setDistance(otherViewModel.getDistance());
//    tourViewModel.setEstimatedTime(otherViewModel.getEstimatedTime());
//    tourViewModel.setRouteInfo(otherViewModel.getRouteInfo());

    RequestHandler.putTour(tourViewModel);
  }


  public void deleteTour(TourViewModel tourViewModel) {
    data.remove(tourViewModel);

    RequestHandler.deleteTour(tourViewModel.getId());
  }

  public void deleteTour(int index) {
    TourViewModel removed = data.remove(index);
    if (removed != null) {
      RequestHandler.deleteTour(removed.getId());
    }
  }

  public void setSelectedTour(TourViewModel tour) {
    selectedTour.set(tour);
  }



  private void addSampleLogsToTour(TourViewModel tour) {
    // Create first sample log
    LogViewModel log1 = new LogViewModel();
    log1.idProperty().set(UUID.randomUUID());
    log1.startDateProperty().set(LocalDateTime.now().minusDays(5));
    log1.endDateProperty().set(LocalDateTime.now().minusDays(4));
    log1.commentProperty().set("Great weather, enjoyed the hike!");
    log1.difficultyProperty().set(3);
    log1.totalDistanceProperty().set(8);
    log1.ratingProperty().set(4);

    // Create second sample log
    LogViewModel log2 = new LogViewModel();
    log2.idProperty().set(UUID.randomUUID());
    log2.startDateProperty().set(LocalDateTime.now().minusDays(8));
    log2.endDateProperty().set(LocalDateTime.now().minusDays(6));
    log2.commentProperty().set("Rainy day but still fun");
    log2.difficultyProperty().set(4);
    log2.totalDistanceProperty().set(8);
    log2.ratingProperty().set(3);

    // Add logs to the tour
    tour.getLogs().newLog(log1);
    tour.getLogs().newLog(log2);
  }

  public void loadTours() {
    RequestHandler.loadTours(tourList -> {
      for (Tour tour : tourList) {
        TourViewModel tvm = new TourViewModel(tour);
        data.add(tvm);
      }
    });
  }
}
