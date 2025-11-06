package ca.mcgill.esce321.flightManagement.Controller;

import ca.mcgill.esce321.flightManagement.Dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.SeatResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.service.SeatServiceImpl;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.mcgill.esce321.flightManagement.Dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.SeatResponseDTO;
import ca.mcgill.esce321.flightManagement.service.SeatServiceImpl;



// import ca.mcgill.ecse321.eventregistration.dto.PersonResponseDto;
// import ca.mcgill.ecse321.eventregistration.model.Person;
// import ca.mcgill.ecse321.eventregistration.service.PersonService;


@RestController
@RequestMapping("/api/seats")
@Validated
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
