package at.technikum.tourplanner.viewmodels;

import at.technikum.tourplanner.models.Tour;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// responsible for tour_general.fxml (listing of the tour info) !!
public class TourViewModel {

    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty tour_description;
    private final StringProperty from;
    private final StringProperty to;
    private final StringProperty transport_type;
    private final IntegerProperty tour_distance;
    private final IntegerProperty estimated_time;
    private final StringProperty route_info;


    public TourViewModel(Tour tour) {
        this.id = new SimpleStringProperty(tour.getId().toString());
        this.name = new SimpleStringProperty(tour.getName());
        this.tour_description = new SimpleStringProperty(tour.getTour_description());
        this.from = new SimpleStringProperty(tour.getFrom());
        this.to = new SimpleStringProperty(tour.getTo());
        this.transport_type = new SimpleStringProperty(tour.getTransport_type());
        this.tour_distance = new SimpleIntegerProperty(tour.getTour_distance());
        this.estimated_time = new SimpleIntegerProperty(tour.getEstimated_time());
        this.route_info = new SimpleStringProperty(tour.getRoute_info());

    }

    public TourViewModel(TourViewModel ttvm) {
        this.id = new SimpleStringProperty(ttvm.getId());
        this.name = new SimpleStringProperty(ttvm.getName());
        this.tour_description = new SimpleStringProperty(ttvm.getTour_description());
        this.from = new SimpleStringProperty(ttvm.getFrom());
        this.to = new SimpleStringProperty(ttvm.getTo());
        this.transport_type = new SimpleStringProperty(ttvm.getTransport_type());
        this.tour_distance = new SimpleIntegerProperty(ttvm.getTour_distance());
        this.estimated_time = new SimpleIntegerProperty(ttvm.getEstimated_time());
        this.route_info = new SimpleStringProperty(ttvm.getRoute_info());
    }

    public String getId() {
        return id.get();
    }
    public String getName(){
        return name.get();
    }
    public String getTour_description() {
        return tour_description.get();
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
    public int getTour_distance() {
        return tour_distance.get();
    }
    public int getEstimated_time() {
        return estimated_time.get();
    }
    public String getRoute_info() {
        return route_info.get();
    }

    public StringProperty idProperty() {
        return id;
    }
    public StringProperty nameProperty() {
        return name;
    }
    public StringProperty tour_descriptionProperty() {
        return tour_description;
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
    public IntegerProperty tour_distanceProperty() {
        return tour_distance;
    }
    public IntegerProperty estimated_timeProperty() {
        return estimated_time;
    }
    public StringProperty route_infoProperty() {
        return route_info;
    }
}
