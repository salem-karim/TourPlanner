package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class TourPlannerController implements Initializable {
  
  //final private TourTableViewModel tourTableViewModel;
  
  @FXML
  private Button createButton;

  @FXML
  private Button editButton;
  
  @FXML
  private Button deleteButton;


    @Override
  public void initialize(URL location, ResourceBundle resources) {
    //tourTableViewModel = new TourTableViewModel();
  }
}