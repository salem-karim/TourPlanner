package at.technikum.backend.Logs;

import at.technikum.common.models.Logs;
import at.technikum.common.models.Tour;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;
    private final LogRepository logRepository;

    public LogController(LogService logService, LogRepository logRepository) {
        this.logService = logService;
        this.logRepository = logRepository;
    }

    @PostMapping
    public ResponseEntity<Logs> createLog(@RequestBody Logs log) {
        return new ResponseEntity<>(logService.saveLog(log), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Logs> updateLog(@PathVariable UUID id, @RequestBody Logs log) {
        try {
            return new ResponseEntity<>(logService.updateLog(id, log), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable UUID id) {
        try {
            logService.deleteLog(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Logs>> getAllLogs() {
        return new ResponseEntity<>(logService.getAllLogs(), HttpStatus.OK);
    }

}
