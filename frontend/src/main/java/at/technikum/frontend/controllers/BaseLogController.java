package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.services.LogValidator;
import at.technikum.frontend.viewmodels.LogTableViewModel;
import at.technikum.frontend.viewmodels.LogViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;

import static at.technikum.frontend.utils.DateTimeBinding.bindBidirectionalDateTime;
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

    for (DatePicker datePicker : Arrays.asList(startDate, endDate)) {
      datePicker.setPromptText("DD.MM.YYYY");
    }

    comment.textProperty().bindBidirectional(logViewModel.commentProperty());
    difficulty.textProperty().bindBidirectional(logViewModel.difficultyProperty(), new NumberStringConverter());
    totalDistance.textProperty().bindBidirectional(logViewModel.totalDistanceProperty(), new NumberStringConverter());
    rating.textProperty().bindBidirectional(logViewModel.ratingProperty(), new NumberStringConverter());
    bindBidirectionalDateTime(logViewModel.startDateProperty(), startDate, startTime);
    bindBidirectionalDateTime(logViewModel.endDateProperty(), endDate, endTime);
    

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
