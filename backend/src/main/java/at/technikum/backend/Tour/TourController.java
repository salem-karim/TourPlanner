package at.technikum.backend.Tour;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.technikum.common.DAL.models.Tour;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tours")
// @RequiredArgsConstructor
public class TourController {

  private final TourService tourService;

  public TourController(final TourService tourService) {
    this.tourService = tourService;
  }

  @PostMapping
  public ResponseEntity<Tour> createTour(@RequestBody final Tour tour) {
    try {
      final Tour saved = tourService.saveTour(tour);
      return new ResponseEntity<>(saved, HttpStatus.CREATED);
    } catch (final Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Tour> updateTour(@PathVariable final UUID id, @RequestBody final Tour tour) {
    try {
      final Tour updated = tourService.updateTour(id, tour);
      return new ResponseEntity<>(updated, HttpStatus.OK);
    } catch (final IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTour(@PathVariable final UUID id) {
    try {
      tourService.deleteTour(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (final IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping
  public ResponseEntity<List<Tour>> getAllTours() {
    return new ResponseEntity<>(tourService.getAllTours(), HttpStatus.OK);
  }
}
