package at.technikum.frontend.services;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Validator {
  protected int errorCount = 0;
  protected IntegerProperty errorCountProperty = new SimpleIntegerProperty(0);
  protected static final String ERROR_STYLE = "-fx-border-color: red;";
  protected static final double ERROR_HEIGHT = 16.0;

  /**
   * Increments the Error Count as well as the Property
   */
  protected void incrementErrorCount() {
    errorCount++;
    errorCountProperty.set(errorCount);
  }

  /**
   * Decrements the Error Count as well as the Property
   */
  protected void decrementErrorCount() {
    errorCount--;
    errorCountProperty.set(errorCount);
  }

  /**
   * @param field      the field to validate
   * @param errorLabel the label to show error messages
   */
  public void showError(final Control field, final Label errorLabel) {
    if (!errorLabel.isVisible()) {
      field.setStyle(ERROR_STYLE);
      errorLabel.setVisible(true);
      errorLabel.setPrefHeight(ERROR_HEIGHT);
      incrementErrorCount();
    }
  }

  /**
   * @param field      the field to validate
   * @param errorLabel the label to hide error messages
   */
  public void hideError(final Control field, final Label errorLabel) {
    if (errorLabel.isVisible()) {
      field.setStyle("");
      errorLabel.setVisible(false);
      errorLabel.setPrefHeight(0);
      decrementErrorCount();
    }
  }

  /**
   * @param value the string value to check
   * @return true if the string is empty or null, false otherwise
   */
  protected boolean isEmpty(final String value) {
    return value == null || value.trim().isEmpty();
  }

}
