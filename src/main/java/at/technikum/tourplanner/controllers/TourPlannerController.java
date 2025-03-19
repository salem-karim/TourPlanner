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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    toursListView.setItems(tourTableViewModel.getDataNames());
    NewEditDeleteButtonBarController newEditDeleteButtonBarController = (NewEditDeleteButtonBarController)
            newEditDeleteButtonBar.getProperties().get("newEditDeleteButtonBarController");
    newEditDeleteButtonBarController.setNewButtonListener(event -> onNewButtonClicked());
    newEditDeleteButtonBarController.setEditButtonListener(event -> onEditButtonClicked());
    newEditDeleteButtonBarController.setDeleteButtonListener(event -> {
      tourTableViewModel.deleteTour(toursListView.getSelectionModel().getSelectedIndex());
      toursListView.setItems(tourTableViewModel.getDataNames());
    });

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
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_tours.fxml"), i18n);
      loader.setController(new EditTourController());
      final Parent root = loader.load();
      EditTourController controller = loader.getController();
      controller.getMainLabel().setText(i18n.getString("editTour.edit"));
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
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_tours.fxml"), i18n);
      loader.setController(NewTourController.builder()
              .tourTableViewModel(tourTableViewModel)
              .mainLabel(new Label(i18n.getString("editTour.edit")))
              .tourViewModel(new TourViewModel())
              .toursListView(toursListView)
              .build());
      final Parent root = loader.load();
      final Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle("Create New Tour");
      stage.show();
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}