package at.technikum.frontend.utils;

import java.time.LocalTime;

/**
 * Enum representing the editing mode of the TimePicker: HOURS or MINUTES.
 * <p>
 * Each mode defines how the spinner increments values and which portion
 * of the time string should be selected.
 */
public enum TimePickerMode {
  HOURS {
    @Override
    public LocalTime increment(final LocalTime time, final int steps) {
      return time.plusHours(steps);
    }

    @Override
    public void select(final TimePicker picker) {
      picker.getEditor().selectRange(0, 2);
    }
  },
  MINUTES {
    @Override
    public LocalTime increment(final LocalTime time, final int steps) {
      return time.plusMinutes(steps);
    }

    @Override
    public void select(final TimePicker picker) {
      picker.getEditor().selectRange(3, 5);
    }
  };

  /**
   * Increments the given time by the specified number of steps.
   *
   * @param time  the original time
   * @param steps number of units to increment (can be negative)
   * @return the incremented LocalTime
   */
  public abstract LocalTime increment(LocalTime time, int steps);

  /**
   * Decrements the given time by the specified number of steps.
   *
   * @param time  the original time
   * @param steps number of units to decrement
   * @return the decremented LocalTime
   */
  public LocalTime decrement(final LocalTime time, final int steps) {
    return increment(time, -steps);
  }

  /**
   * Selects the appropriate portion (HH or mm) in the TimePicker's text field.
   */
  public abstract void select(TimePicker picker);

  /**
   * Determines the mode based on the caret position in the editor.
   *
   * @param caretPosition current caret position
   * @return HOURS if <= 2, MINUTES otherwise
   */
  public static TimePickerMode fromCaret(final int caretPosition) {
    return caretPosition <= 2 ? HOURS : MINUTES;
  }
}
