package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.services.TourValidator;
import at.technikum.tourplanner.utils.EnumStringConverter;
import at.technikum.tourplanner.utils.TransportType;
import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import static at.technikum.tourplanner.TourPlannerApplication.i18n;

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
  protected ChoiceBox<TransportType> transportType;

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

    tourValidator = new TourValidator(i18n);

    if (tourViewModel == null) {
      tourViewModel = new TourViewModel();
    }

    ObservableList<TransportType> transportTypes = FXCollections.observableArrayList(TransportType.values());
    transportType.setItems(transportTypes);
    transportType.setConverter(new EnumStringConverter<>(TransportType.class));

    // Create two-way bindings between UI elements and viewModel properties
    name.textProperty().bindBidirectional(tourViewModel.nameProperty());
    description.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
    from.textProperty().bindBidirectional(tourViewModel.fromProperty());
    to.textProperty().bindBidirectional(tourViewModel.toProperty());
    transportType.valueProperty().bindBidirectional(tourViewModel.transport_typeProperty());

    // Fix the way okCancelController is obtained
    okCancelController = (OKCancelButtonBarController)
            newCancelButtonBar.getProperties().get("okCancelButtonBarController");

//    okCancelController.setOkButtonListener(event -> {
//      // Only when Save is clicked, copy values from UI to model
//      try {
//        tourViewModel.setName(name.getText());
//        tourViewModel.setDescription(description.getText());
//        tourViewModel.setFrom(from.getText());
//        tourViewModel.setTo(to.getText());
//        tourViewModel.setTransportType(transportType.getValue());
//
//        if (tourValidator.validateTour(tourViewModel)) {
//          onSaveButtonClicked();
//        }
//      } catch (Exception e) {
//        tourValidator.showValidationError(List.of("Invalid input format"));
//        log.error("Error saving tour", e);
//      }
//    });

    okCancelController.setCancelButtonListener(event ->
            TourPlannerApplication.closeWindow(newCancelButtonBar));

    initialized = true;
  }

  protected abstract void onSaveButtonClicked();
}