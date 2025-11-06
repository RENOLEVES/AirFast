package ca.mcgill.esce321.flightManagement.Controller;

import ca.mcgill.esce321.flightManagement.Dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.SeatResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.service.SeatServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatController {

    @Autowired
    private SeatServiceImpl seatService;

    /**
     * Create a new seat
     */
    @PostMapping
    public ResponseEntity<SeatResponseDTO> createSeat(@RequestBody SeatRequestDTO request) {
        Seat created = seatService.createSeat(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new SeatResponseDto(created));
    }

    /**
     * Get seat by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SeatResponseDTO> getSeatById(@PathVariable Long id) {
        Seat seat = seatService.getSeatById(id);
        return ResponseEntity.ok(new SeatResponseDTO(seat));
    }

    /**
     * Get all seats
     */
    @GetMapping
    public ResponseEntity<List<SeatResponseDTO>> getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        List<SeatResponseDTO> dtos = seats.stream()
                                          .map(SeatResponseDTO::new)
                                          .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Update a seat (for example, change availability or type)
     */
    @PutMapping("/{id}")
    public ResponseEntity<SeatResponseDTO> updateSeat(
            @PathVariable Long id,
            @RequestBody SeatRequestDTO request) {
        Seat updated = seatService.updateSeat(id, request);
        return ResponseEntity.ok(new SeatResponseDto(updated));
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
