package ca.mcgill.esce321.flightManagement.controller; // <- keep consistent with your folders

import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PersonResponseDTO;
import ca.mcgill.esce321.flightManagement.service.ManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "*")
@Validated
public class ManagerController {

    @Autowired
    private ManagerServiceImpl managerService;

    // CREATE
    @PostMapping
    public ResponseEntity<ManagerResponseDTO> create(@RequestBody ManagerRequestDTO request) {
        final ManagerResponseDTO created = managerService.createManager(request);
        // Location header is optional but nice to have:
        return ResponseEntity
                .created(URI.create("/api/managers/" + created.getId()))
                .body(created);
    }

    // READ one
    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> getById(@PathVariable("id") long id) {

        return ResponseEntity.ok(managerService.findManagerById(id));
    }

    // READ all
    @GetMapping
    public List<ManagerResponseDTO> getAll() {
        return managerService.findAllManagers();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ManagerResponseDTO> update(@PathVariable("id") long id,
                                     @RequestBody ManagerRequestDTO request) {
        return ResponseEntity.ok(managerService.updateManager(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build();
    }

    
    // --- Flights management ---

    @PostMapping("/flights")
    public ResponseEntity<Boolean> addFlight(@RequestBody FlightRequestDTO dto) {
        boolean success = managerService.addFlight(dto);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/flights/{flightId}")
    public ResponseEntity<Boolean> updateFlight(
            @PathVariable long flightId,
            @RequestBody FlightRequestDTO dto
    ) {
        boolean success = managerService.updateFlight(flightId, dto);
        return ResponseEntity.ok(success);
    }

    @DeleteMapping("/flights/{flightId}")
    public ResponseEntity<Boolean> deleteFlight(@PathVariable long flightId) {
        boolean success = managerService.deleteFlight(flightId);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/flights/{flightId}/recurring")
    public ResponseEntity<Boolean> makeFlightRecurring(@PathVariable long flightId) {
        boolean success = managerService.makeFlightRecurring(flightId);
        return ResponseEntity.ok(success);
    }

    @PutMapping("/flights/{flightId}/assign")
    public ResponseEntity<Boolean> assignFlight(
            @PathVariable long flightId,
            @RequestBody List<Long> employeeIds
    ) {
        boolean success = managerService.assignFlight(flightId, employeeIds);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/flights")
    public ResponseEntity<List<FlightResponseDTO>> viewAllFlights() {
        List<FlightResponseDTO> flights = managerService.viewAllFlights();
        return ResponseEntity.ok(flights);
    }

    @GetMapping("/flights/{flightId}/stats")
    public ResponseEntity<FlightResponseDTO> viewFlightStats(@PathVariable long flightId) {
        FlightResponseDTO stats = managerService.viewFlightStats(flightId);
        return ResponseEntity.ok(stats);
    }

    // --- Bookings management ---

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<Boolean> deleteBooking(@PathVariable long bookingId) {
        boolean success = managerService.deleteBooking(bookingId);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponseDTO>> viewAllBookings() {
        List<BookingResponseDTO> bookings = managerService.viewAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // --- Persons management ---

    @GetMapping("/persons")
    public ResponseEntity<List<PersonResponseDTO>> viewAllPersons() {
        List<PersonResponseDTO> persons = managerService.viewAllPersons();
        return ResponseEntity.ok(persons);
    }

    // --- Seat management ---

    @PutMapping("/seats/{seatId}/price")
    public ResponseEntity<Boolean> setSeatPrice(
            @PathVariable long seatId,
            @RequestBody double newPrice
    ) {
        boolean success = managerService.setSeatPrice(seatId, newPrice);
        return ResponseEntity.ok(success);
    }

    // --- Employee creation (Pilot / FlightAttendant) ---

    @PostMapping("/employees")
    public ResponseEntity<Boolean> createEmployee(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String type
    ) {
        boolean success = managerService.createEmployee(email, password, firstName, lastName, type);
        return ResponseEntity.ok(success);
    }



    // Map your service's IllegalArgumentException to HTTP codes
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException ex) {
        // Use 404 if it's “not found”; otherwise 400 is fine.
        String msg = ex.getMessage() == null ? "Bad request" : ex.getMessage();
        HttpStatus status = msg != null && msg.toLowerCase().contains("no seat")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
