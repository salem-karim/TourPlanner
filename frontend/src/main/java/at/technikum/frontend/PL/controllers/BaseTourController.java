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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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


/**
 * Base controller for managing tours in the application. Provides common functionality for tour creation and editing.
 */
@SuperBuilder
@Getter
@NoArgsConstructor
@Slf4j
public abstract class BaseTourController {
  @FXML
  protected Label mainLabel, nameError, descriptionError, fromError, toError;
  @FXML
  protected Label transportTypeError, loadMapError;
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

  private String lastFrom;
  private String lastTo;
  private boolean mapLoaded = false;

  /**
   * Initializes the controller, setting up bindings and listeners.
   */
  public void initialize() {
    if (initialized) {
      return;
    }

    if (tourViewModel == null) {
      tourViewModel = new TourViewModel();
    }

    setupTransportType();

    setupBinding();

    okCancelController = (OKCancelButtonBarController) newCancelButtonBar.getProperties()
            .get("okCancelButtonBarController");

    tourValidator = new TourValidator(this);

    okCancelController.setOkButtonListener(event -> OkButtonListener());

    // Update the load map button handler
    loadMapButton.setOnAction(e -> loadMapButtonListener());

    okCancelController.setCancelButtonListener(event -> TourPlannerApplication.closeWindow(newCancelButtonBar));

    initialized = true;
  }

  private void setupTransportType() {
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
  }

  /**
   * Sets up two-way bindings between UI elements and the TourViewModel properties.
   */
  private void setupBinding() {
    // Create two-way bindings between UI elements and viewModel properties
    name.textProperty().bindBidirectional(tourViewModel.nameProperty());
    description.textProperty().bindBidirectional(tourViewModel.descriptionProperty());
    from.textProperty().bindBidirectional(tourViewModel.fromProperty());
    to.textProperty().bindBidirectional(tourViewModel.toProperty());
    transportType.valueProperty().bindBidirectional(tourViewModel.transport_typeProperty());
  }

  /**
   * Listener for the OK button click event. Validates the tour and saves it if valid.
   */
  private void OkButtonListener() {
    if (tourValidator.validateTour(tourViewModel)) {
      if (!mapLoaded || haveLocationsChanged()) {
        if (!tourValidator.validateRouteOnly(tourViewModel)) {
          log.error("Route validation failed");
          return;
        }
        tourValidator.showError(loadMapButton, loadMapError);
      } else {
        // Map is loaded and locations haven't changed, save directly
        final var image = captureWebViewSnapshot();
        if (image != null) {
          tourViewModel.setRouteInfo(image);
          onSaveButtonClicked();
        }
      }
    }
  }

  /**
   * Listener for the Load Map button click event. Validates the route and loads the map if valid.
   */
  private void loadMapButtonListener() {
    if (!tourValidator.validateRouteOnly(tourViewModel)) {
      log.error("Route validation failed");
      return;
    }
    loadMapAndExecute();
  }

  /**
   * Captures a snapshot of the WebView and converts it to a byte array.
   *
   * @return Byte array representing the WebView snapshot, or null if capture failed.
   */
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

  /**
   * Checks if the locations have changed since the last update.
   *
   * @return true if locations have changed, false otherwise.
   */
  private boolean haveLocationsChanged() {
    return !Objects.equals(from.getText(), lastFrom) ||
            !Objects.equals(to.getText(), lastTo);
  }

  private void updateLastLocations() {
    lastFrom = from.getText();
    lastTo = to.getText();
  }

  /**
   * Loads the map. This method is called when the Load Map button is clicked.
   */
  private void loadMapAndExecute() {
    // Ensure the mapWebView is initialized
    log.info("Loading map...");
    String htmlUrl = Objects.requireNonNull(getClass().getResource("/web/map.html")).toExternalForm();
    mapWebView.getEngine().load(htmlUrl);

    // stateProperty listener to check when the map is loaded
    mapWebView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
      if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
        log.info("Map loaded successfully, attempting to draw route");
        try {
          if (routeJson == null || !routeJson.has("routes") || routeJson.get("routes").isEmpty()) {
            log.error("Invalid route JSON");
            return;
          }

          String encoded = routeJson.get("routes").get(0).get("geometry").asText();
          encoded = encoded.replace("\\", "\\\\").replace("'", "\\'");
          String script = "loadEncodedPolyline('" + encoded + "')";

          mapWebView.getEngine().executeScript(script);
          mapLoaded = true;
          updateLastLocations();

        } catch (Exception ex) {
          log.error("Failed to load route: {}", ex.getMessage(), ex);
        }
      }
    });
  }

  /**
   * Abstract method to be implemented by subclasses for handling the save button click event. This method is called
   * when the user clicks the save button after validating the tour.
   */
  protected abstract void onSaveButtonClicked();
}
