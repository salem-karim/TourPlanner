package at.technikum.common.DAL.models;

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
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "origin", nullable = false)
  private String from;

  @Column(name = "destination", nullable = false)
  private String to;

  @Enumerated(EnumType.STRING)
  @Column(name = "transport_type", nullable = false)
  private TransportType transport_type;

  @Column(name = "distance_km")
  private double total_distance;

  @Column(name = "estimated_time_minutes")
  private double estimated_time;

  @Lob
  @Column(name = "route_image")
  private byte[] route_info;

  // @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval =
  // true)
//  @OneToMany
//  @JoinColumn(name = "tour_id")
  transient private List<Logs> logs = new ArrayList<>();

}
