package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.viewmodels.LogsTableViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
public class TourLogsController implements Initializable {

  @FXML
  private TableColumn rating;
  @FXML
  private TableColumn totalTime;
  @FXML
  private TableColumn totalDistance;
  @FXML
  private TableColumn difficulty;
  @FXML
  private TableColumn comment;
  @FXML
  private TableColumn date;
  @FXML
  private TableView logTable;
  @FXML
  private ButtonBar newEditDeleteButtonBar;

  private LogsTableViewModel logsTableViewModel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    logsTableViewModel = new LogsTableViewModel();
    logTable.setItems(logsTableViewModel.getData());
    rating.setCellFactory(new PropertyValueFactory<>("rating"));
    totalTime.setCellFactory(new PropertyValueFactory<>("totalTime"));
    totalDistance.setCellFactory(new PropertyValueFactory<>("totalDistance"));
    difficulty.setCellFactory(new PropertyValueFactory<>("difficulty"));
    comment.setCellFactory(new PropertyValueFactory<>("comment"));
    date.setCellFactory(new PropertyValueFactory<>("date"));
    logTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    logTable.getSelectionModel().select(0);

    NewEditDeleteButtonBarController newEditDeleteButtonBarController = (NewEditDeleteButtonBarController)
            newEditDeleteButtonBar.getProperties().get("newEditDeleteButtonBarController");
    newEditDeleteButtonBarController.setLogTableView(logTable);
    newEditDeleteButtonBarController.setNewButtonListener((event) -> onNewButtonClicked());
    newEditDeleteButtonBarController.setEditButtonListener((event) -> onEditButtonClicked());
    newEditDeleteButtonBarController.setDeleteButtonListener((event) -> onDeleteButtonClicked());
  }

  private void onDeleteButtonClicked() {
    log.info("Logs delete button clicked");
  }

  private void onEditButtonClicked() {
    log.info("Logs edit button clicked");
  }

  private void onNewButtonClicked() {
    log.info("Logs new button clicked");
  }
}
