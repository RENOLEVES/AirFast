package ca.mcgill.esce321.flightManagement.controller;

import ca.mcgill.esce321.flightManagement.dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.SeatResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.service.SeatServiceImpl;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController
// @RequestMapping("/api/seats")
// @CrossOrigin(origins = "*")
import org.springframework.validation.annotation.Validated;

import java.util.List;



import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.SeatResponseDTO;
import ca.mcgill.esce321.flightManagement.service.SeatServiceImpl;



// import ca.mcgill.ecse321.eventregistration.dto.PersonResponseDto;
// import ca.mcgill.ecse321.eventregistration.model.Person;
// import ca.mcgill.ecse321.eventregistration.service.PersonService;


@RestController
@RequestMapping("/api/seats")
@Validated
@CrossOrigin(origins = "http://localhost:5173")
public class SeatController {

    @Autowired
    private SeatServiceImpl seatService;


    @PostMapping
    public ResponseEntity<SeatResponseDTO> createSeat(@RequestBody SeatRequestDTO request) {
        // seatService.createSeat() returns SeatResponseDTO
        SeatResponseDTO created = seatService.createSeat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get seat by ID
     */
     @GetMapping("/{id}")
    public ResponseEntity<SeatResponseDTO> getSeatById(@PathVariable long id) {
        SeatResponseDTO seat = seatService.getSeatById(id); // returns SeatResponseDTO
        return ResponseEntity.ok(seat);
    }

    @GetMapping("/flight/{id}")
    public ResponseEntity<List<SeatResponseDTO>> getSeatsByFlightId(@PathVariable long id) {
        List<SeatResponseDTO> seats = seatService.getSeatsByFlightId(id); // returns SeatResponseDTO
        return ResponseEntity.ok(seats);
    }

    /**
     * Get all seats
     */
    @GetMapping
    public ResponseEntity<List<SeatResponseDTO>> getAllSeats() {
        List<SeatResponseDTO> seats = seatService.getAllSeats();
        return ResponseEntity.ok(seats);
    }

    /**
     * Update a seat (for example, change availability or type)
     */
    @PutMapping("/{id}")
    public ResponseEntity<SeatResponseDTO> updateSeat(
            @PathVariable Long id,
            @RequestBody SeatRequestDTO request) {
        SeatResponseDTO updated = seatService.updateSeat(id, request);
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
