package at.technikum.frontend.viewmodels;

import at.technikum.frontend.models.Tour;
import at.technikum.frontend.utils.TransportType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class TourTableViewModel {
  private final ObjectProperty<TourViewModel> selectedTour = new SimpleObjectProperty<>();
  private final ObservableList<TourViewModel> data =
          FXCollections.observableArrayList(
//                  new TourViewModel(new Tour(UUID.randomUUID(), "Kahlsberg Wanderung", "Schöne Wanderung aufm Kahlsberg", "Nußdorf", "Kahlsberg", "zu Fuß", 5, 2, "img")),
//                  new TourViewModel(new Tour(UUID.randomUUID(), "Schneeberg Wanderung", "Schöne Wanderung aufm Schneeberg", "Schneeberg Startpunkt", "Schneeberg spitze", "zu Fuß", 15, 5, "img"))
                  new TourViewModel(new Tour(UUID.randomUUID(), "Kahlsberg Wanderung", "Schöne Wanderung aufm Kahlsberg", "Nußdorf", "Kahlsberg", TransportType.BIKE)),
                  new TourViewModel(new Tour(UUID.randomUUID(), "Schneeberg Wanderung", "Schöne Wanderung aufm Schneeberg", "Schneeberg Startpunkt", "Schneeberg spitze", TransportType.FOOT))
          );

  public void newTour(TourViewModel tvm) {
    data.add(tvm);
  }

  public void updateTour(TourViewModel tourViewModel) {
    for (final TourViewModel tour : data) {
      if (tour.getId().equals(tourViewModel.getId())) {
        tour.nameProperty().set(tourViewModel.nameProperty().get());
        tour.fromProperty().set(tourViewModel.fromProperty().get());
        tour.toProperty().set(tourViewModel.toProperty().get());
        tour.transport_typeProperty().set(tourViewModel.transport_typeProperty().get());
//        tour.distanceProperty().set(tourViewModel.distanceProperty().get());
//        tour.estimated_timeProperty().set(tourViewModel.estimated_timeProperty().get());
//        tour.route_infoProperty().set(tourViewModel.route_infoProperty().get());
      }
    }
  }

  public void deleteTour(TourViewModel tourViewModel) {
    data.remove(tourViewModel);
    // sonstiges??
  }

  public void deleteTour(int index) {
    data.remove(index);
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
