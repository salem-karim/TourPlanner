package at.technikum.frontend.services;

import at.technikum.frontend.controllers.BaseTourController;
import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.beans.binding.Bindings;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class TourValidator extends Validator {
  private BaseTourController controller;

  /**
   * @param controller The controller that manages the tour view.
   */
  public TourValidator(BaseTourController controller) {
    this.controller = controller;
    setupFocusListeners();
    setupButtonValidation();
  }

  /**
   * @param tourViewModel The view model containing the tour data to validate.
   * @return true if the tour is valid, false otherwise.
   */
  public boolean validateTour(final TourViewModel tourViewModel) {
    errorCountProperty.set(0);
    boolean isValid = false;

    // Validate each field
    isValid |= validateName(tourViewModel);
    isValid &= validateDescription(tourViewModel);
    isValid &= validateFrom(tourViewModel);
    isValid &= validateTo(tourViewModel);
    isValid &= validateTransportType(tourViewModel);

    return isValid;
  }

  /**
   * @param tourViewModel The view model containing the tour data to validate.
   * @return true if the transport type is valid, false otherwise.
   */
  private boolean validateTransportType(TourViewModel tourViewModel) {
    if (tourViewModel.getTransportType() == null) {
      showError(controller.getTransportType(), controller.getTransportTypeError());
      return false;
    }
    hideError(controller.getTransportType(), controller.getTransportTypeError());
    return true;
  }

  /**
   * @param tourViewModel The view model containing the tour data to validate.
   * @return true if the 'to' field is valid, false otherwise.
   */
  private boolean validateTo(TourViewModel tourViewModel) {
    // TODO: Use the API to actually check Location
    if (isEmpty(tourViewModel.getTo())) {
      showError(controller.getTo(), controller.getToError());
      return false;
    }
    hideError(controller.getTo(), controller.getToError());
    return true;
  }

  /**
   * @param tourViewModel The view model containing the tour data to validate.
   * @return true if the 'from' field is valid, false otherwise.
   */
  private boolean validateFrom(TourViewModel tourViewModel) {
    // TODO: Use the API to actually check Location
    if (isEmpty(tourViewModel.getFrom())) {
      showError(controller.getFrom(), controller.getFromError());
      return false;
    }
    hideError(controller.getFrom(), controller.getFromError());
    return true;
  }

  /**
   * @param tourViewModel The view model containing the tour data to validate.
   * @return true if the description is valid, false otherwise.
   */
  private boolean validateDescription(TourViewModel tourViewModel) {
    if (isEmpty(tourViewModel.getDescription())) {
      showError(controller.getDescription(), controller.getDescriptionError());
      return false;
    }
    hideError(controller.getDescription(), controller.getDescriptionError());
    return true;
  }

  /**
   * @param tourViewModel The view model containing the tour data to validate.
   * @return true if the name is valid, false otherwise.
   */
  private boolean validateName(TourViewModel tourViewModel) {
    if (isEmpty(tourViewModel.getName())) {
      showError(controller.getName(), controller.getNameError());
      return false;
    }
    hideError(controller.getName(), controller.getNameError());
    return true;
  }

  /**
   * Sets up focus listeners for all input fields to validate on focus lost
   */
  private void setupFocusListeners() {
    // Name field validation
    controller.getName().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateName(controller.getTourViewModel());
      }
    });

    // Description field validation
    controller.getDescription().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateDescription(controller.getTourViewModel());
      }
    });

    // From field validation
    controller.getFrom().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateFrom(controller.getTourViewModel());
      }
    });

    // To field validation
    controller.getTo().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateTo(controller.getTourViewModel());
      }
    });

    // Transport type validation
    controller.getTransportType().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateTransportType(controller.getTourViewModel());
      }
    });
  }

  /**
   * Setup the OK Button disable Property binding
   */
  private void setupButtonValidation() {
    // Create a binding that disables the button when errorCount > 0
    controller.getOkCancelController().getOkButton().disableProperty().bind(
        Bindings.createBooleanBinding(
            () -> errorCountProperty.get() > 0,
            errorCountProperty));
  }
}
