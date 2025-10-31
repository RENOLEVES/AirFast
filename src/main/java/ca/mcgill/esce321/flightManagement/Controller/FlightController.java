package ca.mcgill.esce321.flightManagement.Controller;

import ca.mcgill.esce321.flightManagement.dto.ManagerRequestDto;
import ca.mcgill.esce321.flightManagement.service.FlightService;
import ca.mcgill.esce321.flightManagement.dto.ManagerResponseDto;
import ca.mcgill.esce321.flightManagement.model.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {

    @Autowired
    private FlightService flightService;

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody FlightCreateDTO dto) {
        Flight created = flightService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/flights/" + created.getId()))
                .body(created);
    }

    // ---------- READ ----------
    /**
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Flight getFlight(@PathVariable Long id) {
        return flightService.get(id);
    }

    // Search flights by from/to/date (e.g. /api/flights/search?from=YUL&to=YYZ&date=2025-11-01)
    @GetMapping("/search")
    public List<Flight> searchFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flightService.search(from, to, date);
    }

    // ---------- UPDATE ----------
    @PutMapping("/{id}")
    public Flight updateFlight(@PathVariable Long id, @RequestBody FlightUpdateDTO dto) {
        return flightService.update(id, dto);
    }

    // ---------- DELAY ----------
    // Example: PATCH /api/flights/5/delay?minutes=45
    @PatchMapping("/{id}/delay")
    public Flight delayFlight(@PathVariable Long id, @RequestParam long minutes) {
        return flightService.delay(id, Duration.ofMinutes(minutes));
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelFlight(@PathVariable Long id) {
        flightService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- EXCEPTION HANDLER ----------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Bad request";
        HttpStatus status = msg.toLowerCase().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}

