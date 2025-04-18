package at.technikum.frontend.mediators;

import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class TourButtonsMediator implements Mediator {

  public TourButtonsMediator(Button button, ListView<TourViewModel> tourListView, boolean[] disable) {
    tourListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<TourViewModel>) change -> {
      int selectedCount = tourListView.getSelectionModel().getSelectedItems().size();
      button.setDisable(((selectedCount == 0) && (disable[0])) || ((selectedCount == 1) && (disable[1])) || ((selectedCount > 1) && (disable[2])));
    });
  }
}
