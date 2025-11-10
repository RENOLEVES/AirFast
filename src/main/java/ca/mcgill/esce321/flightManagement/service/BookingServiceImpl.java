package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.dto.request.BookingRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl {

    private final BookingRepository bookingRepository;
    private final PersonRepository personRepository;   // used to load Customer by id
    private final SeatRepository seatRepository;       // used to load Seat (and thus Flight)

    public BookingServiceImpl(BookingRepository bookingRepository,
                              PersonRepository personRepository,
                              SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.personRepository = personRepository;
        this.seatRepository = seatRepository;
    }

    // ----------------------- Create -----------------------

    /**
     * Creates a booking for a given customer and seat.
     * Rules:
     *  - customerId must be a Customer
     *  - seatId must exist
     *  - seat's flight must not already be booked by this customer (1 booking per flight per customer)
     *  - if request.bookingStatus/paymentStatus are null, sensible defaults are applied
     */
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        if (dto.getCustomerId() == null) throw new IllegalArgumentException("customerId is required");
        if (dto.getSeatId() == null)     throw new IllegalArgumentException("seatId is required");

        // Load and validate customer
        Person person = personRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("No person with id " + dto.getCustomerId()));
        if (!(person instanceof Customer customer)) {
            throw new IllegalArgumentException("Person " + dto.getCustomerId() + " is not a Customer");
        }

        // Load and validate seat
        Seat seat = seatRepository.findById(dto.getSeatId())
                .orElseThrow(() -> new IllegalArgumentException("No seat with id " + dto.getSeatId()));
        Flight flight = seat.getFlight();
        if (flight == null) {
            throw new IllegalStateException("Seat " + seat.getSeatId() + " has no associated Flight");
        }

        // Enforce: one booking per (customer, flight)
        boolean exists = bookingRepository.existsByCustomerAndFlight(customer, flight);
        if (exists) {
            throw new IllegalArgumentException("Customer already has a booking for this flight");
        }

        // Optional: ensure seat isn't already taken by a confirmed booking
        boolean seatTaken = bookingRepository.existsBySeatAndActive(seat);
        if (seatTaken) {
            throw new IllegalArgumentException("Seat is already booked");
        }

        // Build entity
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSeat(seat);
        booking.setBookingDate(dto.getBookingDate() != null ? dto.getBookingDate() : LocalDateTime.now());
        booking.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : PaymentStatus.NOTPAID);
        booking.setBookingStatus(dto.getBookingStatus() != null ? dto.getBookingStatus() : BookingStatus.CONFIRMED);

        // Persist
        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    // ----------------------- Read -----------------------

    public BookingResponseDTO getBookingById(Long id) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + id));
        return toResponse(b);
    }

    public List<BookingResponseDTO> listAllBookings() {
        return bookingRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<BookingResponseDTO> listBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ----------------------- Update (payment / seat change) -----------------------

    @Transactional
    public BookingResponseDTO updatePaymentStatus(Long bookingId, PaymentStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("newStatus is required");
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + bookingId));
        b.setPaymentStatus(newStatus);
        return toResponse(b);
    }

    /**
     * Change seat for a booking within the same flight.
     * Validates: same flight, new seat not taken, one booking per customer per flight still holds.
     */
    @Transactional
    public BookingResponseDTO changeSeat(Long bookingId, Long newSeatId) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + bookingId));
        Seat currentSeat = b.getSeat();
        Flight flight = currentSeat.getFlight();

        Seat newSeat = seatRepository.findById(newSeatId)
                .orElseThrow(() -> new IllegalArgumentException("No seat with id " + newSeatId));
        if (newSeat.getFlight() == null || !newSeat.getFlight().equals(flight)) {
            throw new IllegalArgumentException("New seat must belong to the same flight");
        }
        if (bookingRepository.existsBySeatAndActive(newSeat)) {
            throw new IllegalArgumentException("New seat is already booked");
        }

        b.setSeat(newSeat);
        return toResponse(b);
    }

    // ----------------------- Cancel -----------------------

    /**
     * Customer cancels a booking:
     *  - bookingStatus -> CANCELLED_BY_CUSTOMER
     *  - Optionally promote earliest WAITLIST (same flight & seat class) to CONFIRMED
     */
    @Transactional
    public void cancelByCustomer(Long bookingId) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + bookingId));

        b.setBookingStatus(BookingStatus.CANCELLED_BY_CUSTOMER);

        // Promote earliest waitlist (FIFO) for same flight & class
        Seat releasedSeat = b.getSeat();
        if (releasedSeat != null) {
            Flight flight = releasedSeat.getFlight();
            SeatClass clazz = releasedSeat.getSeatClass();

            Optional<Booking> next = bookingRepository.findFirstWaitlistFIFO(flight, clazz);
            if (next.isPresent()) {
                Booking nb = next.get();
                nb.setSeat(releasedSeat); // assign the freed seat
                nb.setBookingStatus(BookingStatus.CONFIRMED);
                // payment status stays as-is (your business rule may require re-payment confirmation)
            }
        }
    }

    // ----------------------- Mapping -----------------------

    private BookingResponseDTO toResponse(Booking b) {
        Long customerId = (b.getCustomer() != null) ? b.getCustomer().getId() : null;
        Long seatId = (b.getSeat() != null) ? b.getSeat().getSeatId() : null;

        return new BookingResponseDTO(
                b.getBookingId(),
                customerId,
                seatId,
                b.getBookingDate(),
                b.getPaymentStatus(),
                b.getBookingStatus()
        );
    }
}
