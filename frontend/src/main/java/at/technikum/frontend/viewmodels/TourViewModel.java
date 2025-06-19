package at.technikum.frontend.viewmodels;

import java.util.UUID;

import at.technikum.common.models.Tour;
import at.technikum.common.models.TransportType;
import at.technikum.frontend.utils.AppProperties;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

// responsible for tour_info.fxml (listing of the tour info) !!
@NoArgsConstructor
public class TourViewModel {

  private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty description = new SimpleStringProperty();
  private final StringProperty from = new SimpleStringProperty();
  private final StringProperty to = new SimpleStringProperty();
  private final ObjectProperty<TransportType> transport_type = new SimpleObjectProperty<>();
  private final IntegerProperty distance = new SimpleIntegerProperty();
  private final IntegerProperty estimated_time = new SimpleIntegerProperty();
  private final ObjectProperty<byte[]> route_info = new SimpleObjectProperty<>();
  private final ObjectProperty<LogTableViewModel> logs = new SimpleObjectProperty<>();

  public TourViewModel(final Tour tour) {
    this.id.set(tour.getId());
    this.name.set(tour.getName());
    this.description.set(tour.getDescription());
    this.from.set(tour.getFrom());
    this.to.set(tour.getTo());
    this.transport_type.set(tour.getTransport_type());
    this.distance.set(tour.getTotal_distance());
    this.estimated_time.set(tour.getEstimated_time());
    this.route_info.set(tour.getRoute_info());
    this.logs.set(new LogTableViewModel(tour.getLogs()));
  }

  public TourViewModel(final TourViewModel tourViewModel) {
    this.id.set(tourViewModel.getId());
    this.name.set(tourViewModel.getName());
    this.description.set(tourViewModel.getDescription());
    this.from.set(tourViewModel.getFrom());
    this.to.set(tourViewModel.getTo());
    this.transport_type.set(tourViewModel.getTransportType());
    this.distance.set(tourViewModel.getDistance());
    this.estimated_time.set(tourViewModel.getEstimatedTime());
    this.route_info.set(tourViewModel.getRouteInfo());
    this.logs.set(tourViewModel.getLogs());
  }

  public Tour toTour() {
    return new Tour(
        getId(),
        getName(),
        getDescription(),
        getFrom(),
        getTo(),
        getTransportType(),
        getDistance(),
        getEstimatedTime(),
        getRouteInfo(),
        null);
  }

  // Property value getters
  public UUID getId() {
    return id.get();
  }

  public String getName() {
    return name.get();
  }

  public String getDescription() {
    return description.get();
  }

  public String getFrom() {
    return from.get();
  }

  public String getTo() {
    return to.get();
  }

  public TransportType getTransportType() {
    return transport_type.get();
  }

  public int getDistance() {
    return distance.get();
  }

  public int getEstimatedTime() {
    return estimated_time.get();
  }

  public byte[] getRouteInfo() {
    return route_info.get();
  }

  public LogTableViewModel getLogs() {
    return logs.get();
  }

  public String getLocalizedTransportType() {
    final TransportType transportType = getTransportType();
    return transportType == null ? ""
        : AppProperties.getInstance().getI18n()
            .getString("tourInfo.transportType." + transportType.name().toLowerCase());
  }

  // Property getters
  public ObjectProperty<UUID> idProperty() {
    return id;
  }

  public StringProperty nameProperty() {
    return name;
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public StringProperty fromProperty() {
    return from;
  }

  public StringProperty toProperty() {
    return to;
  }

  public ObjectProperty<TransportType> transport_typeProperty() {
    return transport_type;
  }

  public IntegerProperty distanceProperty() {
    return distance;
  }

  public IntegerProperty estimatedTimeProperty() {
    return estimated_time;
  }

  public ObjectProperty<byte[]> routeInfoProperty() {
    return route_info;
  }

  public ObjectProperty<LogTableViewModel> logsProperty() {
    return logs;
  }

  // Setters
  public void setId(final UUID id) {
    this.id.set(id);
  }

  public void setName(final String name) {
    this.name.set(name);
  }

  public void setDescription(final String description) {
    this.description.set(description);
  }

  public void setFrom(final String from) {
    this.from.set(from);
  }

  public void setTo(final String to) {
    this.to.set(to);
  }

  public void setTransportType(final TransportType transportType) {
    this.transport_type.set(transportType);
  }

  public void setDistance(final int distance) {
    this.distance.set(distance);
  }

  public void setEstimatedTime(final int estimatedTime) {
    this.estimated_time.set(estimatedTime);
  }

  public void setRouteInfo(final byte[] routeInfo) {
    this.route_info.set(routeInfo);
  }

  public void setLogs(final LogTableViewModel logs) {
    this.logs.set(logs);
  }

}
