package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.services.LogValidator;
import at.technikum.tourplanner.viewmodels.LogTableViewModel;
import at.technikum.tourplanner.viewmodels.LogViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
public abstract class BaseLogController {
  @FXML
  protected Label mainLabel;
  @FXML
  protected ButtonBar saveCancelButtonBar;
  @FXML
  protected TextField comment, difficulty, totalDistance, totalTime, rating;
  @FXML
  protected DatePicker date;

  protected OKCancelButtonBarController okCancelController;
  protected LogTableViewModel logTableViewModel;
  protected LogViewModel logViewModel;

  protected LogValidator logValidator;

  protected boolean validateLog() {
    return logValidator.validateLog(logViewModel);
  }

  private boolean initialized = false;

  public void initialize() {
    if (initialized) {
      return;
    }

    logValidator = new LogValidator(TourPlannerApplication.i18n);

    if (logViewModel == null) {
      logViewModel = new LogViewModel();
    }

    // Configure date picker
    StringConverter<LocalDate> dateConverter = new StringConverter<>() {
      private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

      @Override
      public String toString(LocalDate date) {
        if (date != null) {
          return dateFormatter.format(date);
        }
        return "";
      }

      @Override
      public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
          return LocalDate.parse(string, dateFormatter);
        }
        return null;
      }
    };
    date.setConverter(dateConverter);
    date.setPromptText("DD.MM.YYYY");

    // Set field values from logViewModel (one-way)
    comment.setText(logViewModel.getComment());
    difficulty.setText(String.valueOf(logViewModel.getDifficulty()));
    totalDistance.setText(String.valueOf(logViewModel.getTotalDistance()));
    totalTime.setText(String.valueOf(logViewModel.getTotalTime()));
    rating.setText(String.valueOf(logViewModel.getRating()));

    if (logViewModel.getDate() != null) {
      date.setValue(logViewModel.getDate());
    }

    // Set up OK/Cancel button handlers
    okCancelController = (OKCancelButtonBarController)
            saveCancelButtonBar.getProperties().get("okCancelButtonBarController");

    okCancelController.setOkButtonListener(event -> {
      // Only when Save is clicked, copy values from UI to model
      try {
        logViewModel.setComment(comment.getText());
        logViewModel.setDifficulty(Integer.parseInt(difficulty.getText()));
        logViewModel.setTotalDistance((int) Double.parseDouble(totalDistance.getText()));
        logViewModel.setTotalTime((int) Double.parseDouble(totalTime.getText()));
        logViewModel.setRating(Integer.parseInt(rating.getText()));
        if (date.getValue() != null) {
          logViewModel.setDate(LocalDateTime.of(date.getValue(), LocalTime.now()));
        }

        if (validateLog()) {
          onSaveButtonClicked();
        }
      } catch (NumberFormatException e) {
        // Show validation error for number parsing
        logValidator.showValidationError(List.of("Invalid number format"));
      }
    });

    okCancelController.setCancelButtonListener(event ->
            TourPlannerApplication.closeWindow(saveCancelButtonBar));

    initialized = true;
  }

  protected abstract void onSaveButtonClicked();
}