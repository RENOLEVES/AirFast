package ca.mcgill.esce321.flightManagement.controller;

import ca.mcgill.esce321.flightManagement.dto.request.BookingRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.service.BookingServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingServiceImpl bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(@RequestBody BookingRequestDTO request) {
        BookingResponseDTO created = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> list() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> update(@PathVariable Long id, @RequestBody BookingRequestDTO request) {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelByCustomer(@PathVariable Long id) {
        bookingService.cancelByCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
