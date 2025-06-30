package at.technikum.frontend.mediators;

import at.technikum.frontend.viewmodels.LogViewModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.util.Map;

public class LogButtonsMediator implements Mediator {
  private final Map<SelectionState, Boolean> buttonStates;

  public LogButtonsMediator(Button button, TableView<LogViewModel> logTableView,
                            Map<SelectionState, Boolean> buttonStates) {
    validateButtonStates(buttonStates);
    this.buttonStates = buttonStates;

    updateButtonState(button, logTableView.getSelectionModel().getSelectedItems().size());

    logTableView.getSelectionModel().getSelectedItems().addListener(
            (ListChangeListener<LogViewModel>) change ->
                    updateButtonState(button, logTableView.getSelectionModel().getSelectedItems().size()));

    logTableView.getItems().addListener(
            (ListChangeListener<LogViewModel>) change ->
                    updateButtonState(button, logTableView.getSelectionModel().getSelectedItems().size()));
  }

  private void updateButtonState(Button button, int selectedCount) {
    SelectionState state = SelectionState.fromCount(selectedCount);
    button.setDisable(!buttonStates.get(state));
  }
}
