package at.technikum.backend.Logs;

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

import at.technikum.common.DAL.models.Logs;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/logs")
public class LogController {
  private final LogService logService;

  public LogController(final LogService logService) {
    this.logService = logService;
  }

  @PostMapping
  public ResponseEntity<Logs> createLog(@RequestBody final Logs log) {
    return new ResponseEntity<>(logService.saveLog(log), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Logs> updateLog(@PathVariable final UUID id, @RequestBody final Logs log) {
    try {
      return new ResponseEntity<>(logService.updateLog(id, log), HttpStatus.OK);
    } catch (final IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLog(@PathVariable final UUID id) {
    try {
      logService.deleteLog(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (final IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping
  public ResponseEntity<List<Logs>> getAllLogs() {
    return new ResponseEntity<>(logService.getAllLogs(), HttpStatus.OK);
  }

}
