package at.technikum.frontend.PL.viewmodels;

import at.technikum.common.DAL.models.Tour;
import at.technikum.common.DAL.models.TransportType;
import at.technikum.frontend.BL.utils.RequestHandler;
import at.technikum.frontend.BL.utils.TourUtils;
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

    data.add(new TourViewModel(TourUtils.createSampleTour("Kahlsberg Wanderung", "Schöne Wanderung aufm Kahlsberg",
        "Nußdorf", "Kahlsberg", TransportType.CAR)));
    data.add(new TourViewModel(TourUtils.createSampleTour("Schneeberg Wanderung", "Schöne Wanderung aufm Schneeberg",
        "Schneeberg Startpunkt", "Schneeberg Spitze", TransportType.BIKE)));

    data.forEach(TourUtils::addSampleLogs);
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
    tourViewModel.setDistance(otherViewModel.getDistance());
    tourViewModel.setEstimatedTime(otherViewModel.getEstimatedTime());
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
   * Fetches Tours with all Logs from Backend API
   */
  public void loadTours() {
    TourUtils.loadAllTours(data);
  }

}
