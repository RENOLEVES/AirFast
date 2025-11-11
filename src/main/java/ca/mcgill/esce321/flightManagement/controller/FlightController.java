package ca.mcgill.esce321.flightManagement.controller;

import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.service.FlightServiceImpl;

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
    private FlightServiceImpl flightService;

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<FlightResponseDTO> create(@RequestBody FlightRequestDTO request) {
        Flight created = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FlightResponseDTO(created));
    }

    // ---------- READ ONE ----------
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getById(@PathVariable Long id) {
        Flight flight = flightService.getFlightById(id);
        return ResponseEntity.ok(new FlightResponseDTO(flight));
    }

    // ---------- READ ALL ----------
    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getAll() {
        List<Flight> flights = flightService.getAllFlights();
        List<FlightResponseDTO> dtos = flights.stream()
                                              .map(FlightResponseDTO::new)
                                              .toList();
        return ResponseEntity.ok(dtos);
    }

    // ---------- UPDATE ----------
    @PutMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> update(@PathVariable Long id,
                                                    @RequestBody FlightRequestDTO request) {
        Flight updated = flightService.updateFlight(id, request);
        return ResponseEntity.ok(new FlightResponseDTO(updated));
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- (Optional) add if your service has these methods ----------
    // Search example:
    // @GetMapping("/search")
    // public ResponseEntity<List<FlightResponseDTO>> search(@RequestParam String from,
    //                                                       @RequestParam String to,
    //                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    //     return ResponseEntity.ok(
    //         flightService.search(from, to, date).stream().map(FlightResponseDTO::new).toList()
    //     );
    // }
    //
    // Delay example:
    // @PatchMapping("/{id}/delay")
    // public ResponseEntity<FlightResponseDTO> delay(@PathVariable Long id, @RequestParam long minutes) {
    //     Flight delayed = flightService.delay(id, Duration.ofMinutes(minutes));
    //     return ResponseEntity.ok(new FlightResponseDTO(delayed));
    // }

    // ---------- Error handling ----------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
