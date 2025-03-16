package at.technikum.tourplanner.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

public class NewTourController implements Initializable {

  @Getter
  @FXML
  private Label mainLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
}
