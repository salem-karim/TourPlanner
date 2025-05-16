package at.technikum.frontend.utils;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.beans.NamedArg;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
// TODO: Fix bug where the carret pos moves after one step in minutes and also make the : not selectable or editable
public class TimePicker extends Spinner<LocalTime> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
  private final ObjectProperty<LocalTime> localTime = new SimpleObjectProperty<>();

  // --- FXML-Compatible Constructors ---

  /** Default constructor for FXML (uses 00:00) */
  public TimePicker() {
    this(LocalTime.of(0, 0));
  }

  /** Constructor with @NamedArg so you can use default time in FXML */
  public TimePicker(@NamedArg("defaultTime") LocalTime defaultTime) {
    super();
    initialize(defaultTime);
  }

  /** Constructor with @NamedArg so you can use default time in FXML */
  public TimePicker(@NamedArg("defaultTime") String timeString) {
    this(LocalTime.parse(timeString));
  }

  // --- Core init logic shared across all constructors
  private void initialize(LocalTime defaultTime) {
    SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<>() {
      {
        setConverter(new StringConverter<>() {
          @Override
          public String toString(LocalTime time) {
            return time != null ? time.format(FORMATTER) : "";
          }

          @Override
          public LocalTime fromString(String string) {
            try {
              return LocalTime.parse(string, FORMATTER);
            } catch (Exception e) {
              return getValue();
            }
          }
        });
        setValue(defaultTime);
      }

      @Override
      public void decrement(int steps) {
        if (getEditor().getCaretPosition() <= 2) {
          setValue(getValue().minusHours(steps));
        } else {
          setValue(getValue().minusMinutes(steps));
        }
      }

      @Override
      public void increment(int steps) {
        if (getEditor().getCaretPosition() <= 2) {
          setValue(getValue().plusHours(steps));
        } else {
          setValue(getValue().plusMinutes(steps));
        }
      }
    };

    setValueFactory(valueFactory);
    setEditable(true);

    // Bind internal value to localTime property
    Bindings.bindBidirectional(valueFactory.valueProperty(), localTime);

    // Tab navigation
    getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.getCode() == KeyCode.TAB) {
        int pos = getEditor().getCaretPosition();
        if (pos <= 2) {
          getEditor().positionCaret(3); // to minutes
        } else {
          getEditor().positionCaret(0); // to hours
        }
        event.consume();
      }
    });

    // Mouse select hours/minutes
    getEditor().addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
      int pos = getEditor().getCaretPosition();
      if (pos <= 2) {
        getEditor().selectRange(0, 2);
      } else {
        getEditor().selectRange(3, 5);
      }
    });
  }

  // --- JavaBean-style accessors

  public ObjectProperty<LocalTime> localTimeProperty() {
    return localTime;
  }

  public LocalTime getLocalTime() {
    return localTime.get();
  }

  public void setLocalTime(LocalTime time) {
    this.localTime.set(time);
  }
}
