package at.technikum.tourplanner.viewmodels;

import at.technikum.tourplanner.models.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TourTableViewModel {

  private final ObservableList<TourViewModel> data =
          FXCollections.observableArrayList(
                  new TourViewModel(new Tour(UUID.randomUUID(), "Kahlsberg Wanderung", "Schöne Wanderung aufm Kahlsberg", "Nußdorf", "Kahlsberg", "zu Fuß", 5, 2, "img")),
                  new TourViewModel(new Tour(UUID.randomUUID(), "Kahlsberg Wanderung", "Schöne Wanderung aufm Kahlsberg", "Nußdorf", "Kahlsberg", "zu Fuß", 5, 2, "img"))

          );

  // test values
  public ObservableList<String> getDataNames() {
    return FXCollections.observableArrayList(
            "Item 1", "Item 2", "Item 3", "Item 4"
    );
  }


  public void newTour(TourViewModel tvm) {
    data.add(tvm);
  }

  public void updateTour(TourViewModel tourViewModel) {
    for (final TourViewModel tour : data) {
      if (tour.getId().equals(tourViewModel.getId())) {
        tour.nameProperty().set(tourViewModel.nameProperty().get());
        tour.tour_distanceProperty().set(tourViewModel.tour_distanceProperty().get());
        tour.fromProperty().set(tourViewModel.fromProperty().get());
        tour.toProperty().set(tourViewModel.toProperty().get());
        tour.transport_typeProperty().set(tourViewModel.transport_typeProperty().get());
        tour.tour_distanceProperty().set(tourViewModel.tour_distanceProperty().get());
        tour.estimated_timeProperty().set(tourViewModel.estimated_timeProperty().get());
        tour.route_infoProperty().set(tourViewModel.route_infoProperty().get());
      }
    }
  }

  public void deleteTour(TourViewModel tourViewModel) {
    data.remove(tourViewModel);
    // sonstiges??
  }
}
