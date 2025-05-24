package at.technikum.frontend.services;

import at.technikum.frontend.utils.AppProperties;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class Validator {
  
  // TODO: Change this to show the warning label in the UI instead of using an alert dialog
  public void showValidationError(List<String> errors) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(AppProperties.getInstance().getI18n().getString("validation.error.title"));
    alert.setHeaderText(AppProperties.getInstance().getI18n().getString("validation.error.header"));

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
