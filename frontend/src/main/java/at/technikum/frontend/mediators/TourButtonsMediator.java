package at.technikum.frontend.mediators;

import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.Map;

public class TourButtonsMediator implements Mediator {
  private final Map<SelectionState, Boolean> buttonStates;

  public TourButtonsMediator(Button button, ListView<TourViewModel> tourListView,
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

  private void updateButtonState(Button button, int selectedCount) {
    SelectionState state = SelectionState.fromCount(selectedCount);
    button.setDisable(!buttonStates.get(state));
  }
}
