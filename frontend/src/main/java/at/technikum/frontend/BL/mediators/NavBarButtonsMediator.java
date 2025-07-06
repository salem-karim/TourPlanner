package at.technikum.frontend.BL.mediators;

import at.technikum.frontend.PL.viewmodels.TourViewModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

import java.util.Map;

public class NavBarButtonsMediator implements Mediator {
  private final Map<SelectionState, Boolean> buttonStates;

  public NavBarButtonsMediator(MenuItem button, ListView<TourViewModel> tourListView,
                               Map<SelectionState, Boolean> buttonStates) {
    validateButtonStates(buttonStates);
    this.buttonStates = buttonStates;

    updateButtonState(button, tourListView.getSelectionModel().getSelectedItems().size());

    tourListView.getSelectionModel().getSelectedItems().addListener(
            (ListChangeListener<TourViewModel>)
                    change -> updateButtonState(button, tourListView.getSelectionModel().getSelectedItems().size()));
    tourListView.getItems().addListener(
            (ListChangeListener<TourViewModel>)
                    change -> updateButtonState(button, tourListView.getSelectionModel().getSelectedItems().size()));
   
  }

  private void updateButtonState(MenuItem button, int selectedCount) {
    SelectionState state = SelectionState.fromCount(selectedCount);
    button.setDisable(!buttonStates.get(state));
  }
}
