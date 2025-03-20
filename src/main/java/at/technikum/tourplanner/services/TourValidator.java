package at.technikum.tourplanner.services;

import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
public class TourValidator {
  private final ResourceBundle i18n;

  public TourValidator(ResourceBundle i18n) {
    this.i18n = i18n;
  }

  public boolean validateTour(TourViewModel tourViewModel) {
    List<String> errors = new ArrayList<>();

    // Check for empty required fields
    if (isEmpty(tourViewModel.getName())) {
      errors.add(i18n.getString("validation.name.required"));
    }

    if (isEmpty(tourViewModel.getFrom())) {
      errors.add(i18n.getString("validation.from.required"));
    }

    if (isEmpty(tourViewModel.getTo())) {
      errors.add(i18n.getString("validation.to.required"));
    }

    if (isEmpty(tourViewModel.getTransport_type())) {
      errors.add(i18n.getString("validation.transportType.required"));
    }

    // If validation failed, show error message
    if (!errors.isEmpty()) {
      showValidationError(errors);
      return false;
    }

    return true;
  }

  private boolean isEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  private void showValidationError(List<String> errors) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(i18n.getString("validation.error.title"));
    alert.setHeaderText(i18n.getString("validation.error.header"));

    StringBuilder content = new StringBuilder();
    for (String error : errors) {
      content.append("• ").append(error).append("\n");
    }

    alert.setContentText(content.toString());
    alert.getButtonTypes().setAll(ButtonType.OK);
    alert.showAndWait();

    log.warn("Tour validation failed with errors: {}", errors);
  }
}