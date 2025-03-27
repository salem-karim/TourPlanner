package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
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
  private MenuItem quitButton;
  @FXML
  private ListView<TourViewModel> toursListView;

  private TourInfoController tourInfoController;

  @FXML
  private TourLogController tourLogsController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeListView();
    initializeTourInfo();

    NewEditDeleteButtonBarController newEditDeleteButtonBarController = (NewEditDeleteButtonBarController) newEditDeleteButtonBar
            .getProperties().get("newEditDeleteButtonBarController");
    newEditDeleteButtonBarController.setTourListView(toursListView);
    newEditDeleteButtonBarController.setNewButtonListener(event -> onNewButtonClicked());
    newEditDeleteButtonBarController.setEditButtonListener(event -> onEditButtonClicked());
    newEditDeleteButtonBarController.setDeleteButtonListener(event -> onDeleteButtonClicked());

    if (tourLogsController != null) {
      tourLogsController.getLogTable().getSelectionModel().selectFirst();
    }
    quitButton.setOnAction(event -> TourPlannerApplication.closeWindow(newEditDeleteButtonBar));
  }

  private void initializeListView() {
    // Keep using TourViewModel objects
    toursListView.setItems(tourTableViewModel.getData());

    // Set a custom cell factory to display only the names
    toursListView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(TourViewModel tour, boolean empty) {
        super.updateItem(tour, empty);

        if (empty || tour == null) {
          setText(null);
        } else {
          setText(tour.getName());
        }
      }
    });

    toursListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    toursListView.getSelectionModel().selectFirst();

    toursListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null && newVal.intValue() >= 0) {
        tourTableViewModel.setSelectedTour(tourTableViewModel.getData().get(newVal.intValue()));
      }
    });

  }

  private void initializeTourInfo() {
    tourInfoController = (TourInfoController) tourInfo.getProperties().get("tourInfoController");

    // Update both controllers when a tour is selected
    toursListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        updateTourDisplays(newVal);
      }
    });

    // Important: Force an initial update if a tour is already selected
    if (!tourTableViewModel.getData().isEmpty()) {
      toursListView.getSelectionModel().select(0);
      TourViewModel firstTour = toursListView.getSelectionModel().getSelectedItem();
      if (firstTour != null) {
        updateTourDisplays(firstTour);
      }
    }
  }

  private void updateTourDisplays(TourViewModel tour) {
    if (tourInfoController != null) {
      tourInfoController.setTourViewModel(tour);
    } else {
      log.error("Tour info controller not initialized");
    }

    if (tourLogsController != null) {
      tourLogsController.updateSelectedTour(tour);
    } else {
      log.error("Tour logs controller not initialized");
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
      toursListView.refresh();
    }
  }

  private void onEditButtonClicked() {
    TourViewModel selectedTour = toursListView.getSelectionModel().getSelectedItem();
    if (selectedTour == null) {
      log.warn("No tour selected for editing");
      return;
    }

    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/tourplanner/edit_tours.fxml"),
              i18n);
      EditTourController controller = EditTourController.builder()
              .tourViewModel(new TourViewModel(selectedTour))
              .originalTourViewModel(selectedTour)
              .toursListView(toursListView)
              .build();
      loader.setController(controller);

      final Parent root = loader.load();
      final Stage stage = new Stage();

      stage.setTitle(i18n.getString("editTour.edit"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(toursListView.getScene().getWindow());
      stage.setScene(new Scene(root));

      controller.okCancelController.getOkButton().setText(i18n.getString("button.save"));
      controller.getMainLabel().setText(i18n.getString("editTour.edit"));

      stage.showAndWait();
    } catch (IOException e) {
      log.error("Failed to open edit tour dialog", e);
    }
  }

  private void onNewButtonClicked() {
    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/tourplanner/edit_tours.fxml"),
              i18n);
      NewTourController controller = NewTourController.builder()
              .tourTableViewModel(tourTableViewModel)
              .tourViewModel(new TourViewModel())
              .toursListView(toursListView)
              .build();
      loader.setController(controller);

      final Parent root = loader.load();
      final Stage stage = new Stage();

      stage.setTitle(i18n.getString("editTour.new"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(toursListView.getScene().getWindow());
      stage.setScene(new Scene(root));

      controller.initialize();
      controller.getOkCancelController().getOkButton().setText(i18n.getString("button.create"));
      controller.getMainLabel().setText(i18n.getString("editTour.new"));

      stage.showAndWait();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
