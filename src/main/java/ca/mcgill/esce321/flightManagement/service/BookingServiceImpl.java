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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl {

    private final BookingRepository bookingRepository;
    private final PersonRepository personRepository;
    private final SeatRepository seatRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              PersonRepository personRepository,
                              SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.personRepository = personRepository;
        this.seatRepository = seatRepository;
    }

    // ----------------------- Create -----------------------
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        if (dto.getCustomerId() == null) throw new IllegalArgumentException("customerId is required");
        if (dto.getSeatId() == null)     throw new IllegalArgumentException("seatId is required");

        // Load & validate customer
        Person person = personRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("No person with id " + dto.getCustomerId()));
        if (!(person instanceof Customer customer)) {
            throw new IllegalArgumentException("Person " + dto.getCustomerId() + " is not a Customer");
        }

        // Load & validate seat (and flight)
        Seat seat = seatRepository.findById(dto.getSeatId())
                .orElseThrow(() -> new IllegalArgumentException("No seat with id " + dto.getSeatId()));
        Flight flight = seat.getFlight();
        if (flight == null) {
            throw new IllegalStateException("Seat " + seat.getSeatId() + " has no associated Flight");
        }

        // One booking per (customer, flight)
        boolean alreadyHasBookingForFlight = bookingRepository.findAll().stream().anyMatch(b ->
                b.getCustomer() != null
                && b.getCustomer().getId().equals(customer.getId())
                && b.getSeat() != null
                && b.getSeat().getFlight() != null
                && b.getSeat().getFlight().equals(flight)
        );
        if (alreadyHasBookingForFlight) {
            throw new IllegalArgumentException("Customer already has a booking for this flight");
        }

        // Is seat already taken by an 'active' booking? (CONFIRMED or WAITLIST)
        boolean seatTaken = bookingRepository.findAll().stream().anyMatch(b ->
                b.getSeat() != null
                && b.getSeat().equals(seat)
                && isActiveStatus(b.getBookingStatus())
        );
        if (seatTaken) {
            throw new IllegalArgumentException("Seat is already booked");
        }

        // Build & persist
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSeat(seat);
        booking.setBookingDate(dto.getBookingDate() != null ? dto.getBookingDate() : LocalDateTime.now());
        booking.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : PaymentStatus.NOTPAID);
        booking.setBookingStatus(dto.getBookingStatus() != null ? dto.getBookingStatus() : BookingStatus.CONFIRMED);

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
        return bookingRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponseDTO> listBookingsByCustomer(Long customerId) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getCustomer() != null && b.getCustomer().getId().equals(customerId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ----------------------- Update -----------------------
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
        if (currentSeat == null || currentSeat.getFlight() == null) {
            throw new IllegalStateException("Current booking has no flight-bound seat");
        }
        Flight flight = currentSeat.getFlight();

        Seat newSeat = seatRepository.findById(newSeatId)
                .orElseThrow(() -> new IllegalArgumentException("No seat with id " + newSeatId));
        if (newSeat.getFlight() == null || !newSeat.getFlight().equals(flight)) {
            throw new IllegalArgumentException("New seat must belong to the same flight");
        }

        boolean newSeatTaken = bookingRepository.findAll().stream().anyMatch(x ->
                x.getSeat() != null
                && x.getSeat().equals(newSeat)
                && isActiveStatus(x.getBookingStatus())
        );
        if (newSeatTaken) throw new IllegalArgumentException("New seat is already booked");

        b.setSeat(newSeat);
        return toResponse(b);
    }
    @Transactional
    public BookingResponseDTO updateBooking(Long bookingId, BookingRequestDTO dto) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + bookingId));

        // Update date / payment / booking status if provided
        if (dto.getBookingDate() != null)    b.setBookingDate(dto.getBookingDate());
        if (dto.getPaymentStatus() != null)  b.setPaymentStatus(dto.getPaymentStatus());
        if (dto.getBookingStatus() != null)  b.setBookingStatus(dto.getBookingStatus());

        // Optional seat change (same flight, and seat must be free)
        if (dto.getSeatId() != null) {
            Seat currentSeat = b.getSeat();
            if (currentSeat == null || currentSeat.getFlight() == null) {
                throw new IllegalStateException("Current booking has no flight-bound seat");
            }
            Flight flight = currentSeat.getFlight();

            Seat newSeat = seatRepository.findById(dto.getSeatId())
                    .orElseThrow(() -> new IllegalArgumentException("No seat with id " + dto.getSeatId()));
            if (newSeat.getFlight() == null || !newSeat.getFlight().equals(flight)) {
                throw new IllegalArgumentException("New seat must belong to the same flight");
            }

            boolean newSeatTaken = bookingRepository.findAll().stream().anyMatch(x ->
                    x.getSeat() != null
                            && x.getSeat().equals(newSeat)
                            && (x.getBookingStatus() == BookingStatus.CONFIRMED
                                || x.getBookingStatus() == BookingStatus.WAITLIST)
            );
            if (newSeatTaken) throw new IllegalArgumentException("New seat is already booked");

            b.setSeat(newSeat);
        }

        // JPA will flush on transaction end
        return toResponse(b);
    }

    // ----------------------- Delete (used by tests) -----------------------
    @Transactional
    public void deleteBooking(Long bookingId) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + bookingId));
        bookingRepository.delete(b);
    }


    // ----------------------- Cancel & Waitlist Promotion -----------------------
    @Transactional
    public void cancelByCustomer(Long bookingId) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + bookingId));

        b.setBookingStatus(BookingStatus.CANCELLED_BY_CUSTOMER);

        Seat releasedSeat = b.getSeat();
        if (releasedSeat == null || releasedSeat.getFlight() == null) return;

        Flight flight = releasedSeat.getFlight();
        SeatClass clazz = releasedSeat.getSeatClass();

        // Earliest WAITLIST for same flight & class (FIFO by bookingDate)
        Optional<Booking> next = bookingRepository.findAll().stream()
                .filter(x ->
                        x.getBookingStatus() == BookingStatus.WAITLIST
                        && x.getSeat() != null
                        && x.getSeat().getFlight() != null
                        && x.getSeat().getFlight().equals(flight)
                        && x.getSeat().getSeatClass() == clazz
                )
                .min(Comparator.comparing(Booking::getBookingDate));

        if (next.isPresent()) {
            Booking nb = next.get();
            nb.setSeat(releasedSeat);
            nb.setBookingStatus(BookingStatus.CONFIRMED);
        }
    }

    // ----------------------- Helpers -----------------------
    private boolean isActiveStatus(BookingStatus s) {
        return s == BookingStatus.CONFIRMED || s == BookingStatus.WAITLIST;
    }

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

