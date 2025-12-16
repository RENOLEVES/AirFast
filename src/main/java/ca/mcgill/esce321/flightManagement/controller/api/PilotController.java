
package ca.mcgill.esce321.flightManagement.controller.api;

import ca.mcgill.esce321.flightManagement.controller.request.PilotRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PilotResponse;
import ca.mcgill.esce321.flightManagement.service.PilotServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pilots")
@CrossOrigin(origins = "http://localhost:5173")
public class PilotController {

    @Autowired
    private PilotServiceImpl pilotService;

    /**
     * Create a new pilot
     */
    @PostMapping
    public ResponseEntity<PilotResponse> createPilot(@RequestBody PilotRequestDTO request) {
        PilotResponse created = pilotService.createPilot(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(created);
    }

    /**
     * Get pilot by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PilotResponse> getPilotById(@PathVariable Long id) {
        PilotResponse pilot = pilotService.getPilotById(id);
        return ResponseEntity.ok(pilot);
    }

    /**
     * Get all pilots
     */
    @GetMapping
    public ResponseEntity<List<PilotResponse>> getAllPilots() {
        List<PilotResponse> pilots = pilotService.getAllPilots();
        return ResponseEntity.ok(pilots);
    }

    /**
     * Update pilot information
     */
    @PutMapping("/{id}")
    public ResponseEntity<PilotResponse> updatePilot(
            @PathVariable Long id,
            @RequestBody PilotRequestDTO request) {
        PilotResponse updated = pilotService.updatePilot(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a pilot by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePilot(@PathVariable Long id) {
        pilotService.deletePilot(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handle bad or missing data gracefully
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
