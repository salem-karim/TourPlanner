package at.technikum.frontend.controllers;

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

import static at.technikum.frontend.utils.Localization.i18n;

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
    startDate.setCellValueFactory(cellData -> {
      LogViewModel log = cellData.getValue();
      if (log.getStartDate() != null && log.getStartTime() != null) {
        return new SimpleObjectProperty<>(LocalDateTime.of(log.getStartDate(), log.getStartTime()));
      }
      return new SimpleObjectProperty<>(null);
    });
    startDate.setCellFactory(column -> setupDateCellFactory());

    endDate.setCellValueFactory(cellData -> {
      LogViewModel log = cellData.getValue();
      if (log.getEndDate() != null && log.getEndTime() != null) {
        return new SimpleObjectProperty<>(LocalDateTime.of(log.getEndDate(), log.getEndTime()));
      }
      return new SimpleObjectProperty<>(null);
    });
    endDate.setCellFactory(column -> setupDateCellFactory());

    comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
    totalDistance.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
    rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

    logTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    logTable.getSelectionModel().selectFirst();
  }

  private TableCell<LogViewModel, LocalDateTime> setupDateCellFactory() {
    return new TableCell<>() {
      private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

      @Override
      protected void updateItem(LocalDateTime item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          // Combine LocalDate and LocalTime into a formatted string
          LogViewModel log = getTableView().getItems().get(getIndex());
          if (log != null) {
            LocalDate date = log.getStartDate(); // or getEndDate() for end column
            LocalTime time = log.getStartTime(); // or getEndTime() for end column
            if (date != null && time != null) {
              setText(formatter.format(LocalDateTime.of(date, time)));
            } else {
              setText(null);
            }
          }
        }
      }
    };
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
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/edit_logs.fxml"), i18n);
      NewLogController controller = NewLogController.builder()
              .logTableViewModel(selectedTour.getLogs())
              .selectedTour(selectedTour)
              .mainLabel(new Label(i18n.getString("editLog.new")))
              .logViewModel(new LogViewModel())
              .build();
      loader.setController(controller);

      final Parent root = loader.load();
      final Stage stage = new Stage();
      stage.setTitle(i18n.getString("editLog.new"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(logTable.getScene().getWindow());
      stage.setScene(new Scene(root));

      controller.initialize();
      controller.okCancelController.getOkButton().setText(i18n.getString("button.create"));
      controller.getMainLabel().setText(i18n.getString("editLog.new"));

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
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/edit_logs.fxml"), i18n);
      EditLogController controller = EditLogController.builder()
              .logTableViewModel(selectedTour.getLogs())
              .selectedTour(selectedTour)
              .originalLogViewModel(selectedLog)
              .logViewModel(new LogViewModel(selectedLog))
              .build();
      loader.setController(controller);

      final Parent root = loader.load();
      final Stage stage = new Stage();

      stage.setTitle(i18n.getString("editLog.edit"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(logTable.getScene().getWindow());
      stage.setScene(new Scene(root));

      controller.initialize();
      controller.okCancelController.getOkButton().setText(i18n.getString("button.save"));
      controller.getMainLabel().setText(i18n.getString("editLog.edit"));

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