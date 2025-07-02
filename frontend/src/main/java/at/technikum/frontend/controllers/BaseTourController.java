package at.technikum.frontend.controllers;

import at.technikum.common.models.TransportType;
import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.services.TourValidator;
import at.technikum.frontend.utils.AppProperties;
import at.technikum.frontend.viewmodels.TourTableViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Getter
@NoArgsConstructor
@Slf4j
public abstract class BaseTourController {
  @FXML
  protected Label mainLabel, nameError, descriptionError, fromError, toError, transportTypeError;
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
  @Builder.Default
  private boolean initialized = false;

  public void initialize() {
    if (initialized) {
      return;
    }

    tourValidator = new TourValidator(this);

    if (tourViewModel == null) {
      tourViewModel = new TourViewModel();
    }

    transportType.setItems(FXCollections.observableArrayList(TransportType.values()));
    transportType.setConverter(new StringConverter<>() {
      @Override
      public String toString(final TransportType type) {
        if (type == null)
          return "";
        return AppProperties.getInstance().getI18n().getString("tourInfo.transportType." + type.name().toLowerCase());
      }

      @Override
      public TransportType fromString(final String string) {
        return null; // Not needed for now
      }
    });

    // Create two-way bindings between UI elements and viewModel properties
    name.textProperty().bindBidirectional(tourViewModel.nameProperty());
    description.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
    from.textProperty().bindBidirectional(tourViewModel.fromProperty());
    to.textProperty().bindBidirectional(tourViewModel.toProperty());
    transportType.valueProperty().bindBidirectional(tourViewModel.transport_typeProperty());

    // Fix the way okCancelController is obtained
    okCancelController = (OKCancelButtonBarController) newCancelButtonBar.getProperties()
        .get("okCancelButtonBarController");

    okCancelController.setOkButtonListener(event -> {
      if (tourValidator.validateTour(tourViewModel)) {
        onSaveButtonClicked();
      }
    });

    okCancelController.setCancelButtonListener(event -> TourPlannerApplication.closeWindow(newCancelButtonBar));

    initialized = true;
  }

  protected abstract void onSaveButtonClicked();
}
