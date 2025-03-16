package at.technikum.tourplanner.viewmodels;

import at.technikum.tourplanner.models.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.UUID;

public class TourTableViewModel {

    private final ObservableList<TourViewModel> data =
            FXCollections.observableArrayList(
                    new TourViewModel(new Tour(UUID.randomUUID(), "Kahlsberg Wanderung", "Schöne Wanderung aufm Kahlsberg", "Nußdorf", "Kahlsberg", "zu Fuß", 5, 2, "img")),
                    new TourViewModel(new Tour(UUID.randomUUID(), "Kahlsberg Wanderung", "Schöne Wanderung aufm Kahlsberg", "Nußdorf", "Kahlsberg", "zu Fuß", 5, 2, "img"))

                    );

    public ObservableList<TourViewModel> getData() {
        return data;
    }

    // test values
    public ObservableList<String> getDataNames() {
        ObservableList<String> items = FXCollections.observableArrayList(
                "Item 1", "Item 2", "Item 3", "Item 4"
        );
        return items;
    }


    public void newTour(TourViewModel svm) {
        data.add(svm);
    }

    public void updateTour(TourViewModel studentViewModel) {
        for (TourViewModel tour : data) {
            if (tour.getId().equals(studentViewModel.getId())) {
                tour.nameProperty().set(studentViewModel.nameProperty().get());
                tour.tour_distanceProperty().set(studentViewModel.tour_distanceProperty().get());
                tour.fromProperty().set(studentViewModel.fromProperty().get());
                tour.toProperty().set(studentViewModel.toProperty().get());
                tour.transport_typeProperty().set(studentViewModel.transport_typeProperty().get());
                tour.tour_distanceProperty().set(studentViewModel.tour_distanceProperty().get());
                tour.estimated_timeProperty().set(studentViewModel.estimated_timeProperty().get());
                tour.route_infoProperty().set(studentViewModel.route_infoProperty().get());
            }
        }
    }
    public void deleteTour(TourViewModel studentViewModel) {
        data.remove(studentViewModel);
        // sonstiges??
    }
}
