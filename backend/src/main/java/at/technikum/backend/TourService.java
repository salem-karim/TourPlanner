package at.technikum.backend;

import lombok.RequiredArgsConstructor;
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
}
