package at.technikum.frontend.services;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static at.technikum.frontend.utils.Localization.i18n;

@Slf4j
public abstract class Validator {

  public void showValidationError(List<String> errors) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(i18n.getString("validation.error.title"));
    alert.setHeaderText(i18n.getString("validation.error.header"));

    StringBuilder content = new StringBuilder();
    for (String error : errors) {
      content.append("• ").append(error).append("\n");
    }

    alert.setContentText(content.toString());
    alert.getButtonTypes().setAll(ButtonType.OK);
    alert.showAndWait();

    log.warn("Log validation failed with errors: {}", errors);
  }
}
