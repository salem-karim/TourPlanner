package at.technikum.frontend.utils;

import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

/**
 * Utility class for creating converters and input filters for the TimePicker.
 */
public class TimePickerHelper {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  /**
   * Creates a StringConverter for converting between LocalTime and HH:mm text.
   *
   * @param fallbackTime the fallback time to use if parsing fails
   * @return a LocalTime converter
   */
  public static StringConverter<LocalTime> createConverter(LocalTime fallbackTime) {
    return new StringConverter<>() {
      @Override
      public String toString(LocalTime time) {
        return time != null ? time.format(FORMATTER) : "";
      }

      @Override
      public LocalTime fromString(String string) {
        try {
          if (!string.contains(":")) return fallbackTime;
          String[] parts = string.split(":");
          if (parts.length != 2) return fallbackTime;

          int hours = Integer.parseInt(parts[0]);
          int minutes = Integer.parseInt(parts[1]);

          hours = Math.max(0, Math.min(23, hours));
          minutes = Math.max(0, Math.min(59, minutes));

          return LocalTime.of(hours, minutes);
        } catch (Exception e) {
          return fallbackTime;
        }
      }
    };
  }

  /**
   * Creates an input filter that ensures input stays in HH:mm format
   * and prevents invalid hours/minutes values.
   *
   * @return a filter for text changes
   */
  public static UnaryOperator<Change> createInputFilter() {
    return change -> {
      String newText = change.getControlNewText();

      if (!newText.contains(":") && change.getRangeStart() <= 2 && change.getRangeEnd() >= 3) {
        return null;
      }

      if (!newText.matches("[0-9]{0,2}:[0-9]{0,2}")) return null;

      String[] parts = newText.split(":");
      try {
        if (parts.length > 0 && !parts[0].isEmpty() && Integer.parseInt(parts[0]) > 23) return null;
        if (parts.length > 1 && !parts[1].isEmpty() && Integer.parseInt(parts[1]) > 59) return null;
      } catch (NumberFormatException e) {
        return null;
      }

      return change;
    };
  }

  /**
   * Binds the mode of the TimePicker to the caret position of its text editor.
   * Automatically switches between HOURS and MINUTES based on caret movement.
   *
   * @param timePicker the time picker instance
   */
  public static void bindModeToCaret(TimePicker timePicker) {
    timePicker.getEditor().caretPositionProperty().addListener((obs, oldVal, newVal) -> timePicker.setMode(TimePickerMode.fromCaret(newVal.intValue())));
  }
}
