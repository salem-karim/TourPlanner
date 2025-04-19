package at.technikum.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import at.technikum.common.models.Tour;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public Tour saveTour(Tour tour) {
        return tourRepository.save(tour);
    }
}
