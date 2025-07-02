package at.technikum.frontend.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.controlsfx.control.Rating;

import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.services.LogValidator;
import at.technikum.frontend.utils.AppProperties;
import at.technikum.frontend.utils.TimePicker;
import at.technikum.frontend.viewmodels.LogTableViewModel;
import at.technikum.frontend.viewmodels.LogViewModel;
import at.technikum.frontend.viewmodels.TourViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder
@Getter
@NoArgsConstructor
public abstract class BaseLogController {
  @FXML
  protected Label mainLabel, startDateError, endDateError, totalDistanceError, commentError;
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
  @FXML
  protected AnchorPane LogPane;

  protected OKCancelButtonBarController okCancelController;
  protected LogTableViewModel logTableViewModel;
  protected LogViewModel logViewModel;
  protected TourViewModel selectedTour;

  protected LogValidator logValidator;

  @Builder.Default
  private boolean initialized = false;

  /**
   * Initialized all JavaFX Members
   */
  @FXML
  public void initialize() {
    if (initialized) {
      return;
    }

    if (logViewModel == null) {
      logViewModel = new LogViewModel();
    }

    // Initialize basic components first
    initializeComponents();

    // Set up bindings after time pickers are configured
    setupBindings();

    setupButtonBar();

    setupForTest();

    setupRatingKeyHandlers();

    initialized = true;
  }

  /**
   * Sets Converters and PromptText for the DatePickers, and the TextFormatter for
   * the Distance Textfield
   */
  private void initializeComponents() {
    // TODO: make a TextFormatter for Dates as well
    startDate.setConverter(configureDatePicker(startDate, startDateError, "startDate", logViewModel.getStartDate()));
    endDate.setConverter(configureDatePicker(endDate, endDateError, "endDate", logViewModel.getEndDate()));
    startDate.setPromptText("DD.MM.YYYY");
    endDate.setPromptText("DD.MM.YYYY");

    totalDistance.setTextFormatter(new TextFormatter<>(change -> {
      final String newText = change.getControlNewText();
      return newText.matches("\\d*\\.?\\d{0,2}") ? change : null;
    }));
  }

  /**
   * Binds all the TextFields to the View Model Properties Bi-Directionaly
   */
  private void setupBindings() {
    comment.textProperty().bindBidirectional(logViewModel.commentProperty());
    totalDistance.textProperty().bindBidirectional(logViewModel.totalDistanceProperty(), new NumberStringConverter(""));
    if (logViewModel.getTotalDistance() == 0.0) {
      totalDistance.setText("");
    }
    startDate.valueProperty().bindBidirectional(logViewModel.startDateProperty());
    endDate.valueProperty().bindBidirectional(logViewModel.endDateProperty());

    // Bind time pickers
    logViewModel.startTimeProperty().bindBidirectional(startTime.localTimeProperty());
    logViewModel.endTimeProperty().bindBidirectional(endTime.localTimeProperty());

    logViewModel.difficultyProperty().bindBidirectional(difficulty.ratingProperty());
    logViewModel.ratingProperty().bindBidirectional(rating.ratingProperty());
  }

  /**
   * Constructs the ButtonBar by finding the correct Controller
   */
  private void setupButtonBar() {
    // Set up OK/Cancel button handlers
    okCancelController = (OKCancelButtonBarController) saveCancelButtonBar.getProperties()
        .get("okCancelButtonBarController");

    logValidator = new LogValidator(this);

    okCancelController.setOkButtonListener(event -> {
      if (logValidator.validateLog(logViewModel)) {
        onSaveButtonClicked();
      }
    });

    okCancelController.setCancelButtonListener(event -> TourPlannerApplication.closeWindow(saveCancelButtonBar));
  }

  /**
   * Sets some Ids for easier Testing
   */
  private void setupForTest() {
    if (System.getProperty("app.test") != null) {
      okCancelController.getCancelButton().setId("logCancelButton");
      okCancelController.getOkButton().setId("logOkButton");
      startDate.setId("logStartDate");
      endDate.setId("logEndDate");
      totalDistance.setId("logTotalDistance");
    }
  }

  /**
   * @param datePicker The DatePicker to inject into the Validator
   * @param errorLabel The Error Label to inject into the Validator
   * @param type       The type of Date startDate and endDate only !
   * @return null or a StringConverter of LocalDate
   */
  private StringConverter<LocalDate> configureDatePicker(
      final DatePicker datePicker,
      final Label errorLabel,
      final String type,
      final LocalDate fallback) {
    return new StringConverter<>() {
      private final DateTimeFormatter[] parseFormatters = {
          DateTimeFormatter.ofPattern("dd.MM.yyyy"),
          DateTimeFormatter.ofPattern("dd/MM/yyyy"),
          DateTimeFormatter.ofPattern("d.MM.yyyy"),
          DateTimeFormatter.ofPattern("d/MM/yyyy"),
          DateTimeFormatter.ofPattern("dd.M.yyyy"),
          DateTimeFormatter.ofPattern("dd/M/yyyy"),
          DateTimeFormatter.ofPattern("dd.MM.yy"),
          DateTimeFormatter.ofPattern("dd/MM/yy"),
          DateTimeFormatter.ofPattern("d.M.yyyy"),
          DateTimeFormatter.ofPattern("d/M/yyyy"),
          DateTimeFormatter.ofPattern("d.MM.yy"),
          DateTimeFormatter.ofPattern("d/MM/yy"),
          DateTimeFormatter.ofPattern("dd.M.yy"),
          DateTimeFormatter.ofPattern("dd/M/yy"),
          DateTimeFormatter.ofPattern("d.M.yy"),
          DateTimeFormatter.ofPattern("d/M/yy")
      };

      @Override
      public String toString(final LocalDate date) {
        return date != null ? date.format(parseFormatters[0]) : "";
      }

      @Override
      public LocalDate fromString(final String string) {
        if (string != null && !string.isEmpty()) {
          for (var formatter : parseFormatters) {
            try {
              var date = LocalDate.parse(string, formatter);
              logValidator.hideError(datePicker, errorLabel);
              return date;
            } catch (final DateTimeParseException e) {
              log.error("Error in parsing Date {} : {}", string, e.getMessage());
            }
          }
          errorLabel.setText(AppProperties.getInstance().get("validation." + type + ".required"));
          logValidator.showError(datePicker, errorLabel);
        }
        return fallback;
      }
    };

  }

  /**
   * Sets Up Key Handlers for the Rating and Difficulty
   */
  protected void setupRatingKeyHandlers() {
    // For Rating component
    rating.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.LEFT) {
        int currentRating = (int) rating.getRating();
        if (currentRating > 1) {
          rating.setRating(currentRating - 1);
          event.consume();
        }
      } else if (event.getCode() == KeyCode.RIGHT) {
        int currentRating = (int) rating.getRating();
        if (currentRating < 5) {
          rating.setRating(currentRating + 1);
          event.consume();
        }
      }
    });

    // For Difficulty component
    difficulty.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.LEFT) {
        int currentDifficulty = (int) difficulty.getRating();
        if (currentDifficulty > 1) {
          difficulty.setRating(currentDifficulty - 1);
          event.consume();
        }
      } else if (event.getCode() == KeyCode.RIGHT) {
        int currentDifficulty = (int) difficulty.getRating();
        if (currentDifficulty < 5) {
          difficulty.setRating(currentDifficulty + 1);
          event.consume();
        }
      }
    });
  }

  /**
   * Behavior of the Save or OK Button
   */
  protected abstract void onSaveButtonClicked();

}
