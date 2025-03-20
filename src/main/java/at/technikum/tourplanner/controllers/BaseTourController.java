package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.services.TourValidator;
import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public abstract class BaseTourController {
  @FXML
  protected Label mainLabel;
  @FXML
  protected ButtonBar newCancelButtonBar;
  @FXML
  protected TextField name, description, from, to;
  @FXML
  protected ChoiceBox<String> transportType;

  protected OKCancelButtonBarController okCancelController;
  protected TourValidator validator;

  protected TourTableViewModel tourTableViewModel;
  protected TourViewModel tourViewModel;
  protected ListView<TourViewModel> toursListView;

  // Flag to prevent duplicate initialization
  private boolean initialized = false;

  public void initialize() {
    // Prevent duplicate initialization
    if (initialized) {
      return;
    }

    validator = new TourValidator(TourPlannerApplication.i18n);

    // Clear and populate transport types from i18n
    transportType.getItems().clear();
    transportType.getItems().addAll(
            TourPlannerApplication.i18n.getString("tourInfo.transportType.car"),
            TourPlannerApplication.i18n.getString("tourInfo.transportType.bike"),
            TourPlannerApplication.i18n.getString("tourInfo.transportType.foot"),
            TourPlannerApplication.i18n.getString("tourInfo.transportType.bus"),
            TourPlannerApplication.i18n.getString("tourInfo.transportType.train")
    );

    name.textProperty().bindBidirectional(tourViewModel.nameProperty());
    description.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
    from.textProperty().bindBidirectional(tourViewModel.fromProperty());
    to.textProperty().bindBidirectional(tourViewModel.toProperty());
    transportType.valueProperty().bindBidirectional(tourViewModel.transport_typeProperty());

    okCancelController = (OKCancelButtonBarController)
            newCancelButtonBar.getProperties().get("okCancelButtonBarController");

    // Add validation before saving
    okCancelController.setOkButtonListener(event -> {
      if (validator.validateTour(tourViewModel)) {
        onSaveButtonClicked();
      }
    });

    okCancelController.setCancelButtonListener(event ->
            TourPlannerApplication.closeWindow(newCancelButtonBar));

    initialized = true;
  }

  protected abstract void onSaveButtonClicked();
}