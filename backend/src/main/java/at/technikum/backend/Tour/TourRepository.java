package at.technikum.backend.Tour;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.technikum.common.models.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, UUID> {
}
