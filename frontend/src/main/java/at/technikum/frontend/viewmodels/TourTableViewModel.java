package at.technikum.frontend.viewmodels;

import at.technikum.common.models.Tour;
import at.technikum.common.models.TransportType;
import at.technikum.frontend.utils.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

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


  public void newTour(TourViewModel tvm) {
    data.add(tvm); // local list

    // Convert to Tour (model from common)
    Tour tour = tvm.toTour();

    RequestHandler.postTour(tour);
  }

  public void updateTour(TourViewModel updatedViewModel) {
    // Update local data (UI)
    for (final TourViewModel tour : data) {
      if (tour.getId().equals(updatedViewModel.getId())) {
        tour.nameProperty().set(updatedViewModel.nameProperty().get());
        tour.fromProperty().set(updatedViewModel.fromProperty().get());
        tour.toProperty().set(updatedViewModel.toProperty().get());
        tour.transport_typeProperty().set(updatedViewModel.transport_typeProperty().get());
        // tour.distanceProperty().set(updatedViewModel.distanceProperty().get());
        // tour.estimated_timeProperty().set(updatedViewModel.estimated_timeProperty().get());
        // tour.route_infoProperty().set(updatedViewModel.route_infoProperty().get());
        break;
      }
    }

    RequestHandler.putTour(updatedViewModel);
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

  public TourTableViewModel() {
    // Add sample logs to each tour
    for (TourViewModel tour : data) {
      addSampleLogsToTour(tour);
    }
  }

  private void addSampleLogsToTour(TourViewModel tour) {
    // Create first sample log
    LogViewModel log1 = new LogViewModel();
    log1.idProperty().set(UUID.randomUUID());
    log1.dateProperty().set(LocalDate.now().minusDays(5));
    log1.commentProperty().set("Great weather, enjoyed the hike!");
    log1.difficultyProperty().set(3);
    log1.totalDistanceProperty().set(8);
    log1.totalTimeProperty().set(120);
    log1.ratingProperty().set(4);

    // Create second sample log
    LogViewModel log2 = new LogViewModel();
    log2.idProperty().set(UUID.randomUUID());
    log2.dateProperty().set(LocalDate.now().minusDays(2));
    log2.commentProperty().set("Rainy day but still fun");
    log2.difficultyProperty().set(4);
    log2.totalDistanceProperty().set(8);
    log2.totalTimeProperty().set(140);
    log2.ratingProperty().set(3);

    // Add logs to the tour
    tour.getLogs().newLog(log1);
    tour.getLogs().newLog(log2);
  }
}
