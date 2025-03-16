package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    newButton.setOnAction(event -> onNewButtonClicked());
    editButton.setOnAction(event -> onEditButtonClicked());
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
      e.printStackTrace();
    }
  }


  private void onNewButtonClicked() {
    try {
      final FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit_tours.fxml"), i18n);
      loader.setController(new NewTourController());
      final Parent root = loader.load();
      EditTourController controller = loader.getController();
      controller.getMainLabel().setText(i18n.getString("editTour.new"));
      final Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setTitle("Create New Tour");
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}