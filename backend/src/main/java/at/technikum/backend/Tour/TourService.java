package at.technikum.backend.Tour;

import java.util.List;
import java.util.UUID;

import at.technikum.backend.Logs.LogRepository;
import at.technikum.common.DAL.models.Logs;
import org.springframework.stereotype.Service;

import at.technikum.common.DAL.models.Tour;
import org.springframework.transaction.annotation.Transactional;

@Service
// @RequiredArgsConstructor
public class TourService {
  private final TourRepository tourRepository;
  private final LogRepository logsRepository;

  public TourService(final TourRepository tourRepository, final LogRepository logsRepository) {
    this.tourRepository = tourRepository;
    this.logsRepository = logsRepository;
  }

  public Tour saveTour(final Tour tour) {
    // If the ID is null, generate a new one
    if (tour.getId() == null) {
      tour.setId(UUID.randomUUID());
    }
    return tourRepository.save(tour);
  }

  public Tour updateTour(final UUID id, final Tour updatedTour) {
    return tourRepository.findById(id).map(existing -> {
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
    }).orElseThrow(() -> new IllegalArgumentException("Tour with ID " + id + " not found"));
  }

  public List<Logs> getLogsForTour(UUID tourId) {
    return logsRepository.findAllByTourId(tourId);
  }

  @Transactional
  public void deleteTour(final UUID id) {
    if (!tourRepository.existsById(id)) {
      throw new IllegalArgumentException("Tour not found");
    }
    logsRepository.deleteAllByTourId(id);
    tourRepository.deleteById(id);
  }

  public List<Tour> getAllTours() {
    return tourRepository.findAll();
  }
}
