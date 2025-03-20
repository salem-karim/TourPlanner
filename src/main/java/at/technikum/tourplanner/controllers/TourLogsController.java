package at.technikum.tourplanner.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class TourLogsController implements Initializable {

  public TableColumn rating;
  public TableColumn totalTime;
  public TableColumn totalDistance;
  public TableColumn difficulty;
  public TableColumn comment;
  public TableColumn date;
  public TableView logTable;
  public ButtonBar newEditDeleteButtonBar;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // TODO Auto-generated method stub
  }
}
