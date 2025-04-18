package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.services.TourValidator;
import at.technikum.frontend.utils.EnumStringConverter;
import at.technikum.frontend.utils.TransportType;
import at.technikum.frontend.viewmodels.TourTableViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import static at.technikum.frontend.TourPlannerApplication.i18n;

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

    okCancelController.setOkButtonListener(event -> {
      if (tourValidator.validateTour(tourViewModel)) {
        onSaveButtonClicked();
      }
    });

    okCancelController.setCancelButtonListener(event ->
            TourPlannerApplication.closeWindow(newCancelButtonBar));

    initialized = true;
  }

  protected abstract void onSaveButtonClicked();
}