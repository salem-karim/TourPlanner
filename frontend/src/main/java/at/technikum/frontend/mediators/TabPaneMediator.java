package at.technikum.frontend.mediators;

import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;

import java.util.Map;

public class TabPaneMediator implements Mediator {
  private final Map<SelectionState, Boolean> buttonStates;

  public TabPaneMediator(TabPane pane, ListView<TourViewModel> tourListView,
                         Map<SelectionState, Boolean> buttonStates) {
    validateButtonStates(buttonStates);
    this.buttonStates = buttonStates;

    updateButtonState(pane, tourListView.getSelectionModel().getSelectedItems().size());

    tourListView.getSelectionModel().getSelectedItems().addListener(
            (ListChangeListener<TourViewModel>) change ->
                    updateButtonState(pane, tourListView.getSelectionModel().getSelectedItems().size()));

    tourListView.getItems().addListener(
            (ListChangeListener<TourViewModel>) change ->
                    updateButtonState(pane, tourListView.getSelectionModel().getSelectedItems().size()));
  }

  private void updateButtonState(TabPane pane, int selectedCount) {
    SelectionState state = SelectionState.fromCount(selectedCount);
    pane.setDisable(!buttonStates.get(state));
  }
}
