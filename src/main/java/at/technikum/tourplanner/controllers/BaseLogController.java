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
import javafx.util.converter.NumberStringConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    comment.textProperty().bindBidirectional(logViewModel.commentProperty());
    difficulty.textProperty().bindBidirectional(logViewModel.difficultyProperty(), new NumberStringConverter());
    totalDistance.textProperty().bindBidirectional(logViewModel.totalDistanceProperty(), new NumberStringConverter());
    // total Time still needs pattern validation (hh:mm)
    totalTime.textProperty().bindBidirectional(logViewModel.totalTimeProperty(), new NumberStringConverter());
    rating.textProperty().bindBidirectional(logViewModel.ratingProperty(), new NumberStringConverter());
    date.valueProperty().bindBidirectional(logViewModel.dateProperty());

    // Set up OK/Cancel button handlers
    okCancelController = (OKCancelButtonBarController) saveCancelButtonBar
            .getProperties()
            .get("okCancelButtonBarController");

    okCancelController.setOkButtonListener(event -> {
      if (logValidator.validateLog(logViewModel)) {
        onSaveButtonClicked();
      }
      // if (date.getValue() != null) {
      // logViewModel.setDate(LocalDateTime.of(date.getValue(), LocalTime.now()));
      // }
    });

    okCancelController.setCancelButtonListener(event -> TourPlannerApplication.closeWindow(saveCancelButtonBar));

    initialized = true;
  }

  protected abstract void onSaveButtonClicked();
}
