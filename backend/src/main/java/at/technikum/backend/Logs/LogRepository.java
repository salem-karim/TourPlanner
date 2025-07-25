package at.technikum.backend.Logs;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.technikum.common.DAL.models.Logs;

@Repository
public interface LogRepository extends JpaRepository<Logs, UUID> {
  void deleteAllByTourId(UUID tour_id);
  List<Logs> findAllByTourId(UUID tourId);
}
