package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.LogViewModel;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

@Slf4j
public class TourLogController implements Initializable {
  @FXML
  private TableColumn<LogViewModel, Integer> rating;
  @FXML
  private TableColumn<LogViewModel, Double> totalTime;
  @FXML
  private TableColumn<LogViewModel, Double> totalDistance;
  @FXML
  private TableColumn<LogViewModel, Integer> difficulty;
  @FXML
  private TableColumn<LogViewModel, String> comment;
  @FXML
  private TableColumn<LogViewModel, Date> date;
  @FXML
  private TableView<LogViewModel> logTable;
  @FXML
  private ButtonBar newEditDeleteButtonBar;

  @Setter
  private TourViewModel selectedTour;
  private final ResourceBundle i18n = TourPlannerApplication.i18n;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setupTableColumns();
    setupButtonBar();

  }

  private void setupTableColumns() {
    date.setCellValueFactory(new PropertyValueFactory<>("date"));
    comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
    totalDistance.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
    totalTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));
    rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

    logTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  private void setupButtonBar() {
    NewEditDeleteButtonBarController controller = (NewEditDeleteButtonBarController)
            newEditDeleteButtonBar.getProperties().get("newEditDeleteButtonBarController");

    controller.setLogTableView(logTable);
    controller.setNewButtonListener(event -> onNewButtonClicked());
    controller.setEditButtonListener(event -> onEditButtonClicked());
    controller.setDeleteButtonListener(event -> onDeleteButtonClicked());
  }

  public void updateSelectedTour(TourViewModel tour) {
    this.selectedTour = tour;
    if (tour != null) {
      logTable.setItems(tour.getLogs().getData());
    } else {
      log.info("No tour selected, clearing logs table");
      logTable.getItems().clear();
    }
  }

  private void onNewButtonClicked() {
    if (selectedTour == null) {
      log.warn("No tour selected");
      return;
    }

    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/tourplanner/edit_logs.fxml"), i18n);
      NewLogController controller = NewLogController.builder()
              .logTableViewModel(selectedTour.getLogs())
              .mainLabel(new Label(i18n.getString("editLog.new")))
              .logViewModel(new LogViewModel())
              .build();

      loader.setController(controller);
      final Parent root = loader.load();
      controller.initialize();
      controller.okCancelController.getOkButton().setText(i18n.getString("button.new"));
      controller.getMainLabel().setText(i18n.getString("editLog.new"));
      final Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle(i18n.getString("editLog.new"));
      stage.show();
    } catch (IOException e) {
      log.error("Failed to open new log dialog", e);
    }
  }

  private void onEditButtonClicked() {
    if (selectedTour == null) {
      log.warn("No tour selected");
      return;
    }

    LogViewModel selectedLog = logTable.getSelectionModel().getSelectedItem();
    if (selectedLog == null) {
      log.warn("No log selected for editing");
      return;
    }

    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/tourplanner/edit_logs.fxml"), i18n);
      EditLogController controller = EditLogController.builder()
              .logTableViewModel(selectedTour.getLogs())
              .logViewModel(selectedLog)
              .build();

      loader.setController(controller);
      final Parent root = loader.load();
      controller.initialize();
      controller.okCancelController.getOkButton().setText(i18n.getString("button.save"));
      controller.getMainLabel().setText(i18n.getString("editLog.edit"));
      controller.setupWithLog(selectedLog);
      final Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle(i18n.getString("editLog.edit"));
      stage.show();
    } catch (IOException e) {
      log.error("Failed to open edit log dialog", e);
    }
  }

  private void onDeleteButtonClicked() {
    if (selectedTour == null) {
      return;
    }

    var selectedIndices = new ArrayList<>(logTable.getSelectionModel().getSelectedIndices());
    if (selectedIndices.isEmpty()) {
      return;
    }

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(i18n.getString("delete.confirmation.title"));
    alert.setHeaderText(i18n.getString("delete.confirmation.header"));
    alert.setContentText(i18n.getString("delete.confirmation.content"));

    var result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      selectedIndices.sort((a, b) -> b - a);
      for (int index : selectedIndices) {
        selectedTour.getLogs().deleteLog(index);
      }
    }
  }
}