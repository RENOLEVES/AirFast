package ca.mcgill.esce321.flightManagement.Controller;

import ca.mcgill.esce321.flightManagement.Dto.request.FlightAttendantRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.FlightAttendantResponseDTO;
import ca.mcgill.esce321.flightManagement.model.FlightAttendant;
import ca.mcgill.esce321.flightManagement.service.FlightAttendantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight-attendants")
@CrossOrigin(origins = "*")
public class FlightAttendantController {

    @Autowired
    private FlightAttendantService flightAttendantService;

    // CREATE
    @PostMapping
    public ResponseEntity<FlightAttendantResponseDTO> create(@RequestBody FlightAttendantRequestDTO request) {
        FlightAttendant created = flightAttendantService.createFlightAttendant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new FlightAttendantResponseDTO(created));
    }

    // READ one
    @GetMapping("/{id}")
    public ResponseEntity<FlightAttendantResponseDTO> getById(@PathVariable Long id) {
        FlightAttendant fa = flightAttendantService.getFlightAttendantById(id);
        return ResponseEntity.ok(new FlightAttendantResponseDTO(fa));
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<FlightAttendantResponseDTO>> getAll() {
        List<FlightAttendant> all = flightAttendantService.getAllFlightAttendants();
        return ResponseEntity.ok(all.stream().map(FlightAttendantResponseDTO::new).toList());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<FlightAttendantResponseDTO> update(@PathVariable Long id,
                                                             @RequestBody FlightAttendantRequestDTO request) {
        FlightAttendant updated = flightAttendantService.updateFlightAttendant(id, request);
        return ResponseEntity.ok(new FlightAttendantResponseDTO(updated));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flightAttendantService.deleteFlightAttendant(id);
        return ResponseEntity.noContent().build();
    }

    // Error mapping (keep consistent with your other controllers)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
