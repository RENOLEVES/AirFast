package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    public FlightServiceImpl(FlightRepository flightRepository,
                             BookingRepository bookingRepository,
                             SeatRepository seatRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
    }

    // ---------------------- Create ----------------------

    @Transactional
    public FlightResponseDTO createFlight(FlightRequestDTO dto) {
        validateCreate(dto);

        Flight f = new Flight();
        applyRequestToEntity(dto, f);
        // Initial defaults
        f.setActive(true);
        // seatsRemaining is typically derived; if your model persists it, initialize = capacity
        f.setSeatsRemaining(f.getCapacity());

        Flight saved = flightRepository.save(f);

        return toResponse(saved);
    }

    // ---------------------- Read ----------------------

    public FlightResponseDTO getFlightById(long flightId) {
        Flight f = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight " + flightId + " not found."));
        return toResponse(f);
    }

    public List<FlightResponseDTO> listAllFlights() {
        return flightRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<FlightResponseDTO> searchFlights(String from, String to, LocalDate date) {
        // If you have a custom search query in repo, use it. Else, add it later.
        // Example if implemented: flightRepository.search(from, to, date)
        return flightRepository.findAll().stream()
                .filter(f -> Objects.equals(f.getDepartLocation(), from))
                .filter(f -> Objects.equals(f.getArrivalLocation(), to))
                .filter(f -> f.getDepartTime() != null && f.getDepartTime().toLocalDate().equals(date))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ---------------------- Update ----------------------

    @Transactional
    public FlightResponseDTO updateFlight(long flightId, FlightRequestDTO dto) {
        Flight f = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight " + flightId + " not found."));

        // If capacity is changing, guard against shrinking below active bookings
        if (dto.getCapacity() > 0 && dto.getCapacity() != f.getCapacity()) {
            int active = countActiveBookingsForFlight(f);
            if (dto.getCapacity() < active) {
                throw new IllegalArgumentException("Cannot reduce capacity below current active bookings (" + active + ").");
            }
            f.setCapacity(dto.getCapacity());
            // seatsRemaining stays derived; do not blindly overwrite here
        }

        // Apply the rest of the fields (null-safe)
        applyRequestToEntityPartial(dto, f);

        return toResponse(f);
    }

    // ---------------------- Cancel/Delete ----------------------

    @Transactional
    public void cancelFlight(long flightId) {
        Flight f = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight " + flightId + " not found."));

        // Mark flight as inactive/cancelled. If you have an enum status, set it too.
        f.setActive(false);
        if (hasFlightStatusEnum(f)) {
            f.setStatus(FlightStatus.CANCELLED);
        }

        // Cascade: mark related bookings as CANCELLED_BY_AIRLINE (keep history)
        List<Booking> bookings = bookingRepository.findByFlight(f);
        for (Booking b : bookings) {
            if (b.getBookingStatus() != BookingStatus.CANCELLED_BY_CUSTOMER) {
                b.setBookingStatus(BookingStatus.CANCELLED_BY_AIRLINE);
            }
        }
    }

    // ---------------------- Delay (Shift times) ----------------------

    @Transactional
    public FlightResponseDTO delayFlight(long flightId, int hours) {
        if (hours == 0) return getFlightById(flightId);

        Flight f = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight " + flightId + " not found."));

        if (f.getDepartTime() != null) {
            f.setDepartTime(f.getDepartTime().plus(Duration.ofHours(hours)));
        }
        if (f.getArrivalTime() != null) {
            f.setArrivalTime(f.getArrivalTime().plus(Duration.ofHours(hours)));
        }

        if (hasFlightStatusEnum(f)) {
            f.setStatus(FlightStatus.DELAYED);
        }

        return toResponse(f);
    }

    // ---------------------- Helpers ----------------------

    private void validateCreate(FlightRequestDTO d) {
        require(d.getFlightNumber(), "flightNumber");
        require(d.getDepartLocation(), "departLocation");
        require(d.getArrivalLocation(), "arrivalLocation");
        Objects.requireNonNull(d.getExpectedDepartTime(), "expectedDepartTime is required");
        if (d.getCapacity() <= 0) throw new IllegalArgumentException("capacity must be > 0");
    }

    /** Maps a full create request to entity. */
    private void applyRequestToEntity(FlightRequestDTO d, Flight f) {
        f.setFlightNumber(d.getFlightNumber());
        f.setDepartLocation(d.getDepartLocation());
        f.setArrivalLocation(d.getArrivalLocation());
        // Business convention:
        // expectedDepartTime = planned schedule, departTime = initially same as expected (shifts if delayed)
        f.setExpectedDepartTime(d.getExpectedDepartTime());
        f.setDepartTime(d.getExpectedDepartTime());
        f.setArrivalTime(d.getArrivalTime()); // optional; or compute from flightTime
        f.setFlightTime(d.getFlightTime());
        f.setCapacity(d.getCapacity());
        f.setRecurring(d.isRecurring());
        f.setActive(d.isActive()); // if client sends it; otherwise force true on create
    }

    private void applyRequestToEntityPartial(FlightRequestDTO d, Flight f) {
        if (d.getFlightNumber() != null) f.setFlightNumber(d.getFlightNumber());
        if (d.getDepartLocation() != null) f.setDepartLocation(d.getDepartLocation());
        if (d.getArrivalLocation() != null) f.setArrivalLocation(d.getArrivalLocation());
        if (d.getExpectedDepartTime() != null) f.setExpectedDepartTime(d.getExpectedDepartTime());
        if (d.getDepartTime() != null) f.setDepartTime(d.getDepartTime());
        if (d.getArrivalTime() != null) f.setArrivalTime(d.getArrivalTime());
        if (d.getFlightTime() != 0.0) f.setFlightTime(d.getFlightTime());
        f.setRecurring(d.isRecurring()); // if you want to allow toggling

    }

    private FlightResponseDTO toResponse(Flight f) {
        // seatsRemaining: recompute if you want (e.g., capacity - confirmed bookings)
        int seatsRemaining = computeSeatsRemaining(f);

        FlightResponseDTO r = new FlightResponseDTO(
                f.getFlightId(),
                f.getCapacity(),
                seatsRemaining,
                f.getDepartTime(),
                f.getArrivalTime(),
                f.getExpectedDepartTime(),
                f.getDepartLocation(),
                f.getArrivalLocation(),
                f.getFlightNumber(),
                f.getFlightTime(),
                f.isRecurring(),
                f.isActive()
        );
        return r;
    }

    private int computeSeatsRemaining(Flight f) {
        int capacity = f.getCapacity();
        int activeConfirmed = countConfirmedBookingsForFlight(f);
        return Math.max(0, capacity - activeConfirmed);
    }

    private int countActiveBookingsForFlight(Flight f) {
        List<Booking> list = bookingRepository.findByFlight(f);
        int count = 0;
        for (Booking b : list) {
            if (b.getBookingStatus() == BookingStatus.CONFIRMED ||
                b.getBookingStatus() == BookingStatus.WAITLIST) {
                count++;
            }
        }
        return count;
    }

    private int countConfirmedBookingsForFlight(Flight f) {
        List<Booking> list = bookingRepository.findByFlight(f);
        int count = 0;
        for (Booking b : list) {
            if (b.getBookingStatus() == BookingStatus.CONFIRMED) count++;
        }
        return count;
    }

    private static void require(String s, String field) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(field + " is required");
    }

    private static boolean hasFlightStatusEnum(Flight f) {
        try {
            Flight.class.getDeclaredMethod("setStatus", FlightStatus.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
