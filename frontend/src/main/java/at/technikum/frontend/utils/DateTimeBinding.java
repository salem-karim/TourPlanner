package at.technikum.frontend.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeBinding {
  // Define time format
  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  /**
   * Binds a DatePicker and TextField to a LocalDateTime property bidirectionally
   *
   * @param dateTimeProp LocalDateTime property to bind to
   * @param datePicker   DatePicker for selecting the date
   * @param timeField    TextField for entering the time
   */
  public static void bindBidirectionalDateTime(
          ObjectProperty<LocalDateTime> dateTimeProp,
          DatePicker datePicker,
          TextField timeField) {

    // Set up time field formatter and validator
    timeField.setPromptText("HH:mm");

    // Initialize controls with current values if available
    if (dateTimeProp.get() != null) {
      LocalDateTime currentValue = dateTimeProp.get();
      datePicker.setValue(currentValue.toLocalDate());
      timeField.setText(currentValue.toLocalTime().format(TIME_FORMATTER));
    }

    // Update dateTimeProp when datePicker changes
    datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
      updateDateTime(dateTimeProp, newDate, timeField.getText());
    });

    // Update dateTimeProp when timeField changes
    timeField.textProperty().addListener((obs, oldTime, newTime) -> {
      updateDateTime(dateTimeProp, datePicker.getValue(), newTime);
    });

    // Update controls when dateTimeProp changes
    dateTimeProp.addListener((ObservableValue<? extends LocalDateTime> obs,
                              LocalDateTime oldValue,
                              LocalDateTime newValue) -> {
      if (newValue != null) {
        // Update without triggering the listeners
        datePicker.setValue(newValue.toLocalDate());
        timeField.setText(newValue.toLocalTime().format(TIME_FORMATTER));
      } else {
        datePicker.setValue(null);
        timeField.setText("");
      }
    });
  }

  private static void updateDateTime(
          ObjectProperty<LocalDateTime> property,
          LocalDate date,
          String timeString) {

    if (date == null) {
      property.set(null);
      return;
    }

    try {
      // Parse time or use midnight as default
      LocalTime time = LocalTime.MIDNIGHT;
      if (timeString != null && !timeString.trim().isEmpty()) {
        time = LocalTime.parse(timeString.trim(), TIME_FORMATTER);
      }

      // Combine date and time
      property.set(LocalDateTime.of(date, time));
    } catch (DateTimeParseException e) {
      // Invalid time format, don't update the datetime property
      // You could add error handling/highlighting here
    }
  }

  /**
   * Sample usage with a ViewModel
   */
  public static class ViewModel {
    private final ObjectProperty<LocalDateTime> startDateTime =
            new SimpleObjectProperty<>(LocalDateTime.now());

    private final ObjectProperty<LocalDateTime> endDateTime =
            new SimpleObjectProperty<>(LocalDateTime.now().plusHours(1));

    public ObjectProperty<LocalDateTime> startDateTimeProperty() {
      return startDateTime;
    }

    public ObjectProperty<LocalDateTime> endDateTimeProperty() {
      return endDateTime;
    }

    public LocalDateTime getStartDateTime() {
      return startDateTime.get();
    }

    public LocalDateTime getEndDateTime() {
      return endDateTime.get();
    }

    public void setStartDateTime(LocalDateTime dateTime) {
      startDateTime.set(dateTime);
    }

    public void setEndDateTime(LocalDateTime dateTime) {
      endDateTime.set(dateTime);
    }
  }

  /**
   * Example of how to use this binding in a controller or view
   */
  public static void exampleUsage() {
    // Create view model
    ViewModel viewModel = new ViewModel();

    // Create UI controls
    DatePicker startDatePicker = new DatePicker();
    TextField startTimeField = new TextField();

    DatePicker endDatePicker = new DatePicker();
    TextField endTimeField = new TextField();

    // Set up bindings
    bindBidirectionalDateTime(viewModel.startDateTimeProperty(), startDatePicker, startTimeField);
    bindBidirectionalDateTime(viewModel.endDateTimeProperty(), endDatePicker, endTimeField);

    // Now when the user interacts with startDatePicker and startTimeField,
    // the viewModel.startDateTime property will be updated, and vice versa
  }
}
