package at.technikum.backend.Logs;

import at.technikum.backend.Tour.TourRepository;
import at.technikum.common.models.Logs;
import at.technikum.common.models.Tour;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LogService {
    private final LogRepository logRepository;
    private final TourRepository tourRepository;

    public LogService(LogRepository logRepository, TourRepository tourRepository) {
        this.logRepository = logRepository;
      this.tourRepository = tourRepository;
    }

    public Logs saveLog(final Logs log, final UUID tourId) {

      Tour tour = tourRepository.findById(tourId)
              .orElseThrow(() -> new IllegalArgumentException("Tour not found with id: " + tourId));
        log.setTour(tour);
        if (log.getId() == null) {
            log.setId(UUID.randomUUID());
        }
        return logRepository.save(log);
    }

    public Logs updateLog(UUID id, Logs updatedLog) {
        return logRepository.findById(id)
                .map(existing -> {
                    existing.setStart_date_time(updatedLog.getStart_date_time());
                    existing.setEnd_date_time(updatedLog.getEnd_date_time());
                    existing.setComment(updatedLog.getComment());
                    existing.setDifficulty(updatedLog.getDifficulty());
                    existing.setTotal_distance(updatedLog.getTotal_distance());
                    existing.setTotal_time(updatedLog.getTotal_time());
                    existing.setRating(updatedLog.getRating());
                    return logRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Log not found"));
    }

    public void deleteLog(UUID id) {
        if (!logRepository.existsById(id)) {
            throw new IllegalArgumentException("Log not found");
        }
        logRepository.deleteById(id);
    }
}
