package at.technikum.frontend.PL.controllers;

import at.technikum.frontend.PL.viewmodels.TourViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

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
          () -> tourViewModel.getLocalizedTransportType(), tourViewModel.transport_typeProperty()));
      distanceLabel.textProperty().bind(Bindings.createStringBinding(
          () -> String.format("%.2f", tourViewModel.getDistance()), tourViewModel.distanceProperty()));
      durationLabel.textProperty().bind(Bindings.createStringBinding(
          () -> String.format("%.2f", tourViewModel.getEstimatedTime()), tourViewModel.estimatedTimeProperty()));

      // Make ImageView responsive
      AnchorPane parentPane = (AnchorPane) RouteImage.getParent();

      // Simple width and height bindings
      RouteImage.fitWidthProperty().bind(parentPane.widthProperty());
      RouteImage.fitHeightProperty().bind(parentPane.heightProperty());
      RouteImage.setPreserveRatio(true);

      // Center the image by setting alignment
      AnchorPane.setTopAnchor(RouteImage, 0.0);
      AnchorPane.setBottomAnchor(RouteImage, 0.0);
      AnchorPane.setLeftAnchor(RouteImage, 0.0);
      AnchorPane.setRightAnchor(RouteImage, 0.0);

      // Image binding
      RouteImage.imageProperty().bind(Bindings.createObjectBinding(() -> {
        byte[] routeInfo = tourViewModel.getRouteInfo();
        if (routeInfo != null) {
          try {
            return new Image(new ByteArrayInputStream(routeInfo));
          } catch (Exception e) {
            log.error("Failed to load route image: {}", e.getMessage(), e);
          }
        }
        return null;
      }, tourViewModel.routeInfoProperty()));
    } else {
      log.warn("TourViewModel is null, skipping bindings");
    }
  }
}
