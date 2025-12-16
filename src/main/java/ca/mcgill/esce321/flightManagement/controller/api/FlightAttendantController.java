package ca.mcgill.esce321.flightManagement.controller.api;

import ca.mcgill.esce321.flightManagement.controller.request.FlightAttendantRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightAttendantResponse;
import ca.mcgill.esce321.flightManagement.model.FlightAttendant;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.service.FlightAttendantServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight-attendants")
@CrossOrigin(origins = "http://localhost:5173")
public class FlightAttendantController {

    @Autowired
    private FlightAttendantServiceImpl flightAttendantService;

    // ---------- CREATE ----------
    @PostMapping
    public ResponseEntity<FlightAttendantResponse> create(@RequestBody FlightAttendantRequestDTO request) {
        FlightAttendantResponse created = flightAttendantService.createFlightAttendant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------- READ ONE ----------
    @GetMapping("/{id}")
    public ResponseEntity<FlightAttendantResponse> getById(@PathVariable Long id) {
        Person p = flightAttendantService.getFlightAttendantById(id);
        if (p instanceof FlightAttendant fa) {
            return ResponseEntity.ok(toResponseDTO(fa));
        }
        // If your DB stores multiple Person subtypes under the same repo, guard against mismatches
        throw new IllegalArgumentException("FlightAttendant " + id + " not found");
    }

    // ---------- READ ALL ----------
    @GetMapping
    public ResponseEntity<List<FlightAttendantResponse>> getAll() {
        List<FlightAttendantResponse> flightAttendants = flightAttendantService.getAllFlightAttendants();
        return ResponseEntity.ok(flightAttendants);
    }

    // ---------- UPDATE ----------
    @PutMapping("/{id}")
    public ResponseEntity<FlightAttendantResponse> update(
            @PathVariable Long id,
            @RequestBody FlightAttendantRequestDTO request) {
        FlightAttendantResponse updated = flightAttendantService.updateFlightAttendant(id, request);
        return ResponseEntity.ok(updated);
    }

    // ---------- DELETE ----------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flightAttendantService.deleteFlightAttendant(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Error Handling ----------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }

    // ---------- mapper (mirrors your service's toResponseDTO) ----------
    private static FlightAttendantResponse toResponseDTO(FlightAttendant fa) {
        FlightAttendantResponse dto = new FlightAttendantResponse();
        dto.setId(fa.getId());
        dto.setEmail(fa.getEmail());
        dto.setFirstName(fa.getFirstName());
        dto.setLastName(fa.getLastName());
        // add other fields here if your response DTO includes them
        return dto;
    }
}

