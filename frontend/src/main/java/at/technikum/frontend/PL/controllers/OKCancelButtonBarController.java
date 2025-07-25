package at.technikum.frontend.PL.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OKCancelButtonBarController implements Initializable {

  @FXML
  private ButtonBar buttonBar;
  @FXML
  private Button okButton;
  @FXML
  private Button cancelButton;

  private EventHandler<ActionEvent> okButtonListener;
  private EventHandler<ActionEvent> cancelButtonListener;

  // Mediators View Elements go here later

  public void onOK(ActionEvent event) {
    if (okButtonListener != null) {
      okButtonListener.handle(event);
    }
  }

  public void onCancel(ActionEvent event) {
    if (cancelButtonListener != null) {
      cancelButtonListener.handle(event);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttonBar.getProperties().put("okCancelButtonBarController", this);
    okButton.setOnAction(this::onOK);
    cancelButton.setOnAction(this::onCancel);
  }
}
