package at.technikum.tourplanner.viewmodels;

import at.technikum.tourplanner.models.Tour;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NoArgsConstructor;

import java.util.UUID;

// responsible for tour_info.fxml (listing of the tour info) !!
@NoArgsConstructor
public class TourViewModel {

  private ObjectProperty<UUID> id = new SimpleObjectProperty<>();
  private StringProperty name = new SimpleStringProperty();
  private StringProperty description = new SimpleStringProperty();
  private StringProperty from = new SimpleStringProperty();
  private StringProperty to = new SimpleStringProperty();
  private StringProperty transport_type = new SimpleStringProperty();
  private final ObjectProperty<LogTableViewModel> logs = new SimpleObjectProperty<>(new LogTableViewModel());

//  private IntegerProperty distance = new SimpleIntegerProperty();
//  private IntegerProperty estimated_time = new SimpleIntegerProperty();
//  private StringProperty route_info = new SimpleStringProperty();

  public TourViewModel(final Tour tour) {
    this.id = new SimpleObjectProperty<>(tour.getId());
    this.name = new SimpleStringProperty(tour.getName());
    this.description = new SimpleStringProperty(tour.getDescription());
    this.from = new SimpleStringProperty(tour.getFrom());
    this.to = new SimpleStringProperty(tour.getTo());
    this.transport_type = new SimpleStringProperty(tour.getTransport_type());
//    this.distance = new SimpleIntegerProperty(tour.getDistance());
//    this.estimated_time = new SimpleIntegerProperty(tour.getEstimated_time());
//    this.route_info = new SimpleStringProperty(tour.getRoute_info());

  }

  public TourViewModel(final TourViewModel ttvm) {
    this.id = new SimpleObjectProperty<>(ttvm.getId());
    this.name = new SimpleStringProperty(ttvm.getName());
    this.description = new SimpleStringProperty(ttvm.getTour_description());
    this.from = new SimpleStringProperty(ttvm.getFrom());
    this.to = new SimpleStringProperty(ttvm.getTo());
    this.transport_type = new SimpleStringProperty(ttvm.getTransport_type());
//    this.distance = new SimpleIntegerProperty(ttvm.getTour_distance());
//    this.estimated_time = new SimpleIntegerProperty(ttvm.getEstimated_time());
//    this.route_info = new SimpleStringProperty(ttvm.getRoute_info());
  }

  public LogTableViewModel getLogs() {
    return logs.get();
  }

  // getters of the properties values
  public UUID getId() {
    return id.get();
  }

  public String getName() {
    return name.get();
  }

  public String getTour_description() {
    return description.get();
  }

  public String getFrom() {
    return from.get();
  }

  public String getTo() {
    return to.get();
  }

  public String getTransport_type() {
    return transport_type.get();
  }

//  public int getTour_distance() {
//    return distance.get();
//  }

//  public int getEstimated_time() {
//    return estimated_time.get();
//  }

//  public String getRoute_info() {
//    return route_info.get();
//  }

  // getters of the properties themselves
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

  public StringProperty transport_typeProperty() {
    return transport_type;
  }

//  public IntegerProperty distanceProperty() {
//    return distance;
//  }

//  public IntegerProperty estimated_timeProperty() {
//    return estimated_time;
//  }

//  public StringProperty route_infoProperty() {
//    return route_info;
//  }

  public void setId(UUID id) {
    this.id.set(id);
  }

  public void setName(String name) {
    this.name.set(name);
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public void setFrom(String from) {
    this.from.set(from);
  }

  public void setTo(String to) {
    this.to.set(to);
  }

  public void setTransportType(String transportType) {
    this.transport_type.set(transportType);
  }
}
