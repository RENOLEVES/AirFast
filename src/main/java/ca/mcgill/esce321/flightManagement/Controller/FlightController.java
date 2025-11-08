package ca.mcgill.esce321.flightManagement.controller;

import ca.mcgill.esce321.flightManagement.service.FlightServiceImpl;
import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {

    @Autowired
    private  FlightServiceImpl flightService;

    /**
     * Create a new Flight
     */
    @PostMapping
    public ResponseEntity<FlightResponseDTO> createFlight(@RequestBody FlightRequestDTO request) {
        Flight created = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FlightResponseDTO(created));
    }

    /**
     * Get a flight by its ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.getFlightById(id);
        return ResponseEntity.ok(new FlightResponseDTO(flight));
    }

    /**
     * Get all flights
     */
    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        List<FlightResponseDTO> dtos = flights.stream()
                                              .map(FlightResponseDTO::new)
                                              .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Update flight information
     */
    @PutMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(
            @PathVariable Long id,
            @RequestBody FlightRequestDTO request) {
        Flight updated = flightService.updateFlight(id, request);
        return ResponseEntity.ok(new FlightResponseDTO(updated));
    }

    /**
     * Delete a flight by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handle illegal arguments or missing resources gracefully
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
