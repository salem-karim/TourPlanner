package at.technikum.backend.Logs;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import at.technikum.common.models.Logs;

@Service
public class LogService {
  private final LogRepository logRepository;

  public LogService(final LogRepository logRepository) {
    this.logRepository = logRepository;
  }

  public Logs saveLog(final Logs log) {
    if (log.getId() == null) {
      log.setId(UUID.randomUUID());
    }
    return logRepository.save(log);
  }

  public Logs updateLog(final UUID id, final Logs updatedLog) {
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

  public void deleteLog(final UUID id) {
    if (!logRepository.existsById(id)) {
      throw new IllegalArgumentException("Log not found");
    }
    logRepository.deleteById(id);
  }

  public List<Logs> getAllLogs() {
    return logRepository.findAll();
  }
}
