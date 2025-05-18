package at.technikum.frontend.controllers;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.services.LogValidator;
import at.technikum.frontend.utils.TimePicker;
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
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.Rating;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static at.technikum.frontend.utils.Localization.i18n;

@Slf4j
@SuperBuilder
@Getter
@NoArgsConstructor
public abstract class BaseLogController {
  @FXML
  protected Label mainLabel;
  @FXML
  protected ButtonBar saveCancelButtonBar;
  @FXML
  protected TextField comment, totalDistance;
  @FXML
  protected DatePicker startDate, endDate;
  @FXML
  protected TimePicker startTime, endTime;
  @FXML
  protected Rating difficulty, rating;
  
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
          try {
            return LocalDate.parse(string, dateFormatter);
          } catch (Exception e) {
            log.error("Error in parsing Date: ", e);
          }
        }
        return null;
      }
    };
    
    // TODO: Harden validation of date input
    startDate.setConverter(dateConverter);
    endDate.setConverter(dateConverter);
    startDate.setPromptText("DD.MM.YYYY");
    endDate.setPromptText("DD.MM.YYYY");

    comment.textProperty().bindBidirectional(logViewModel.commentProperty());
    totalDistance.textProperty().bindBidirectional(logViewModel.totalDistanceProperty(), new NumberStringConverter());
    totalDistance.setText("");
    startDate.valueProperty().bindBidirectional(logViewModel.startDateProperty());
    endDate.valueProperty().bindBidirectional(logViewModel.endDateProperty());
    // Bind the time pickers value to the ViewModel not the other way around
    logViewModel.endTimeProperty().bindBidirectional(endTime.localTimeProperty());
    logViewModel.startTimeProperty().bindBidirectional(startTime.localTimeProperty());
    logViewModel.difficultyProperty().bindBidirectional(difficulty.ratingProperty());
    logViewModel.ratingProperty().bindBidirectional(rating.ratingProperty());


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
