package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.net.URL;
import java.util.ResourceBundle;

public class TourPlannerController implements Initializable {
  
  private TourTableViewModel tourTableViewModel;
  
  @FXML
  private Button createButton;

  @FXML
  private Button modifyButton;
  
  @FXML
  private Button deleteButton;

  @FXML
  private Button generalButton;

  @FXML
  private Button logsButton;



  @FXML
  private ListView<String> toursListView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    tourTableViewModel = new TourTableViewModel();
    toursListView.setItems(tourTableViewModel.getDataNames());
  }
}