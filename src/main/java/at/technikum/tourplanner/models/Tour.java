package at.technikum.tourplanner.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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

}
