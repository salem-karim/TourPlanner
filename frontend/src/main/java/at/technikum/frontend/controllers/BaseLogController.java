package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.services.LogValidator;
import at.technikum.frontend.viewmodels.LogTableViewModel;
import at.technikum.frontend.viewmodels.LogViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
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

import java.time.LocalTime;

import static at.technikum.frontend.utils.Localization.i18n;

@SuperBuilder
@Getter
@NoArgsConstructor
public abstract class BaseLogController {
  @FXML
  protected Label mainLabel;
  @FXML
  protected ButtonBar saveCancelButtonBar;
  @FXML
  protected TextField comment, difficulty, totalDistance, rating, startTime, endTime;
  @FXML
  protected DatePicker startDate, endDate;

  protected OKCancelButtonBarController okCancelController;
  protected LogTableViewModel logTableViewModel;
  protected LogViewModel logViewModel;
  protected TourViewModel selectedTour;

  protected LogValidator logValidator;

  private boolean initialized = false;

  public void initialize() {
    if (initialized) {
      return;
    }

    logValidator = new LogValidator(i18n);

    if (logViewModel == null) {
      logViewModel = new LogViewModel();
    }

    comment.textProperty().bindBidirectional(logViewModel.commentProperty());
    difficulty.textProperty().bindBidirectional(logViewModel.difficultyProperty(), new NumberStringConverter());
    totalDistance.textProperty().bindBidirectional(logViewModel.totalDistanceProperty(), new NumberStringConverter());
    rating.textProperty().bindBidirectional(logViewModel.ratingProperty(), new NumberStringConverter());
    startDate.valueProperty().bindBidirectional(logViewModel.startDateProperty());
    endDate.valueProperty().bindBidirectional(logViewModel.endDateProperty());
    startTime.textProperty().bindBidirectional(logViewModel.startTimeProperty(), new StringConverter<>() {
      @Override
      public String toString(LocalTime object) {
        return object != null ? object.toString() : "";
      }

      @Override
      public LocalTime fromString(String string) {
        return null;
      }
    });
    endTime.textProperty().bindBidirectional(logViewModel.endTimeProperty(), new StringConverter<>() {
      @Override
      public String toString(LocalTime object) {
        return object != null ? object.toString() : "";
      }

      @Override
      public LocalTime fromString(String string) {
        return null;
      }
    });


    // Set up OK/Cancel button handlers
    okCancelController = (OKCancelButtonBarController) saveCancelButtonBar
            .getProperties()
            .get("okCancelButtonBarController");

    okCancelController.setOkButtonListener(event -> {
      if (logValidator.validateLog(logViewModel)) {
        onSaveButtonClicked();
      }
    });

    okCancelController.setCancelButtonListener(event -> TourPlannerApplication.closeWindow(saveCancelButtonBar));

    initialized = true;
  }

  protected abstract void onSaveButtonClicked();
}
