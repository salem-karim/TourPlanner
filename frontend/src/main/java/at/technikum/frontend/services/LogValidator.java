package at.technikum.frontend.services;

import java.util.ArrayList;
import java.util.List;

import at.technikum.frontend.utils.AppProperties;
import at.technikum.frontend.viewmodels.LogViewModel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class LogValidator extends Validator {

  public boolean validateLog(final LogViewModel logViewModel) {
    final List<String> errors = new ArrayList<>();

    // Check date
    if (logViewModel.getStartDate() == null) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.date.required"));
    }

    if (logViewModel.getEndDate() == null) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.date.required"));
    }

    if (logViewModel.getStartTime() == null) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.date.required"));
    }

    if (logViewModel.getEndTime() == null) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.date.required"));
    }

    // Check comment
    if (isEmpty(logViewModel.getComment())) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.comment.required"));
    }

    // Check difficulty (1-5)
    final int difficulty = logViewModel.getDifficulty();
    if (difficulty < 1 || difficulty > 5) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.difficulty.range"));
    }

    // Check rating (1-5)
    final int rating = logViewModel.getRating();
    if (rating < 1 || rating > 5) {
      errors.add(AppProperties.getInstance().getI18n().getString("validation.rating.range"));
    }

    // If validation failed, show error message
    if (!errors.isEmpty()) {
      showValidationError(errors);
      return false;
    }

    return true;
  }

  private boolean isEmpty(final String value) {
    return value == null || value.trim().isEmpty();
  }
}
