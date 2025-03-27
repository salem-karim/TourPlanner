package at.technikum.tourplanner.models;

import at.technikum.tourplanner.utils.TransportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Tour {
  private UUID id;
  private String name;
  private String description;
  private String from;
  private String to;
  private TransportType transport_type;
//  private int distance;
//  private int estimated_time;
//  private String route_info; //image???

}
