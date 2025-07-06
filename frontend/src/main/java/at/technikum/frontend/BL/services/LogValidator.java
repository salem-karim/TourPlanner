package at.technikum.frontend.BL.services;

import java.time.LocalTime;

import at.technikum.frontend.PL.controllers.BaseLogController;
import at.technikum.frontend.BL.utils.AppProperties;
import at.technikum.frontend.PL.viewmodels.LogViewModel;
import javafx.beans.binding.Bindings;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class LogValidator extends Validator {
  private BaseLogController controller;

  /**
   * @param controller Base controller for log validation
   */
  public LogValidator(final BaseLogController controller) {
    this.controller = controller;
    setupFocusListeners();
    setupButtonValidation();
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if the log is valid, false otherwise
   */
  public boolean validateLog(final LogViewModel logViewModel) {
    errorCountProperty.set(0);
    boolean isValid = false;

    // Validate each field
    isValid |= validateStartDate(logViewModel);
    isValid &= validateEndDate(logViewModel);
    isValid &= validateStartTime(logViewModel);
    isValid &= validateEndTime(logViewModel);
    isValid &= validateComment(logViewModel);
    isValid &= validateDistance(logViewModel);
    isValid &= validateRating(logViewModel);
    isValid &= validateDifficulty(logViewModel);

    return isValid;
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if the start date is valid, false otherwise
   */
  private boolean validateStartDate(final LogViewModel logViewModel) {
    if (logViewModel.getStartDate() == null) {
      controller.getStartDateError().setText(AppProperties.getInstance().get("validation.startDate.required"));
      showError(controller.getStartDate(), controller.getStartDateError());
      return false;
    } else if (validateDateOrder(logViewModel)) {
      controller.getStartDateError().setText(AppProperties.getInstance().get("validation.startDate.order"));
      showError(controller.getStartDate(), controller.getStartDateError());
      return false;
    }
    hideError(controller.getStartDate(), controller.getStartDateError());
    return true;
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if the end date is valid, false otherwise
   */
  private boolean validateEndDate(final LogViewModel logViewModel) {
    if (logViewModel.getEndDate() == null) {
      controller.getEndDateError().setText(AppProperties.getInstance().get("validation.endDate.required"));
      showError(controller.getEndDate(), controller.getEndDateError());
      return false;
    } else if (validateDateOrder(logViewModel)) {
      controller.getEndDateError().setText(AppProperties.getInstance().get("validation.endDate.order"));
      showError(controller.getEndDate(), controller.getEndDateError());
      return false;
    }
    hideError(controller.getEndDate(), controller.getEndDateError());
    return true;
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if both are NOT null the end date is after the start date, false
   *         otherwise
   */
  private boolean validateDateOrder(final LogViewModel logViewModel) {
    if (logViewModel.getStartDate() == null || logViewModel.getEndDate() == null) {
      return true;
    }
    return !logViewModel.getEndDate().isAfter(logViewModel.getStartDate());
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if the start time is valid, false otherwise
   */
  private boolean validateStartTime(final LogViewModel logViewModel) {
    return validateTime(logViewModel.getStartTime());
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if the end time is valid, false otherwise
   */
  private boolean validateEndTime(final LogViewModel logViewModel) {
    return validateTime(logViewModel.getEndTime());
  }

  /**
   * @param time The time to validate
   * @return true if the time is valid, false otherwise
   */
  private boolean validateTime(final LocalTime time) {
    return time != null;
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if the comment is valid, false otherwise
   */
  private boolean validateComment(final LogViewModel logViewModel) {
    if (isEmpty(logViewModel.getComment())) {
      showError(controller.getComment(), controller.getCommentError());
      return false;
    }
    hideError(controller.getComment(), controller.getCommentError());
    return true;
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if the total distance is valid, false otherwise
   */
  private boolean validateDistance(final LogViewModel logViewModel) {
    try {
      final double distance = logViewModel.getTotalDistance();
      if (distance <= 0) {
        showError(controller.getTotalDistance(), controller.getTotalDistanceError());
        return false;
      }
      hideError(controller.getTotalDistance(), controller.getTotalDistanceError());
      return true;
    } catch (final NumberFormatException e) {
      showError(controller.getTotalDistance(), controller.getTotalDistanceError());
      return false;
    }
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if the rating is valid, false otherwise
   */
  private boolean validateRating(final LogViewModel logViewModel) {
    final int rating = logViewModel.getRating();
    return rating >= 1 && rating <= 5;
  }

  /**
   * @param logViewModel The log view model to validate
   * @return true if the difficulty is valid, false otherwise
   */
  private boolean validateDifficulty(final LogViewModel logViewModel) {
    final int difficulty = logViewModel.getDifficulty();
    return difficulty >= 1 && difficulty <= 5;
  }

  /**
   * Sets up focus listeners for all input fields to validate on focus lost
   */
  private void setupFocusListeners() {
    // Date fields
    controller.getStartDate().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateStartDate(controller.getLogViewModel());
        if (controller.getLogViewModel().getEndDate() != null) {
          validateEndDate(controller.getLogViewModel());
        }
      }
    });

    controller.getEndDate().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateEndDate(controller.getLogViewModel());
        if (controller.getLogViewModel().getStartDate() != null) {
          validateStartDate(controller.getLogViewModel());
        }
      }
    });

    // Time fields
    controller.getStartTime().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateStartTime(controller.getLogViewModel());
      }
    });

    controller.getEndTime().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateEndTime(controller.getLogViewModel());
      }
    });

    // Other fields
    controller.getComment().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateComment(controller.getLogViewModel());
      }
    });

    controller.getTotalDistance().focusedProperty().addListener((obs, oldValue, newValue) -> {
      if (!newValue) { // Focus lost
        validateDistance(controller.getLogViewModel());
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
