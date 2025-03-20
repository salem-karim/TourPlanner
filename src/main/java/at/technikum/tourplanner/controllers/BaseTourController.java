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
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
@Slf4j
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
  protected TourValidator tourValidator;

  protected TourTableViewModel tourTableViewModel;
  protected TourViewModel tourViewModel;
  protected ListView<TourViewModel> toursListView;

  // Flag to prevent duplicate initialization
  private boolean initialized = false;

  public void initialize() {
    if (initialized) {
      return;
    }

    tourValidator = new TourValidator(TourPlannerApplication.i18n);

    if (tourViewModel == null) {
      tourViewModel = new TourViewModel();
    }

    transportType.getItems().addAll(
            TourPlannerApplication.i18n.getString("tourInfo.transportType.car"),
            TourPlannerApplication.i18n.getString("tourInfo.transportType.bike"),
            TourPlannerApplication.i18n.getString("tourInfo.transportType.foot"),
            TourPlannerApplication.i18n.getString("tourInfo.transportType.bus"),
            TourPlannerApplication.i18n.getString("tourInfo.transportType.train")
    );
    // Set field values from tourViewModel (one-way)
    name.setText(tourViewModel.getName());
    description.setText(tourViewModel.getTour_description());
    from.setText(tourViewModel.getFrom());
    to.setText(tourViewModel.getTo());
    transportType.setValue(tourViewModel.getTransport_type());

    // Fix the way okCancelController is obtained
    okCancelController = (OKCancelButtonBarController)
            newCancelButtonBar.getProperties().get("okCancelButtonBarController");

    okCancelController.setOkButtonListener(event -> {
      // Only when Save is clicked, copy values from UI to model
      try {
        tourViewModel.setName(name.getText());
        tourViewModel.setDescription(description.getText());
        tourViewModel.setFrom(from.getText());
        tourViewModel.setTo(to.getText());
        tourViewModel.setTransportType(transportType.getValue());

        if (tourValidator.validateTour(tourViewModel)) {
          onSaveButtonClicked();
        }
      } catch (Exception e) {
        tourValidator.showValidationError(List.of("Invalid input format"));
        log.error("Error saving tour", e);
      }
    });

    okCancelController.setCancelButtonListener(event ->
            TourPlannerApplication.closeWindow(newCancelButtonBar));

    initialized = true;
  }

  protected abstract void onSaveButtonClicked();
}