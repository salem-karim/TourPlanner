package at.technikum.tourplanner.models;

import java.util.UUID;

public class Tour {
    private UUID id;
    private String name;
    private String tour_description;
    private String from;
    private String to;
    private String transport_type;
    private int tour_distance;
    private int estimated_time;
    private String route_info; //image???

    public Tour(UUID id, String name, String tour_description, String from, String to, String transport_type, int tour_distance, int estimated_time, String route_info) {
        this.id = id;
        this.name = name;
        this.tour_description = tour_description;
        this.from = from;
        this.to = to;
        this.transport_type = transport_type;
        this.tour_distance = tour_distance;
        this.estimated_time = estimated_time;
        this.route_info = route_info;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTour_description() {
        return tour_description;
    }
    public void setTour_description(String tour_description) {
        this.tour_description = tour_description;
    }

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }

    public String getTransport_type() {
        return transport_type;
    }
    public void setTransport_type(String transport_type) {
        this.transport_type = transport_type;
    }

    public int getTour_distance() {
        return tour_distance;
    }
    public void setTour_distance(int tour_distance) {
        this.tour_distance = tour_distance;
    }

    public int getEstimated_time() {
        return estimated_time;
    }
    public void setEstimated_time(int estimated_time) {
        this.estimated_time = estimated_time;
    }

    public String getRoute_info() {
        return route_info;
    }
    public void setRoute_info(String route_info) {
        this.route_info = route_info;
    }
}
