package at.technikum.tourplanner;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TourPlannerController {
  @FXML
  private Label welcomeText;

  @FXML
  protected void onHelloButtonClick() {
    welcomeText.setText("Welcome to JavaFX Application!");
  }
}