package at.technikum.frontend.BL.mediators;

import at.technikum.frontend.PL.viewmodels.TourViewModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;

import java.util.Map;

public class TourButtonsMediator implements Mediator {
  private final Map<SelectionState, Boolean> buttonStates;

  public TourButtonsMediator(Control button, ListView<TourViewModel> tourListView,
                             Map<SelectionState, Boolean> buttonStates) {
    validateButtonStates(buttonStates);
    this.buttonStates = buttonStates;

    updateButtonState(button, tourListView.getSelectionModel().getSelectedItems().size());

    tourListView.getSelectionModel().getSelectedItems().addListener(
            (ListChangeListener<TourViewModel>) change ->
                    updateButtonState(button, tourListView.getSelectionModel().getSelectedItems().size()));

    tourListView.getItems().addListener(
            (ListChangeListener<TourViewModel>) change ->
                    updateButtonState(button, tourListView.getSelectionModel().getSelectedItems().size()));
  }

  private void updateButtonState(Control button, int selectedCount) {
    SelectionState state = SelectionState.fromCount(selectedCount);
    button.setDisable(!buttonStates.get(state));
  }
}
