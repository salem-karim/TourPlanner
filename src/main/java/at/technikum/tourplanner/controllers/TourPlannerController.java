package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
  private Button newButton;

  @FXML
  private Button editButton;

  @FXML
  private Button deleteButton;

  @FXML
  private Button generalButton;

  @FXML
  private Button logsButton;

  @FXML
  private ListView<String> toursListView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    toursListView.setItems(tourTableViewModel.getDataNames());
//    tourTableViewModel.selectedTourProperty().bind(toursListView.getSelectionModel().selectedItemProperty());
    newButton.setOnAction(this::onNewButtonClicked);
    editButton.setOnAction(this::onEditButtonClicked);
  }


  private void onEditButtonClicked(final ActionEvent event) {
    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/tourplanner/edit_tours.fxml"), i18n);
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


  private void onNewButtonClicked(final ActionEvent event) {
    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/at/technikum/tourplanner/edit_tours.fxml"), i18n);
      loader.setController(NewTourController.builder()
              .tourTableViewModel(tourTableViewModel)
              .mainLabel(new Label(i18n.getString("editTour.edit")))
              .tourViewModel(new TourViewModel())
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