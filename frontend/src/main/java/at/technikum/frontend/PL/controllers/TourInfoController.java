package at.technikum.frontend.PL.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import at.technikum.frontend.PL.viewmodels.TourViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TourInfoController implements Initializable {

  @FXML
  private Label nameLabel;
  @FXML
  private Label descriptionLabel;
  @FXML
  private Label fromLabel;
  @FXML
  private Label toLabel;
  @FXML
  private Label transportTypeLabel;
  @FXML
  private Label distanceLabel;
  @FXML
  private Label durationLabel;
  @FXML
  private SplitPane splitPane;

  private TourViewModel tourViewModel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    splitPane.getProperties().put("tourInfoController", this);

  }

  public void setTourViewModel(TourViewModel tourViewModel) {
    this.tourViewModel = tourViewModel;
    bindLabels();
  }

  private void bindLabels() {
    if (tourViewModel != null) {
      nameLabel.textProperty().bind(tourViewModel.nameProperty());
      descriptionLabel.textProperty().bind(tourViewModel.descriptionProperty());
      fromLabel.textProperty().bind(tourViewModel.fromProperty());
      toLabel.textProperty().bind(tourViewModel.toProperty());
      transportTypeLabel.textProperty().bind(Bindings.createStringBinding(
          () -> tourViewModel.getLocalizedTransportType(), tourViewModel.transport_typeProperty()));
      // distanceLabel.textProperty().bind(Bindings.convert(tourViewModel.distanceProperty()));
      // durationLabel.textProperty().bind(Bindings.convert(tourViewModel.estimated_timeProperty()));
    }
  }
}
