package at.technikum.frontend.PL.controllers;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.JsonNode;

import at.technikum.common.DAL.models.TransportType;
import at.technikum.frontend.TourPlannerApplication;
import at.technikum.frontend.BL.services.TourValidator;
import at.technikum.frontend.BL.utils.AppProperties;
import at.technikum.frontend.PL.viewmodels.TourTableViewModel;
import at.technikum.frontend.PL.viewmodels.TourViewModel;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebView;
import javafx.util.StringConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Getter
@NoArgsConstructor
@Slf4j
public abstract class BaseTourController {
  @FXML
  protected Label mainLabel, nameError, descriptionError, fromError, toError, transportTypeError;
  @FXML
  protected ButtonBar newCancelButtonBar;
  @FXML
  protected TextField name, description, from, to;
  @FXML
  protected ChoiceBox<TransportType> transportType;
  @FXML
  protected Button loadMapButton;
  @FXML
  protected WebView mapWebView;
  @Setter
  protected JsonNode routeJson;

  protected OKCancelButtonBarController okCancelController;
  protected TourValidator tourValidator;

  protected TourTableViewModel tourTableViewModel;
  protected TourViewModel tourViewModel;
  protected ListView<TourViewModel> toursListView;

  // Flag to prevent duplicate initialization
  @Builder.Default
  private boolean initialized = false;

  public void initialize() {
    if (initialized) {
      return;
    }

    if (tourViewModel == null) {
      tourViewModel = new TourViewModel();
    }

    transportType.setItems(FXCollections.observableArrayList(TransportType.values()));
    transportType.setConverter(new StringConverter<>() {
      @Override
      public String toString(final TransportType type) {
        if (type == null)
          return "";
        return AppProperties.getInstance().getI18n().getString("tourInfo.transportType." + type.name().toLowerCase());
      }

      @Override
      public TransportType fromString(final String string) {
        return null; // Not needed for now
      }
    });

    // Create two-way bindings between UI elements and viewModel properties
    name.textProperty().bindBidirectional(tourViewModel.nameProperty());
    description.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
    from.textProperty().bindBidirectional(tourViewModel.fromProperty());
    to.textProperty().bindBidirectional(tourViewModel.toProperty());
    transportType.valueProperty().bindBidirectional(tourViewModel.transport_typeProperty());

    // Fix the way okCancelController is obtained
    okCancelController = (OKCancelButtonBarController) newCancelButtonBar.getProperties()
        .get("okCancelButtonBarController");

    tourValidator = new TourValidator(this);

    okCancelController.setOkButtonListener(event -> {
      if (tourValidator.validateTour(tourViewModel)) {
        final var image = captureWebViewSnapshot();
        if (image != null)
          tourViewModel.setRouteInfo(image);
        onSaveButtonClicked();
      }
    });

    loadMapButton.setOnAction(e -> {
      log.info("Loading map...");

      if (!tourValidator.validateRouteOnly(tourViewModel)) {
        log.error("Route validation failed");
        final Alert alert = new Alert(Alert.AlertType.ERROR,
            "Could not calculate route. Please verify the locations and try again.",
            ButtonType.OK);
        alert.showAndWait();
        return;
      }

      final String htmlUrl = Objects.requireNonNull(getClass().getResource("/web/map.html")).toExternalForm();
      mapWebView.getEngine().load(htmlUrl);

      mapWebView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
        if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
          log.info("Map loaded successfully, attempting to draw route");

          try {
            if (routeJson == null || !routeJson.has("routes") || routeJson.get("routes").isEmpty()) {
              log.error("Invalid route JSON");
              return;
            }

            String encoded = routeJson.get("routes").get(0).get("geometry").asText();

            // Clean and escape the encoded string
            encoded = encoded.replace("\\", "\\\\").replace("'", "\\'");

            // Use a simpler JavaScript call
            final String script = "loadEncodedPolyline('" + encoded + "')";
            log.info("Executing script with encoded length: {}", encoded.length());

            final Object result = mapWebView.getEngine().executeScript(script);
            log.info("Script execution complete with result: {}", result);

          } catch (final Exception ex) {
            log.error("Failed to load route: {}", ex.getMessage(), ex);
          }
        }
      });
    });

    okCancelController.setCancelButtonListener(event -> TourPlannerApplication.closeWindow(newCancelButtonBar));

    initialized = true;
  }

  protected abstract void onSaveButtonClicked();

  private byte[] captureWebViewSnapshot() {
    try {
      final WritableImage snapshot = mapWebView.snapshot(new SnapshotParameters(), null);

      // Convert to BufferedImage
      final var bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

      // Convert to byte array
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ImageIO.write(bufferedImage, "png", outputStream);

      return outputStream.toByteArray();
    } catch (final Exception e) {
      log.error("Failed to capture WebView snapshot: {}", e.getMessage());
      return null;
    }
  }
}
