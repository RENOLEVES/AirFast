
package ca.mcgill.esce321.flightManagement.controller;

import ca.mcgill.esce321.flightManagement.dto.request.PilotRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PilotResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Pilot;
import ca.mcgill.esce321.flightManagement.service.PilotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pilots")
@CrossOrigin(origins = "*")
public class PilotController {

    @Autowired
    private PilotService pilotService;

    /**
     * Create a new pilot
     */
    @PostMapping
    public ResponseEntity<PilotResponseDTO> createPilot(@RequestBody PilotRequestDTO request) {
        Pilot created = pilotService.createPilot(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new PilotResponseDTO(created));
    }

    /**
     * Get pilot by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PilotResponseDTO> getPilotById(@PathVariable Long id) {
        Pilot pilot = pilotService.getPilotById(id);
        return ResponseEntity.ok(new PilotResponseDTO(pilot));
    }

    /**
     * Get all pilots
     */
    @GetMapping
    public ResponseEntity<List<PilotResponseDTO>> getAllPilots() {
        List<Pilot> pilots = pilotService.getAllPilots();
        List<PilotResponseDTO> dtos = pilots.stream()
                                            .map(PilotResponseDTO::new)
                                            .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Update pilot information
     */
    @PutMapping("/{id}")
    public ResponseEntity<PilotResponseDTO> updatePilot(
            @PathVariable Long id,
            @RequestBody PilotRequestDTO request) {
        Pilot updated = pilotService.updatePilot(id, request);
        return ResponseEntity.ok(new PilotResponseDTO(updated));
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
