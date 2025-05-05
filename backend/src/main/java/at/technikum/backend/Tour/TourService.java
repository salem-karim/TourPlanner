package at.technikum.backend.Tour;

import org.springframework.stereotype.Service;
import at.technikum.common.models.Tour;

import java.util.UUID;

@Service
//@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public Tour saveTour(Tour tour) {
        // If the ID is null, generate a new one
        if (tour.getId() == null) {
            tour.setId(UUID.randomUUID());
        }
        return tourRepository.save(tour);
    }

    public Tour updateTour(UUID id, Tour updatedTour) {
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

    public void deleteTour(UUID id) {
        if (!tourRepository.existsById(id)) {
            throw new IllegalArgumentException("Tour not found");
        }
        tourRepository.deleteById(id);
    }
}
