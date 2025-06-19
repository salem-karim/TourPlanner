package at.technikum.backend.Tour;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import at.technikum.common.models.Tour;

@Service
// @RequiredArgsConstructor
public class TourService {
  private final TourRepository tourRepository;

  public TourService(final TourRepository tourRepository) {
    this.tourRepository = tourRepository;
  }

  public Tour saveTour(final Tour tour) {
    // If the ID is null, generate a new one
    if (tour.getId() == null) {
      tour.setId(UUID.randomUUID());
    }
    return tourRepository.save(tour);
  }

  public Tour updateTour(final UUID id, final Tour updatedTour) {
    return tourRepository.findById(id)
        .map(existing -> {
          // Set all updatable fields
          existing.setName(updatedTour.getName());
          existing.setDescription(updatedTour.getDescription());
          existing.setFrom(updatedTour.getFrom());
          existing.setTo(updatedTour.getTo());
          existing.setTransport_type(updatedTour.getTransport_type());
          existing.setTotal_distance(updatedTour.getTotal_distance());
          existing.setEstimated_time(updatedTour.getEstimated_time());
          existing.setRoute_info(updatedTour.getRoute_info());
          return tourRepository.save(existing);
        })
        .orElseThrow(() -> new IllegalArgumentException("Tour with ID " + id + " not found"));
  }

  public void deleteTour(final UUID id) {
    if (!tourRepository.existsById(id)) {
      throw new IllegalArgumentException("Tour not found");
    }
    tourRepository.deleteById(id);
  }

  public List<Tour> getAllTours() {
    return tourRepository.findAll();
  }
}
