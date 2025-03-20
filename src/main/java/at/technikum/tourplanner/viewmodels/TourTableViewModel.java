package at.technikum.tourplanner.viewmodels;

import at.technikum.tourplanner.models.Tour;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TourTableViewModel {
  private final ObjectProperty<TourViewModel> selectedTour = new SimpleObjectProperty<>();
  private final ObservableList<TourViewModel> data =
          FXCollections.observableArrayList(
                  new TourViewModel(new Tour(UUID.randomUUID(), "Kahlsberg Wanderung", "Schöne Wanderung aufm Kahlsberg", "Nußdorf", "Kahlsberg", "zu Fuß", 5, 2, "img")),
                  new TourViewModel(new Tour(UUID.randomUUID(), "Schneeberg Wanderung", "Schöne Wanderung aufm Schneeberg", "Schneeberg Startpunkt", "Schneeberg spitze", "zu Fuß", 15, 5, "img"))

          );

//  public ObservableList<String> getDataNames() {
//    return FXCollections.observableArrayList(data.stream().map(TourViewModel::getName).toList());
//  }

  public void newTour(TourViewModel tvm) {
    data.add(tvm);
  }

  public void updateTour(TourViewModel tourViewModel) {
    for (final TourViewModel tour : data) {
      if (tour.getId().equals(tourViewModel.getId())) {
        tour.nameProperty().set(tourViewModel.nameProperty().get());
        tour.distanceProperty().set(tourViewModel.distanceProperty().get());
        tour.fromProperty().set(tourViewModel.fromProperty().get());
        tour.toProperty().set(tourViewModel.toProperty().get());
        tour.transport_typeProperty().set(tourViewModel.transport_typeProperty().get());
        tour.distanceProperty().set(tourViewModel.distanceProperty().get());
        tour.estimated_timeProperty().set(tourViewModel.estimated_timeProperty().get());
        tour.route_infoProperty().set(tourViewModel.route_infoProperty().get());
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
}
