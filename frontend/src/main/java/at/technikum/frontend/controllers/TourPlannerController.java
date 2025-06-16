package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.utils.AppProperties;
import at.technikum.frontend.viewmodels.TourTableViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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

  @FXML
  public SplitPane tourInfo;
  public AnchorPane tourLogs;
  public MenuItem englishButton, germanButton, polishButton;
  @FXML
  private ButtonBar newEditDeleteButtonBar;
  @FXML
  private MenuItem quitButton;
  @FXML
  private ListView<TourViewModel> tourListView;

  private TourInfoController tourInfoController;

  @FXML
  private TourLogController tourLogsController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeListView();
    initializeTourInfo();

    NewEditDeleteButtonBarController newEditDeleteButtonBarController = (NewEditDeleteButtonBarController) newEditDeleteButtonBar
            .getProperties().get("newEditDeleteButtonBarController");
    newEditDeleteButtonBarController.setTourListView(tourListView);
    newEditDeleteButtonBarController.setNewButtonListener(event -> onNewButtonClicked());
    newEditDeleteButtonBarController.setEditButtonListener(event -> onEditButtonClicked());
    newEditDeleteButtonBarController.setDeleteButtonListener(event -> onDeleteButtonClicked());

    if (tourLogsController != null) {
      tourLogsController.getLogTable().getSelectionModel().selectFirst();
    }
    quitButton.setOnAction(event -> TourPlannerApplication.closeWindow(newEditDeleteButtonBar));
    englishButton.setOnAction(event -> changeLanguage("en"));
    germanButton.setOnAction(event -> changeLanguage("de"));
    polishButton.setOnAction(event -> changeLanguage("pl"));
  }

  private void initializeListView() {
    // Keep using TourViewModel objects
    tourListView.setItems(tourTableViewModel.getData());

    // Set a custom cell factory to display only the names
    tourListView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(TourViewModel tour, boolean empty) {
        super.updateItem(tour, empty);

        if (empty || tour == null) {
          textProperty().unbind();
          setText(null);
        } else {
          textProperty().bind(tour.nameProperty());
        }
      }
    });

    tourListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    tourListView.getSelectionModel().selectFirst();

    tourListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null && newVal.intValue() >= 0) {
        tourTableViewModel.setSelectedTour(tourTableViewModel.getData().get(newVal.intValue()));
      }
    });

  }

  private void initializeTourInfo() {
    tourInfoController = (TourInfoController) tourInfo.getProperties().get("tourInfoController");

    // Update both controllers when a tour is selected
    tourListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        updateTourDisplays(newVal);
      }
    });

    // Important: Force an initial update if a tour is already selected
    if (!tourTableViewModel.getData().isEmpty()) {
      tourListView.getSelectionModel().select(0);
      TourViewModel firstTour = tourListView.getSelectionModel().getSelectedItem();
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
    var selectedIndices = new ArrayList<>(tourListView.getSelectionModel().getSelectedIndices());

    if (selectedIndices.isEmpty()) {
      log.warn("No tours selected for deletion");
      return;
    }

    // Show confirmation alert
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(AppProperties.getInstance().getI18n().getString("delete.confirmation.title"));
    alert.setHeaderText(AppProperties.getInstance().getI18n().getString("delete.confirmation.header"));
    alert.setContentText(AppProperties.getInstance().getI18n().getString("delete.confirmation.content"));

    var result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      // Delete selected tours in reverse order
      selectedIndices.sort((a, b) -> b - a);
      for (int index : selectedIndices) {
        tourTableViewModel.deleteTour(index);
      }
      tourListView.refresh();
    }
  }

  private void onEditButtonClicked() {
    TourViewModel selectedTour = tourListView.getSelectionModel().getSelectedItem();
    if (selectedTour == null) {
      log.warn("No tour selected for editing");
      return;
    }

    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/edit_tours.fxml"),
              AppProperties.getInstance().getI18n());
      EditTourController controller = EditTourController.builder()
              .tourTableViewModel(tourTableViewModel)
              .tourViewModel(new TourViewModel(selectedTour))
              .originalTourViewModel(selectedTour)
              .toursListView(tourListView)
              .build();
      loader.setController(controller);

      final Parent root = loader.load();
      final Stage stage = new Stage();

      stage.setTitle(AppProperties.getInstance().getI18n().getString("editTour.edit"));
      stage.initModality(Modality.WINDOW_MODAL);
      stage.initOwner(tourListView.getScene().getWindow());
      stage.setScene(new Scene(root));

      controller.okCancelController.getOkButton().setText(AppProperties.getInstance().getI18n().getString("button.save"));
      controller.getMainLabel().setText(AppProperties.getInstance().getI18n().getString("editTour.edit"));

      stage.showAndWait();
    } catch (IOException e) {
      log.error("Failed to open edit tour dialog", e);
    }
  }

  private void onNewButtonClicked() {
    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/edit_tours.fxml"),
              AppProperties.getInstance().getI18n());
      NewTourController controller = NewTourController.builder()
              .tourTableViewModel(tourTableViewModel)
              .tourViewModel(new TourViewModel())
              .toursListView(tourListView)
              .build();
      loader.setController(controller);

      final Parent root = loader.load();
      final Stage stage = new Stage();

      stage.setTitle(AppProperties.getInstance().getI18n().getString("editTour.new"));
//      stage.initModality(Modality.WINDOW_MODAL);
//      stage.initOwner(tourListView.getScene().getWindow());
      stage.setScene(new Scene(root));

      controller.initialize();
      controller.getOkCancelController().getOkButton().setText(AppProperties.getInstance().getI18n().getString("button.create"));
      controller.getMainLabel().setText(AppProperties.getInstance().getI18n().getString("editTour.new"));

      stage.showAndWait();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private void changeLanguage(String newLang) {
    if (newLang.equals(AppProperties.getInstance().getLocale().getLanguage())) return;

    // Update the language
    AppProperties.getInstance().setLanguage(newLang);

    try {
      // Reload the entire scene with the new language
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/frontend/main_window.fxml"),
              AppProperties.getInstance().getI18n());
      Parent root = loader.load();

      // Get the current stage
      Stage stage = (Stage) tourListView.getScene().getWindow();

      // Replace the scene content
      Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
      stage.setScene(scene);
    } catch (IOException e) {
      log.error("Failed to reload view after language change", e);
    }
  }
}
