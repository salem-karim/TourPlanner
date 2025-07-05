package at.technikum.frontend.PL.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Function;

import at.technikum.frontend.BL.utils.AppProperties;
import at.technikum.frontend.PL.viewmodels.LogViewModel;
import at.technikum.frontend.PL.viewmodels.TourViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

  private record DateTimeAccessors(
      Function<LogViewModel, ObservableValue<LocalDate>> dateProperty,
      Function<LogViewModel, ObservableValue<LocalTime>> timeProperty,
      Function<LogViewModel, LocalDate> dateGetter,
      Function<LogViewModel, LocalTime> timeGetter) {
  }

  @Override
  public void initialize(final URL location, final ResourceBundle resources) {
    setupTableColumns();
    setupButtonBar();

  }

  private void setupTableColumns() {
    setupDateTimeColumn(startDate, new DateTimeAccessors(
        LogViewModel::startDateProperty,
        LogViewModel::startTimeProperty,
        LogViewModel::getStartDate,
        LogViewModel::getStartTime));

    setupDateTimeColumn(endDate, new DateTimeAccessors(
        LogViewModel::endDateProperty,
        LogViewModel::endTimeProperty,
        LogViewModel::getEndDate,
        LogViewModel::getEndTime));

    comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
    difficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
    totalDistance.setCellValueFactory(new PropertyValueFactory<>("totalDistance"));
    rating.setCellValueFactory(new PropertyValueFactory<>("rating"));

    // noinspection deprecation
    logTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    logTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    logTable.getSelectionModel().selectFirst();
  }

  private void setupDateTimeColumn(
      final TableColumn<LogViewModel, LocalDateTime> column,
      final DateTimeAccessors accessors) {
    column.setCellValueFactory(cellData -> {
      final LogViewModel log = cellData.getValue();
      return new SimpleObjectProperty<>() {
        {
          accessors.dateProperty().apply(log)
              .addListener((obs, oldVal, newVal) -> refreshValue(
                  accessors.dateGetter().apply(log),
                  accessors.timeGetter().apply(log)));
          accessors.timeProperty().apply(log)
              .addListener((obs, oldVal, newVal) -> refreshValue(
                  accessors.dateGetter().apply(log),
                  accessors.timeGetter().apply(log)));

          refreshValue(
              accessors.dateGetter().apply(log),
              accessors.timeGetter().apply(log));
        }

        private void refreshValue(final LocalDate date, final LocalTime time) {
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
      protected void updateItem(final LocalDateTime dateTime, final boolean empty) {
        super.updateItem(dateTime, empty);
        setText(empty || dateTime == null ? null : formatter.format(dateTime));
      }
    });
  }

  private void setupButtonBar() {
    final NewEditDeleteButtonBarController controller = (NewEditDeleteButtonBarController) newEditDeleteButtonBar
        .getProperties().get("newEditDeleteButtonBarController");

    controller.setLogTableView(logTable);
    controller.setNewButtonListener(event -> onNewButtonClicked());
    controller.setEditButtonListener(event -> onEditButtonClicked());
    controller.setDeleteButtonListener(event -> onDeleteButtonClicked());
    controller.getNewButton().setId("newLogButton");
    controller.getEditButton().setId("editLogButton");
    controller.getDeleteButton().setId("deleteLogButton");
    logTable.setId("logTable");
  }

  public void updateSelectedTour(final TourViewModel tour) {
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
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/edit_logs.fxml"),
          AppProperties.getInstance().getI18n());
      final NewLogController controller = NewLogController.builder()
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
      controller.okCancelController.getOkButton()
          .setText(AppProperties.getInstance().getI18n().getString("button.create"));
      controller.getMainLabel().setText(AppProperties.getInstance().getI18n().getString("editLog.new"));

      stage.showAndWait();
    } catch (final IOException e) {
      log.error("Failed to open new log dialog", e);
    }
  }

  private void onEditButtonClicked() {
    if (selectedTour == null) {
      log.warn("No tour selected");
      return;
    }

    final LogViewModel selectedLog = logTable.getSelectionModel().getSelectedItem();
    if (selectedLog == null) {
      log.warn("No log selected for editing");
      return;
    }

    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/edit_logs.fxml"),
          AppProperties.getInstance().getI18n());
      final EditLogController controller = EditLogController.builder()
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
      controller.okCancelController.getOkButton()
          .setText(AppProperties.getInstance().getI18n().getString("button.save"));
      controller.getMainLabel().setText(AppProperties.getInstance().getI18n().getString("editLog.edit"));

      stage.showAndWait();
    } catch (final IOException e) {
      log.error("Failed to open edit log dialog", e);
    }
  }

  private void onDeleteButtonClicked() {
    if (selectedTour == null) {
      return;
    }

    final var selectedIndices = new ArrayList<>(logTable.getSelectionModel().getSelectedIndices());
    if (selectedIndices.isEmpty()) {
      return;
    }

    final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(AppProperties.getInstance().getI18n().getString("delete.confirmation.title"));
    alert.setHeaderText(AppProperties.getInstance().getI18n().getString("delete.confirmation.header"));
    alert.setContentText(AppProperties.getInstance().getI18n().getString("delete.confirmation.content"));

    final var result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      selectedIndices.sort((a, b) -> b - a);
      for (final int index : selectedIndices) {
        selectedTour.getLogs().deleteLog(index);
      }
    }
  }
}
