package at.technikum.common.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tour {

  @Id
//  @GeneratedValue
//  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  @id
  private UUID id;

  private String name;

  private String description;

  @Column(name = "origin")
  private String from;

  @Column(name = "destination")
  private String to;

  @Enumerated(EnumType.STRING)
  @Column(name = "transport_type")
  private TransportType transport_type;

  @Column(name = "distance_km")
  private int total_distance;

  @Column(name = "estimated_time_minutes")
  private int estimated_time;

  @Lob
  @Column(name = "route_image")
  private byte[] route_info;

  @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Logs> logs = new ArrayList<>();

}
