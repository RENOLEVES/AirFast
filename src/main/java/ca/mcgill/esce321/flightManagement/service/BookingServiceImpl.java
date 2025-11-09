package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.dto.request.BookingRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class BookingServiceImpl {

    private final BookingRepository bookingRepository;
    private final PersonRepository personRepository;  // to load Customer
    private final SeatRepository seatRepository;      // to load Seat (and Flight)

    public BookingServiceImpl(BookingRepository bookingRepository,
                              PersonRepository personRepository,
                              SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.personRepository = personRepository;
        this.seatRepository = seatRepository;
    }

    // --------- CREATE ----------
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO dto) {
        if (dto.getCustomerId() == null) throw new IllegalArgumentException("customerId is required");
        if (dto.getSeatId() == null)     throw new IllegalArgumentException("seatId is required");

        Person p = personRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("No person with id " + dto.getCustomerId()));
        if (!(p instanceof Customer customer)) {
            throw new IllegalArgumentException("Provided person is not a Customer");
        }

        Seat seat = seatRepository.findById(dto.getSeatId())
                .orElseThrow(() -> new IllegalArgumentException("No seat with id " + dto.getSeatId()));

        // Simple guards without custom repo methods:
        // 1) one booking per (customer, flight)
        Flight flight = seat.getFlight();
        boolean alreadyBookedSameFlight = bookingRepository.findAll().stream()
                .anyMatch(b -> b.getCustomer() != null
                        && b.getCustomer().getId().equals(customer.getId())
                        && b.getSeat() != null
                        && b.getSeat().getFlight() != null
                        && b.getSeat().getFlight().equals(flight));
        if (alreadyBookedSameFlight) {
            throw new IllegalArgumentException("Customer already has a booking for this flight");
        }

        // 2) seat not already taken (confirmed or waitlist depending on your rule)
        boolean seatTaken = bookingRepository.findAll().stream()
                .anyMatch(b -> b.getSeat() != null
                        && b.getSeat().getSeatId().equals(seat.getSeatId())
                        && (b.getBookingStatus() == BookingStatus.CONFIRMED
                            || b.getBookingStatus() == BookingStatus.WAITLIST));
        if (seatTaken) {
            throw new IllegalArgumentException("Seat is already booked");
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSeat(seat);
        booking.setBookingDate(dto.getBookingDate() != null ? dto.getBookingDate() : LocalDateTime.now());
        booking.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : PaymentStatus.NOTPAID);
        booking.setBookingStatus(dto.getBookingStatus() != null ? dto.getBookingStatus() : BookingStatus.CONFIRMED);

        Booking saved = bookingRepository.save(booking);
        return toResponse(saved);
    }

    // --------- READ ----------
    public BookingResponseDTO getBookingById(Long id) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + id));
        return toResponse(b);
    }

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<BookingResponseDTO> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getCustomer() != null && b.getCustomer().getId().equals(customerId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // --------- UPDATE ----------
    @Transactional
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO dto) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + id));

        // allow payment/status updates; date change rarely needed but supported
        if (dto.getPaymentStatus() != null) b.setPaymentStatus(dto.getPaymentStatus());
        if (dto.getBookingStatus() != null) b.setBookingStatus(dto.getBookingStatus());
        if (dto.getBookingDate() != null)   b.setBookingDate(dto.getBookingDate());

        // seat change (same flight recommended; leaving business rule simple here)
        if (dto.getSeatId() != null && (b.getSeat() == null || !b.getSeat().getSeatId().equals(dto.getSeatId()))) {
            Seat newSeat = seatRepository.findById(dto.getSeatId())
                    .orElseThrow(() -> new IllegalArgumentException("No seat with id " + dto.getSeatId()));
            // naive “seat not taken” check
            boolean seatTaken = bookingRepository.findAll().stream()
                    .anyMatch(x -> x.getSeat() != null
                            && x.getSeat().getSeatId().equals(newSeat.getSeatId())
                            && !x.getBookingId().equals(b.getBookingId())
                            && (x.getBookingStatus() == BookingStatus.CONFIRMED
                                || x.getBookingStatus() == BookingStatus.WAITLIST));
            if (seatTaken) throw new IllegalArgumentException("New seat is already booked");
            b.setSeat(newSeat);
        }

        return toResponse(b);
    }

    // --------- DELETE / CANCEL ----------
    @Transactional
    public void deleteBooking(Long id) {
        Optional<Booking> opt = bookingRepository.findById(id);
        if (opt.isEmpty()) throw new IllegalArgumentException("No booking with id " + id);
        bookingRepository.delete(opt.get());
    }

    @Transactional
    public void cancelByCustomer(Long id) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No booking with id " + id));
        b.setBookingStatus(BookingStatus.CANCELLED_BY_CUSTOMER);
        // Promotion from waitlist can be added later when your team finalizes the rule.
    }

    // --------- mapper ----------
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
