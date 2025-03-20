package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseTourController {
  @FXML
  protected Label mainLabel;
  @FXML
  protected ButtonBar newCancelButtonBar;
  @FXML
  protected TextField name, description, from, to, transportType, distance, duration;

  protected OKCancelButtonBarController okCancelController;

  protected TourTableViewModel tourTableViewModel;
  protected TourViewModel tourViewModel;
  protected ListView<String> toursListView;

  public void initialize() {
    name.textProperty().bindBidirectional(tourViewModel.nameProperty());
    description.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
    from.textProperty().bindBidirectional(tourViewModel.fromProperty());
    to.textProperty().bindBidirectional(tourViewModel.toProperty());
    transportType.textProperty().bindBidirectional(tourViewModel.transport_typeProperty());
    distance.textProperty().bindBidirectional(tourViewModel.distanceProperty(), new NumberStringConverter());
    duration.textProperty().bindBidirectional(tourViewModel.estimated_timeProperty(), new NumberStringConverter());

    okCancelController = (OKCancelButtonBarController)
            newCancelButtonBar.getProperties().get("okCancelButtonBarController");
    okCancelController.setOkButtonListener(event -> onSaveButtonClicked());
    okCancelController.setCancelButtonListener(event -> TourPlannerApplication.closeWindow(newCancelButtonBar));
  }

  protected abstract void onSaveButtonClicked();
}