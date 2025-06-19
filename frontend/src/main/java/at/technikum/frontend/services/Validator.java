package at.technikum.frontend.services;

import java.util.List;

import at.technikum.frontend.utils.AppProperties;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Validator {

  public void showValidationError(final List<String> errors) {
    final Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(AppProperties.getInstance().getI18n().getString("validation.error.title"));
    alert.setHeaderText(AppProperties.getInstance().getI18n().getString("validation.error.header"));

    final StringBuilder content = new StringBuilder();
    for (final String error : errors) {
      content.append("• ").append(error).append("\n");
    }

    alert.setContentText(content.toString());
    alert.getButtonTypes().setAll(ButtonType.OK);
    alert.showAndWait();

    log.warn("Log validation failed with errors: {}", errors);
  }
}
