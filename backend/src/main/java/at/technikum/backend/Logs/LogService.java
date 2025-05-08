package at.technikum.backend.Logs;

import at.technikum.common.models.Logs;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Logs saveLog(final Logs log) {
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
