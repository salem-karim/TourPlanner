package at.technikum.tourplanner.models;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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


}
