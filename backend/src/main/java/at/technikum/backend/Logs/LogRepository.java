package at.technikum.backend.Logs;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.technikum.common.models.Logs;

@Repository
public interface LogRepository extends JpaRepository<Logs, UUID> {
}
