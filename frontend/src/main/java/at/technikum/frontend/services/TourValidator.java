package at.technikum.frontend.services;

import at.technikum.frontend.viewmodels.TourViewModel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
public class TourValidator extends Validator {
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

    if (tourViewModel.getTransportType() == null) {
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
}