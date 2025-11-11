package ca.mcgill.esce321.flightManagement.controller;

import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.service.FlightServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {

    private final FlightServiceImpl flightService;

    public FlightController(FlightServiceImpl flightService) {
        this.flightService = flightService;
    }

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<FlightResponseDTO> create(@RequestBody FlightRequestDTO request) {
        FlightResponseDTO created = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------- READ ONE ----------
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    // ---------- READ ALL ----------
    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getAll() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    // ---------- UPDATE ----------
    @PutMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> update(@PathVariable Long id,
                                                    @RequestBody FlightRequestDTO request) {
        return ResponseEntity.ok(flightService.updateFlight(id, request));
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Error handling ----------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
