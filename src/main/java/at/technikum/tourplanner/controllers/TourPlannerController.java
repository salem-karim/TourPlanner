package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Slf4j
public class TourPlannerController implements Initializable {

  private final TourTableViewModel tourTableViewModel = new TourTableViewModel();
  private final ResourceBundle i18n = TourPlannerApplication.i18n;

  @FXML
  public SplitPane tourInfo;
  @FXML
  private ButtonBar newEditDeleteButtonBar;
  @FXML
  private AnchorPane tourLogs;
  @FXML
  private MenuItem quitButton;
  @FXML
  private ListView<String> toursListView;

  private TourInfoController tourInfoController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    toursListView.setItems(tourTableViewModel.getDataNames());
    toursListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    toursListView.getSelectionModel().select(0);
    initializeTourInfo();

    toursListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null && newVal.intValue() >= 0) {
        tourTableViewModel.setSelectedTour(tourTableViewModel.getData().get(newVal.intValue()));
      }
    });

    NewEditDeleteButtonBarController newEditDeleteButtonBarController = (NewEditDeleteButtonBarController)
            newEditDeleteButtonBar.getProperties().get("newEditDeleteButtonBarController");
    newEditDeleteButtonBarController.setTourListView(toursListView);
    newEditDeleteButtonBarController.setNewButtonListener(event -> onNewButtonClicked());
    newEditDeleteButtonBarController.setEditButtonListener(event -> onEditButtonClicked());
    newEditDeleteButtonBarController.setDeleteButtonListener(event -> onDeleteButtonClicked());

    Platform.runLater(() -> {
      ButtonBar logsButtonBar = (ButtonBar) tourLogs.lookup("#newEditDeleteButtonBar");
      if (logsButtonBar != null) {
        var logButtonBarController = (NewEditDeleteButtonBarController)
                logsButtonBar.getProperties().get("newEditDeleteButtonBarController");
        logButtonBarController.setNewButtonListener(event -> onLogsNewButtonClicked());
        logButtonBarController.setEditButtonListener(event -> onLogsEditButtonClicked());
        logButtonBarController.setDeleteButtonListener(event -> onLogsDeleteButtonClicked());
      }
    });

//    tourTableViewModel.selectedTourProperty().bind(toursListView.getSelectionModel().selectedItemProperty());
    quitButton.setOnAction(event -> TourPlannerApplication.closeWindow(newEditDeleteButtonBar));
  }

  private void initializeTourInfo() {
    // Get the controller that was created by fx:include
    log.info("SplitPane properties: " + tourInfo.getProperties());

    tourInfoController = (TourInfoController) tourInfo.getProperties().get("fx:controller");

    // Update the TourInfoController when a new tour is selected
    toursListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        tourTableViewModel.getData().stream()
                .filter(tour -> tour.getName().equals(newVal))
                .findFirst()
                .ifPresent(selectedTour -> tourInfoController.setTourViewModel(selectedTour));
      }
    });

    // Initialize with the first tour if available
    if (!tourTableViewModel.getData().isEmpty()) {
      String firstTourName = toursListView.getItems().getFirst();
      tourTableViewModel.getData().stream()
              .filter(tour -> tour.getName().equals(firstTourName))
              .findFirst()
              .ifPresent(firstTour -> tourInfoController.setTourViewModel(firstTour));
    }
  }

  private void onDeleteButtonClicked() {
    // Get selected indices
    var selectedIndices = new ArrayList<>(toursListView.getSelectionModel().getSelectedIndices());

    if (selectedIndices.isEmpty()) {
      log.warn("No tours selected for deletion");
      return;
    }

    // Show confirmation alert
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(i18n.getString("delete.confirmation.title"));
    alert.setHeaderText(i18n.getString("delete.confirmation.header"));
    alert.setContentText(i18n.getString("delete.confirmation.content"));

    var result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      // Delete selected tours in reverse order
      selectedIndices.sort((a, b) -> b - a);
      for (int index : selectedIndices) {
        tourTableViewModel.deleteTour(index);
      }
      toursListView.setItems(tourTableViewModel.getDataNames());
    }
  }


  private void onLogsDeleteButtonClicked() {
    log.info("Logs delete button clicked");
  }

  private void onLogsEditButtonClicked() {
    log.info("Logs edit button clicked");
  }

  private void onLogsNewButtonClicked() {
    log.info("Logs new button clicked");
  }


  private void onEditButtonClicked() {
    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/tourplanner/edit_tours.fxml"), i18n);

      int selectedIndex = toursListView.getSelectionModel().getSelectedIndex();
      TourViewModel selectedTour = tourTableViewModel.getData().get(selectedIndex);

      EditTourController controller = EditTourController.builder()
              .tourTableViewModel(tourTableViewModel)
              .mainLabel(new Label(i18n.getString("editTour.edit")))
              .tourViewModel(selectedTour)
              .toursListView(toursListView)
              .build();
      loader.setController(controller);
      final Parent root = loader.load();
      controller.initialize();
      controller.getOkCancelController().getOkButton().setText(i18n.getString("button.save"));
      final Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle("Edit Tour");
      stage.show();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private void onNewButtonClicked() {
    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/tourplanner/edit_tours.fxml"), i18n);
      NewTourController controller = NewTourController.builder()
              .tourTableViewModel(tourTableViewModel)
              .mainLabel(new Label(i18n.getString("editTour.new")))
              .tourViewModel(new TourViewModel())
              .toursListView(toursListView)
              .build();
      loader.setController(controller);
      final Parent root = loader.load();
      controller.initialize();
      controller.getOkCancelController().getOkButton().setText(i18n.getString("button.new"));
      final Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle("Create New Tour");
      stage.show();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}