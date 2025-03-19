package at.technikum.tourplanner.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
@NoArgsConstructor
public class NewEditDeleteButtonBarController implements Initializable {

  @FXML
  private ButtonBar buttonBar;
  @FXML
  private Button newButton;
  @FXML
  private Button editButton;
  @FXML
  private Button deleteButton;

  private EventHandler newButtonListener;
  private EventHandler editButtonListener;
  private EventHandler deleteButtonListener;

  public void onNew(ActionEvent event) {
    if (newButtonListener != null) {
      newButtonListener.handle(null);
    }
  }

  public void onEdit(ActionEvent event) {
    if (editButtonListener != null) {
      editButtonListener.handle(null);
    }
  }

  public void onDelete(ActionEvent event) {
    if (deleteButtonListener != null) {
      deleteButtonListener.handle(null);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    buttonBar.getProperties().put("newEditDeleteButtonBarController", this);
    newButton.setOnAction(this::onNew);
    editButton.setOnAction(this::onEdit);
    deleteButton.setOnAction(this::onDelete);
  }
}
