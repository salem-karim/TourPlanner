package at.technikum.frontend.viewmodels;

import at.technikum.common.models.Tour;
import at.technikum.common.models.TransportType;
import javafx.beans.property.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static at.technikum.frontend.utils.Localization.i18n;

// responsible for tour_info.fxml (listing of the tour info) !!
@NoArgsConstructor
public class TourViewModel {

  private ObjectProperty<UUID> id = new SimpleObjectProperty<>();
  private StringProperty name = new SimpleStringProperty();
  private StringProperty description = new SimpleStringProperty();
  private StringProperty from = new SimpleStringProperty();
  private StringProperty to = new SimpleStringProperty();
  private ObjectProperty<TransportType> transport_type = new SimpleObjectProperty<>();
  private IntegerProperty total_distance = new SimpleIntegerProperty();
  private IntegerProperty estimated_time = new SimpleIntegerProperty();
  private ObjectProperty<byte[]> route_info = new SimpleObjectProperty<>();
  private final ObjectProperty<LogTableViewModel> logs = new SimpleObjectProperty<>(new LogTableViewModel());

  public TourViewModel(final Tour tour) {
    this.id.set(tour.getId());
    this.name.set(tour.getName());
    this.description.set(tour.getDescription());
    this.from.set(tour.getFrom());
    this.to.set(tour.getTo());
    this.transport_type.set(tour.getTransport_type());
    this.total_distance.set(tour.getTotal_distance());
    this.estimated_time.set(tour.getEstimated_time());
    this.route_info.set(tour.getRoute_info());
  }

  // Property value getters
  public UUID getId() { return id.get(); }
  public String getName() { return name.get(); }
  public String getTour_description() { return description.get(); }
  public String getFrom() { return from.get(); }
  public String getTo() { return to.get(); }
  public TransportType getTransport_type() { return transport_type.get(); }
  public int getTour_distance() { return total_distance.get(); }
  public int getEstimated_time() { return estimated_time.get(); }
  public byte[] getRoute_info() { return route_info.get(); }

  // Property getters
  public ObjectProperty<UUID> idProperty() { return id; }
  public StringProperty nameProperty() { return name; }
  public StringProperty descriptionProperty() { return description; }
  public StringProperty fromProperty() { return from; }
  public StringProperty toProperty() { return to; }
  public ObjectProperty<TransportType> transport_typeProperty() { return transport_type; }
  public IntegerProperty distanceProperty() { return total_distance; }
  public IntegerProperty estimated_timeProperty() { return estimated_time; }
  public ObjectProperty<byte[]> route_infoProperty() { return route_info; }

  // Setters
  public void setId(UUID id) { this.id.set(id); }
  public void setName(String name) { this.name.set(name); }
  public void setDescription(String description) { this.description.set(description); }
  public void setFrom(String from) { this.from.set(from); }
  public void setTo(String to) { this.to.set(to); }
  public void setTransportType(TransportType transportType) { this.transport_type.set(transportType); }
  public void setTotalDistance(int distance) { this.total_distance.set(distance); }
  public void setEstimatedTime(int estimatedTime) { this.estimated_time.set(estimatedTime); }
  public void setRouteInfo(byte[] routeInfo) { this.route_info.set(routeInfo); }

  public LogTableViewModel getLogs() { return logs.get(); }
}
