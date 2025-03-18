package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import lombok.Builder;

import java.net.URL;
import java.util.ResourceBundle;

@Builder
public class NewTourController implements Initializable {

  @FXML
  private Label mainLabel;

  @FXML
  private Button saveButton;

  @FXML
  private Button cancelButton;

  @FXML
  private TextField name, description, from, to, transportType, distance, duration;

  private TourTableViewModel tourTableViewModel;
  private TourViewModel tourViewModel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    cancelButton.setOnAction(event -> TourPlannerApplication.closeWindow(cancelButton));
    saveButton.setOnAction(this::onSaveButtonClicked);
    name.textProperty().bindBidirectional(tourViewModel.nameProperty());
    description.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
    from.textProperty().bindBidirectional(tourViewModel.fromProperty());
    to.textProperty().bindBidirectional(tourViewModel.toProperty());
    transportType.textProperty().bindBidirectional(tourViewModel.transport_typeProperty());
    distance.textProperty().bindBidirectional(tourViewModel.distanceProperty(), new NumberStringConverter());
    duration.textProperty().bindBidirectional(tourViewModel.estimated_timeProperty(), new NumberStringConverter());
  }

  private void onSaveButtonClicked(ActionEvent actionEvent) {
    tourTableViewModel.newTour(tourViewModel);
    System.out.println(tourTableViewModel.getDataNames());
  }
}
