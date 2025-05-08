package at.technikum.backend.Logs;

import at.technikum.common.models.Logs;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
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
}
