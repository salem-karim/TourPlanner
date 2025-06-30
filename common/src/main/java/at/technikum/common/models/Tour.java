package at.technikum.common.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tour {

  @Id
  // @GeneratedValue
  // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  // @id
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

  // @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval =
  // true)
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "tour_id")
  private List<Logs> logs = new ArrayList<>();

}
