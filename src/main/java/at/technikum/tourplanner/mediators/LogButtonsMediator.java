package at.technikum.tourplanner.mediators;

import at.technikum.tourplanner.viewmodels.LogViewModel;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class LogButtonsMediator implements Mediator {
  public LogButtonsMediator(Button deleteButton, TableView<LogViewModel> logTableView, boolean[] booleans) {
    logTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<LogViewModel>) change -> {
      int selectedCount = logTableView.getSelectionModel().getSelectedItems().size();
      deleteButton.setDisable(((selectedCount == 0) && (booleans[0])) || ((selectedCount == 1) && (booleans[1])) || ((selectedCount > 1) && (booleans[2])));
    });
  }
}
