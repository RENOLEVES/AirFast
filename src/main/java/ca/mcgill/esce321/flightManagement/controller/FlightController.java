package ca.mcgill.esce321.flightManagement.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.service.FlightServiceImpl;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(
            @RequestBody FlightRequestDTO request
    ) {
        return ResponseEntity.ok(
                flightService.searchFlights(
                        request.getDepartDateTimeFromStr(),
                        request.getArrivalDateTimeFromStr(),
                        request.getDepartLocation(),
                        request.getArrivalLocation()
                )
        );
    }
}
