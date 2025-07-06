package at.technikum.frontend.BL.services;

import java.util.Arrays;
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
  public TourValidator(final BaseTourController controller) {
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
  private boolean validateTransportType(final TourViewModel tourViewModel) {
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
  private boolean validateTo(final TourViewModel tourViewModel) {
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
  private boolean validateFrom(final TourViewModel tourViewModel) {
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
  private boolean validateDescription(final TourViewModel tourViewModel) {
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
  private boolean validateName(final TourViewModel tourViewModel) {
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
  
  private boolean validateCoordinates(double[] coords) {
    return coords == null || coords.length != 2
            || !(coords[0] >= -180) || !(coords[0] <= 180)  // longitude
            || !(coords[1] >= -90) || !(coords[1] <= 90);   // latitude
}

  /**
   * @param tourViewModel The Tour View Model to be validated
   * @return true when there is an Issue and false when everything went right
   */
public boolean validateRouteIfBothCoordsPresent(final TourViewModel tourViewModel) {
    if (fromCoordsOpt.isPresent() && toCoordsOpt.isPresent()) {
        double[] fromCoords = fromCoordsOpt.get();
        double[] toCoords = toCoordsOpt.get();

        // Validate coordinate ranges
        if (validateCoordinates(fromCoords) || validateCoordinates(toCoords)) {
            log.error("Invalid coordinates: from {} to {}", 
                Arrays.toString(fromCoords), 
                Arrays.toString(toCoords));
            showRouteValidationError();
            return true;
        }

        if (tourViewModel.getTransportType() == null)
            return true;

      log.info("Requesting route from {} to {} with transport type {}",
              Arrays.toString(fromCoordsOpt.get()),
              Arrays.toString(toCoordsOpt.get()),
              tourViewModel.getTransportType());

      final JsonNode route = RequestHandler.getInstance().RouteBetween(
              fromCoordsOpt, toCoordsOpt, tourViewModel.getTransportType());

      if (route == null) {
        log.error("Route request returned null");
        showRouteValidationError();
        return true;
      }

      log.info("Received route response: {}", route);
      // Geometry decoding check (used by Leaflet polyline)
      final JsonNode geometry = route.get("routes").get(0).get("geometry");
      if (geometry == null || geometry.isNull() || !geometry.isTextual()) {
        showRouteValidationError();
        return true;
      }

      final Optional<RouteInfo> routeInfoOpt = RequestHandler.getInstance().parseRouteInfo(route);
      if (routeInfoOpt.isEmpty()) {
        showRouteValidationError();
        return true;
      }

      // ✅ Route is valid
      tourViewModel.setDistance(routeInfoOpt.get().distance());
      tourViewModel.setEstimatedTime(routeInfoOpt.get().duration());
      hideError(controller.getTo(), controller.getToError());
      hideError(controller.getFrom(), controller.getFromError());
      controller.setRouteJson(route);
      return false;
    }
   
  // If we reach here, either coords are missing or transport type is not set
    showRouteValidationError();
    return true;
  }

  /**
   * Sets the ErrorLabels to the correct Text and shows them
   */
  private void showRouteValidationError() {
    final String msg = AppProperties.getInstance().get("validation.route");
    controller.getToError().setText(msg);
    controller.getFromError().setText(msg);
    showError(controller.getTo(), controller.getToError());
    showError(controller.getFrom(), controller.getFromError());
  }

  public boolean validateRouteOnly(final TourViewModel tourViewModel) {
    errorCountProperty.set(0);

    boolean isValid = true;

    isValid &= validateTransportType(tourViewModel);
    isValid &= validateFrom(tourViewModel);
    isValid &= validateTo(tourViewModel);


    return isValid;
  }
}
