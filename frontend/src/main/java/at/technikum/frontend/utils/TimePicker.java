package at.technikum.frontend.utils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.beans.NamedArg;

import java.time.LocalTime;

/**
 * A custom JavaFX Spinner for selecting time in HH:mm format.
 * <p>
 * This control allows direct text editing and supports incrementing/decrementing
 * either hours or minutes depending on the caret position or mode.
 */
public class TimePicker extends Spinner<LocalTime> {

  private final ObjectProperty<LocalTime> localTime = new SimpleObjectProperty<>();
  private final ObjectProperty<TimePickerMode> mode = new SimpleObjectProperty<>(TimePickerMode.HOURS);

  /**
   * Constructs a TimePicker with the default time (00:00).
   */
  public TimePicker() {
    this(LocalTime.of(0, 0));
  }

  /**
   * Constructs a TimePicker with the specified default time.
   *
   * @param defaultTime the time to initialize the picker with
   */
  public TimePicker(@NamedArg("defaultTime") LocalTime defaultTime) {
    super();
    initialize(defaultTime);
  }

  /**
   * Constructs a TimePicker by parsing the given time string in HH:mm format.
   *
   * @param timeString a time string, e.g., "12:30"
   */
  public TimePicker(@NamedArg("defaultTime") String timeString) {
    this(LocalTime.parse(timeString));
  }

  private void initialize(LocalTime defaultTime) {
    setEditable(true);

    StringConverter<LocalTime> converter = TimePickerHelper.createConverter(defaultTime);

    SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<>() {
      {
        setConverter(converter);
        setValue(defaultTime);
      }

      @Override
      public void decrement(int steps) {
        TimePickerMode m = mode.get();
        setValue(m.decrement(getValue(), steps));
        m.select(TimePicker.this);
      }

      @Override
      public void increment(int steps) {
        TimePickerMode m = mode.get();
        setValue(m.increment(getValue(), steps));
        m.select(TimePicker.this);
      }
    };

    setValueFactory(valueFactory);
    localTime.bindBidirectional(valueFactory.valueProperty());

    getEditor().setTextFormatter(new TextFormatter<>(converter, defaultTime, TimePickerHelper.createInputFilter()));
    getEditor().setText(converter.toString(defaultTime));

    getEditor().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
      if (e.getCode() == KeyCode.TAB) {
        mode.set(mode.get() == TimePickerMode.HOURS ? TimePickerMode.MINUTES : TimePickerMode.HOURS);
        e.consume();
      }
    });

    getEditor().addEventFilter(MouseEvent.MOUSE_CLICKED, e -> setMode(TimePickerMode.fromCaret(getEditor().getCaretPosition())));

    mode.addListener((obs, oldMode, newMode) -> newMode.select(this));
    TimePickerHelper.bindModeToCaret(this);
    mode.get().select(this);
  }

  /**
   * Returns the local time value property.
   */
  public ObjectProperty<LocalTime> localTimeProperty() {
    return localTime;
  }

  /**
   * Gets the selected LocalTime.
   */
  public LocalTime getLocalTime() {
    return localTime.get();
  }

  /**
   * Sets the selected LocalTime.
   */
  public void setLocalTime(LocalTime time) {
    this.localTime.set(time);
  }

  /**
   * Returns the current editing mode (HOURS or MINUTES).
   */
  public TimePickerMode getMode() {
    return mode.get();
  }

  /**
   * Sets the current editing mode.
   */
  public void setMode(TimePickerMode newMode) {
    mode.set(newMode);
  }

  /**
   * Returns the mode property for binding.
   */
  public ObjectProperty<TimePickerMode> modeProperty() {
    return mode;
  }
}
