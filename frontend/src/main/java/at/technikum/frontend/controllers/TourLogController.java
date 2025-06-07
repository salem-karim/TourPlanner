package at.technikum.frontend.controllers;

import at.technikum.frontend.utils.AppProperties;
import at.technikum.frontend.viewmodels.LogViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Slf4j
public class TourLogController implements Initializable {
  @FXML
  private TableColumn<LogViewModel, Integer> rating;
  @FXML
  private TableColumn<LogViewModel, Double> totalDistance;
  @FXML
  private TableColumn<LogViewModel, Integer> difficulty;
  @FXML
  private TableColumn<LogViewModel, String> comment;
  @FXML
  private TableColumn<LogViewModel, LocalDateTime> startDate, endDate;
  @FXML
  private ButtonBar newEditDeleteButtonBar;
  @Getter
  @FXML
  private TableView<LogViewModel> logTable;

  @Setter
  private TourViewModel selectedTour;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setupTableColumns();
    setupButtonBar();

  }

  private void setupTableColumns() {
    // Set up date columns using the reusable method
    setupDateTimeColumn(startDate, true);
    setupDateTimeColumn(endDate, false);

    // Other column setup remains the same
    comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
    totalDistance.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
    rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

    logTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    logTable.getSelectionModel().selectFirst();
  }

  private void setupDateTimeColumn(TableColumn<LogViewModel, LocalDateTime> column,
                                   boolean isStart) {
    column.setCellValueFactory(cellData -> {
      LogViewModel log = cellData.getValue();
      return new SimpleObjectProperty<>() {
        {
          // Create listeners for both date and time properties
          if (isStart) {
            log.startDateProperty().addListener((obs, oldVal, newVal) ->
                    refreshValue(log.getStartDate(), log.getStartTime()));
            log.startTimeProperty().addListener((obs, oldVal, newVal) ->
                    refreshValue(log.getStartDate(), log.getStartTime()));
          } else {
            log.endDateProperty().addListener((obs, oldVal, newVal) ->
                    refreshValue(log.getEndDate(), log.getEndTime()));
            log.endTimeProperty().addListener((obs, oldVal, newVal) ->
                    refreshValue(log.getEndDate(), log.getEndTime()));
          }

          // Set initial value
          LocalDate date = isStart ? log.getStartDate() : log.getEndDate();
          LocalTime time = isStart ? log.getStartTime() : log.getEndTime();
          refreshValue(date, time);
        }

        private void refreshValue(LocalDate date, LocalTime time) {
          if (date != null && time != null) {
            set(LocalDateTime.of(date, time));
          } else {
            set(null);
          }
        }
      };
    });

    column.setCellFactory(col -> new TableCell<>() {
      private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

      @Override
      protected void updateItem(LocalDateTime dateTime, boolean empty) {
        super.updateItem(dateTime, empty);
        setText(empty || dateTime == null ? null : formatter.format(dateTime));
      }
    });
  }


  private void setupButtonBar() {
    NewEditDeleteButtonBarController controller = (NewEditDeleteButtonBarController)
            newEditDeleteButtonBar.getProperties().get("newEditDeleteButtonBarController");

    controller.setLogTableView(logTable);
    controller.setNewButtonListener(event -> onNewButtonClicked());
    controller.setEditButtonListener(event -> onEditButtonClicked());
    controller.setDeleteButtonListener(event -> onDeleteButtonClicked());
    controller.getNewButton().setId("newLogButton");
    controller.getEditButton().setId("editLogButton");
    controller.getDeleteButton().setId("deleteLogButton");
    logTable.setId("logTable");
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
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/edit_logs.fxml"), AppProperties.getInstance().getI18n());
      NewLogController controller = NewLogController.builder()
              .logTableViewModel(selectedTour.getLogs())
              .selectedTour(selectedTour)
              .mainLabel(new Label(AppProperties.getInstance().getI18n().getString("editLog.new")))
              .logViewModel(new LogViewModel())
              .build();
      loader.setController(controller);

      final Parent root = loader.load();
      final Stage stage = new Stage();
      stage.setTitle(AppProperties.getInstance().getI18n().getString("editLog.new"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(logTable.getScene().getWindow());
      stage.setScene(new Scene(root));

      controller.initialize();
      controller.okCancelController.getOkButton().setText(AppProperties.getInstance().getI18n().getString("button.create"));
      controller.getMainLabel().setText(AppProperties.getInstance().getI18n().getString("editLog.new"));

      stage.showAndWait();
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
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/edit_logs.fxml"), AppProperties.getInstance().getI18n());
      EditLogController controller = EditLogController.builder()
              .logTableViewModel(selectedTour.getLogs())
              .selectedTour(selectedTour)
              .originalLogViewModel(selectedLog)
              .logViewModel(new LogViewModel(selectedLog))
              .build();
      loader.setController(controller);

      final Parent root = loader.load();
      final Stage stage = new Stage();

      stage.setTitle(AppProperties.getInstance().getI18n().getString("editLog.edit"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(logTable.getScene().getWindow());
      stage.setScene(new Scene(root));

      controller.initialize();
      controller.okCancelController.getOkButton().setText(AppProperties.getInstance().getI18n().getString("button.save"));
      controller.getMainLabel().setText(AppProperties.getInstance().getI18n().getString("editLog.edit"));

      stage.showAndWait();
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
    alert.setTitle(AppProperties.getInstance().getI18n().getString("delete.confirmation.title"));
    alert.setHeaderText(AppProperties.getInstance().getI18n().getString("delete.confirmation.header"));
    alert.setContentText(AppProperties.getInstance().getI18n().getString("delete.confirmation.content"));

    var result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      selectedIndices.sort((a, b) -> b - a);
      for (int index : selectedIndices) {
        selectedTour.getLogs().deleteLog(index);
      }
    }
  }
}