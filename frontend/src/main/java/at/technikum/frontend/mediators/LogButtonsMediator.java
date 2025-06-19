package at.technikum.frontend.mediators;

import at.technikum.frontend.viewmodels.LogViewModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class LogButtonsMediator implements Mediator {
  public LogButtonsMediator(final Button deleteButton, final TableView<LogViewModel> logTableView,
      final boolean[] booleans) {
    logTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<LogViewModel>) change -> {
      final int selectedCount = logTableView.getSelectionModel().getSelectedItems().size();
      deleteButton.setDisable(((selectedCount == 0) && (booleans[0])) || ((selectedCount == 1) && (booleans[1]))
          || ((selectedCount > 1) && (booleans[2])));
    });
  }
}
