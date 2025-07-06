package at.technikum.frontend.BL.services;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

import at.technikum.frontend.BL.utils.AppProperties;
import at.technikum.frontend.BL.utils.RequestHandler;
import at.technikum.frontend.BL.utils.RouteInfo;
import at.technikum.frontend.PL.controllers.BaseTourController;
import at.technikum.frontend.PL.viewmodels.TourViewModel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class TourValidator extends Validator {
  private BaseTourController controller;

  private Optional<double[]> fromCoordsOpt = Optional.empty();
  private Optional<double[]> toCoordsOpt = Optional.empty();

  /**
   * @param controller The controller that manages the tour view.
   */
  public TourValidator(BaseTourController controller) {
    this.controller = controller;
    setupFocusListeners();
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
    final String to = tourViewModel.getTo();
    if (isEmpty(to)) {
      controller.getToError().setText(AppProperties.getInstance().get("validation.to.required"));
      showError(controller.getTo(), controller.getToError());
      toCoordsOpt = Optional.empty(); // clear on error
      return false;
    }

    toCoordsOpt = RequestHandler.getInstance().getCoordinates(to);
    if (toCoordsOpt.isEmpty()) {
      controller.getToError().setText(AppProperties.getInstance().get("validation.to.location"));
      showError(controller.getTo(), controller.getToError());
      return false;
    }
    if (validateRouteIfBothCoordsPresent(tourViewModel)) 
      return false;
    hideError(controller.getTo(), controller.getToError());
    return true;
  }

  /**
   * @param tourViewModel The view model containing the tour data to validate.
   * @return true if the 'from' field is valid, false otherwise.
   */
  private boolean validateFrom(TourViewModel tourViewModel) {
    final String from = tourViewModel.getFrom();
    if (isEmpty(from)) {
      controller.getFromError().setText(AppProperties.getInstance().get("validation.from.required"));
      showError(controller.getFrom(), controller.getFromError());
      fromCoordsOpt = Optional.empty(); // clear on error
      return false;
    }

    fromCoordsOpt = RequestHandler.getInstance().getCoordinates(from);
    if (fromCoordsOpt.isEmpty()) {
      controller.getFromError().setText(AppProperties.getInstance().get("validation.from.location"));
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

    // Transport type validation
    controller.getTransportType().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateTransportType(controller.getTourViewModel());
      }
    });
  }

  private boolean validateRouteIfBothCoordsPresent(TourViewModel tourViewModel) {
    if (tourViewModel.getDistance() > 0 && tourViewModel.getEstimatedTime() > 0) {
      return false; // Assume route is already valid
    }
    if (fromCoordsOpt.isPresent() && toCoordsOpt.isPresent()) {
      if (tourViewModel.getTransportType() == null)
        return true;

      JsonNode route = RequestHandler.getInstance().RouteBetween(fromCoordsOpt, toCoordsOpt,
          tourViewModel.getTransportType());
      Optional<RouteInfo> routeInfoOpt = RequestHandler.getInstance().parseRouteInfo(route);
      if (routeInfoOpt.isEmpty() || route == null) {
        controller.getToError().setText(AppProperties.getInstance().get("validation.route"));
        showError(controller.getTo(), controller.getToError());
        showError(controller.getFrom(), controller.getFromError());
        return true;
      }

      // Route is valid → update view model and hide route errors
      tourViewModel.setDistance(routeInfoOpt.get().distance());
      tourViewModel.setEstimatedTime(routeInfoOpt.get().duration());
      hideError(controller.getTo(), controller.getToError());
      hideError(controller.getFrom(), controller.getFromError());
      return false;
    }
    return true;
  }
}
