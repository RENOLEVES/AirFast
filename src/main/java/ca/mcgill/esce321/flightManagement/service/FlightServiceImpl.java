package ca.mcgill.ecse321.flightManagement.service;

// to be implemented:
// import ca.mcgill.ecse321.flightManagement.dto.FlightCreateDTO;
// import ca.mcgill.ecse321.flightManagement.dto.FlightUpdateDTO;

import ca.mcgill.esce321.flightManagement.model.Booking;
import ca.mcgill.esce321.flightManagement.model.BookingStatus;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.FlightStatus;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository; 
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
package ca.mcgill.ecse321.flightManagement.service;

@Service
public class FlightServiceImpl implements FlightService { ... }



@Service
public class FlightServiceImpl implements FlightService {


    Flight createFlight(FlightRequestDTO dto);
    Flight getFlightById(Long id);
    List<Flight> getAllFlights();
    Flight updateFlight(Long id, FlightRequestDTO dto);
    void deleteFlight(Long id);

    private final FlightRepository flightRepo;
    private final BookingRepository bookingRepo;

    public FlightServiceImpl(FlightRepository flightRepo, BookingRepository bookingRepo) {
        this.flightRepo = flightRepo;
        this.bookingRepo = bookingRepo;
    }

    // ---------- CREATE ----------

    @Override
    @Transactional
    public Flight create(FlightCreateDTO d) {
        require(d.flightNumber(), "flightNumber");
        require(d.departLocation(), "departLocation");
        require(d.arrivalLocation(), "arrivalLocation");
        Objects.requireNonNull(d.departTime(), "departTime is required");
        Objects.requireNonNull(d.arriveTime(), "arriveTime is required");
        Objects.requireNonNull(d.capacity(), "capacity is required");

        // Optional: reject duplicate (same number + departTime) if you add this finder to repo
        // if (flightRepo.existsByFlightNumberAndDepartTime(d.flightNumber(), d.departTime()))
        //     throw new IllegalArgumentException("Duplicate flightNumber for same departure time");

        Flight f = new Flight();
        f.setFlightNumber(d.flightNumber());
        f.setDepartLocation(d.departLocation());
        f.setArrivalLocation(d.arrivalLocation());
        f.setDepartTime(d.departTime());
        f.setArriveTime(d.arriveTime());
        f.setCapacity(d.capacity());
        f.setStatus(FlightStatus.SCHEDULED);
        return flightRepo.save(f);
    }

    // ---------- UPDATE ------------

    @Override
    @Transactional
    public Flight update(Long id, FlightUpdateDTO d) {
        Flight f = flightRepo.findById(id).orElseThrow(() -> notFound(id));

        if (d.flightNumber() != null) f.setFlightNumber(d.flightNumber());
        if (d.departLocation() != null) f.setDepartLocation(d.departLocation());
        if (d.arrivalLocation() != null) f.setArrivalLocation(d.arrivalLocation());
        if (d.departTime() != null) f.setDepartTime(d.departTime());
        if (d.arriveTime() != null) f.setArriveTime(d.arriveTime());

        if (d.capacity() != null) {
            // Safety: do not shrink below current active bookings (confirmed+waitlist)
            int currentActive = bookingRepo.countActiveByFlight(id); // see repo helper below
            if (d.capacity() < currentActive) {
                throw new IllegalArgumentException("Cannot set capacity below current active bookings (" + currentActive + ")");
            }
            f.setCapacity(d.capacity());
        }

        return f; // managed entity; will be flushed by @Transactional
    }

    // ---------- DELETE (airline cancel) ----------

    @Override
    @Transactional
    public void delete(Long id) {
        Flight f = flightRepo.findById(id).orElseThrow(() -> notFound(id));
        // mark flight as cancelled
        f.setStatus(FlightStatus.CANCELLED);

        // cascade effect on bookings (CANCELLED_BY_AIRLINE), but keep history
        List<Booking> bookings = bookingRepo.findByFlight(f);
        for (Booking b : bookings) {
            if (b.getBookingStatus() != BookingStatus.CANCELLED_BY_CUSTOMER) {
                b.setBookingStatus(BookingStatus.CANCELLED_BY_AIRLINE);
            }
        }
    }

    // ---------- READ ----------

    @Override
    public Flight get(Long id) {
        return flightRepo.findById(id).orElseThrow(() -> notFound(id));
    }

    @Override
    public List<Flight> search(String from, String to, LocalDate date) {
        // Implemented as a custom JPQL query in FlightRepository
        return flightRepo.search(from, to, date);
    }

    // ---------- DELAY ----------

    @Override
    @Transactional
    public Flight delay(Long id, Duration delay) {
        Flight f = flightRepo.findById(id).orElseThrow(() -> notFound(id));
        f.setDepartTime(f.getDepartTime().plus(delay));
        f.setArriveTime(f.getArriveTime().plus(delay));
        f.setStatus(FlightStatus.DELAYED);
        return f;
    }

    // ---------- helpers ----------

    private static void require(String s, String field) {
        if (s == null || s.isBlank()) throw new IllegalArgumentException(field + " is required");
    }

    private static IllegalArgumentException notFound(Long id) {
        return new IllegalArgumentException("Flight " + id + " not found");
    }
}
