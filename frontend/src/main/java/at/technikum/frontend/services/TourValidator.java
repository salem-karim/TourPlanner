package at.technikum.frontend.services;

import at.technikum.frontend.utils.AppProperties;
import at.technikum.frontend.viewmodels.TourViewModel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor
public class TourValidator extends Validator {
  
  //TODO: Add methods to validate only specific fields like:
  // validateString for Name and Description
  // use the API to validate From and To locations

  public boolean validateTour(TourViewModel tourViewModel) {
    List<String> errors = new ArrayList<>();

    // Check for empty required fields
    if (isEmpty(tourViewModel.getName())) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.name.required"));
    }

    if (isEmpty(tourViewModel.getFrom())) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.from.required"));
    }

    if (isEmpty(tourViewModel.getTo())) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.to.required"));
    }

    if (tourViewModel.getTransportType() == null) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.transportType.required"));
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