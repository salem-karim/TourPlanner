package at.technikum.frontend.PL.controllers;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import at.technikum.frontend.PL.viewmodels.TourViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Setter
@Slf4j
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
  @FXML
  private ImageView RouteImage;

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

      // Text bindings
      nameLabel.textProperty().bind(tourViewModel.nameProperty());
      descriptionLabel.textProperty().bind(tourViewModel.descriptionProperty());
      fromLabel.textProperty().bind(tourViewModel.fromProperty());
      toLabel.textProperty().bind(tourViewModel.toProperty());
      transportTypeLabel.textProperty().bind(Bindings.createStringBinding(
          () -> tourViewModel.getLocalizedTransportType(),
          tourViewModel.transport_typeProperty()));
      distanceLabel.textProperty().bind(Bindings.createStringBinding(
          () -> String.format("%.2f", tourViewModel.getDistance()),
          tourViewModel.distanceProperty()));
      durationLabel.textProperty().bind(Bindings.createStringBinding(
          () -> String.format("%.2f", tourViewModel.getEstimatedTime()),
          tourViewModel.estimatedTimeProperty()));

      // Make ImageView responsive
      RouteImage.fitWidthProperty().bind(splitPane.widthProperty().multiply(0.4));
      RouteImage.setPreserveRatio(true);

      // Image binding
      tourViewModel.routeInfoProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
          try {
            final var byteArrayInputStream = new ByteArrayInputStream(newValue);
            Image image = new Image(byteArrayInputStream);
            RouteImage.setImage(image);
          } catch (Exception e) {
            log.error("Failed to load route image: {}", e.getMessage(), e);
          }
        } else {
          RouteImage.setImage(null);
          log.info("Cleared image from ImageView");
        }
      });

      // Set initial image if available
      byte[] currentRouteInfo = tourViewModel.getRouteInfo();
      if (currentRouteInfo != null) {
        final var byteArrayInputStream = new ByteArrayInputStream(currentRouteInfo);
        RouteImage.setImage(new Image(byteArrayInputStream));
      }
    } else {
      log.warn("TourViewModel is null, skipping bindings");
    }
  }
}
