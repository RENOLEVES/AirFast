package ca.mcgill.esce321.flightManagement.controller.api;

import ca.mcgill.esce321.flightManagement.controller.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.SeatResponse;
import ca.mcgill.esce321.flightManagement.service.SeatServiceImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/seats")
@Validated
@CrossOrigin(origins = "http://localhost:5173")
public class SeatController {

    @Autowired
    private SeatServiceImpl seatService;


    @PostMapping
    public ResponseEntity<SeatResponse> createSeat(@RequestBody SeatRequestDTO request) {
        // seatService.createSeat() returns SeatResponseDTO
        SeatResponse created = seatService.createSeat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get seat by ID
     */
     @GetMapping("/{id}")
    public ResponseEntity<SeatResponse> getSeatById(@PathVariable long id) {
        SeatResponse seat = seatService.getSeatById(id); // returns SeatResponseDTO
        return ResponseEntity.ok(seat);
    }

    @GetMapping("/flight/{id}")
    public ResponseEntity<List<SeatResponse>> getSeatsByFlightId(@PathVariable long id) {
        List<SeatResponse> seats = seatService.getSeatsByFlightId(id); // returns SeatResponseDTO
        return ResponseEntity.ok(seats);
    }

    /**
     * Get all seats
     */
    @GetMapping
    public ResponseEntity<List<SeatResponse>> getAllSeats() {
        List<SeatResponse> seats = seatService.getAllSeats();
        return ResponseEntity.ok(seats);
    }

    /**
     * Update a seat (for example, change availability or type)
     */
    @PutMapping("/{id}")
    public ResponseEntity<SeatResponse> updateSeat(
            @PathVariable Long id,
            @RequestBody SeatRequestDTO request) {
        SeatResponse updated = seatService.updateSeat(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a seat (if unassigned or cancelled)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handle validation and missing resource errors
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
