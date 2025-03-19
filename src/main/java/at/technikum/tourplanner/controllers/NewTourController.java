package at.technikum.tourplanner.controllers;

import at.technikum.tourplanner.TourPlannerApplication;
import at.technikum.tourplanner.viewmodels.TourTableViewModel;
import at.technikum.tourplanner.viewmodels.TourViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import lombok.Builder;

import java.net.URL;
import java.util.ResourceBundle;

@Builder
public class NewTourController implements Initializable {

  @FXML
  private Label mainLabel;
  @FXML
  private ButtonBar saveCancelButtonBar;
  @FXML
  private TextField name, description, from, to, transportType, distance, duration;

  private TourTableViewModel tourTableViewModel;
  private TourViewModel tourViewModel;
  private ListView<String> toursListView;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    name.textProperty().bindBidirectional(tourViewModel.nameProperty());
    description.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
    from.textProperty().bindBidirectional(tourViewModel.fromProperty());
    to.textProperty().bindBidirectional(tourViewModel.toProperty());
    transportType.textProperty().bindBidirectional(tourViewModel.transport_typeProperty());
    distance.textProperty().bindBidirectional(tourViewModel.distanceProperty(), new NumberStringConverter());
    duration.textProperty().bindBidirectional(tourViewModel.estimated_timeProperty(), new NumberStringConverter());

    OKCancelButtonBarController okCancelController = (OKCancelButtonBarController)
            saveCancelButtonBar.getProperties().get("okCancelButtonBarController");
    okCancelController.getOkButton().setText(TourPlannerApplication.i18n.getString("button.new"));
    okCancelController.setOkButtonListener(event -> onSaveButtonClicked());
    okCancelController.setCancelButtonListener(event -> TourPlannerApplication.closeWindow(saveCancelButtonBar));
  }

  private void onSaveButtonClicked() {
//    tourViewModel.idProperty().get() = UUID.randomUUID();
    tourTableViewModel.newTour(tourViewModel);
    System.out.println(tourTableViewModel.getData());
    toursListView.setItems(tourTableViewModel.getDataNames());
    TourPlannerApplication.closeWindow(saveCancelButtonBar);
  }
}
