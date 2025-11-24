package ca.mcgill.esce321.flightManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import jakarta.transaction.Transactional;

@Service
@Validated
public class FlightServiceImpl {

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    // --------- CREATE ----------
    @Transactional
    public FlightResponseDTO createFlight(FlightRequestDTO dto) {
        // Use the 7-arg ctor (no status)
        Flight f = new Flight(
                dto.getCapacity(),
                dto.getExpectedDepartTime(),
                dto.getDepartLocation(),
                dto.getArrivalLocation(),
                dto.getFlightNumber(),
                dto.getFlightTime(),
                dto.isRecurring(), null, null
        );

        if (dto.getDepartTime() != null)  f.setDepartTime(dto.getDepartTime());
        if (dto.getArrivalTime() != null) f.setArrivalTime(dto.getArrivalTime());

        // Active default: true unless explicitly set
        f.setActive(dto.isActive() != null ? dto.isActive() : true);

        // Seats remaining default: capacity unless positive value provided
        if (dto.getSeatsRemaining() > 0) {
            f.setSeatsRemaining(dto.getSeatsRemaining());
        } else {
            f.setSeatsRemaining(dto.getCapacity());
        }

        // Delay (nullable in request)
        if (dto.getDelayHours() != null) {
            f.setDelayHours(dto.getDelayHours());
        }

        // Status (nullable in request; set only if provided)
        if (dto.getStatus() != null) {
            f.setFlightStatus(dto.getStatus());  
        }

        Flight saved = flightRepository.save(f);
        return toResponse(saved);
    }

    // --------- READ ----------
    public FlightResponseDTO getFlightById(long id) {
        Flight f = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Flight with ID " + id));
        return toResponse(f);
    }

    public List<FlightResponseDTO> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // --------- UPDATE ----------
    @Transactional
    public FlightResponseDTO updateFlight(long id, FlightRequestDTO dto) {
        Flight f = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Flight with ID " + id));

        // Required-ish fields (overwrite)
        f.setCapacity(dto.getCapacity());
        f.setExpectedDepartTime(dto.getExpectedDepartTime());
        f.setDepartLocation(dto.getDepartLocation());
        f.setArrivalLocation(dto.getArrivalLocation());
        f.setFlightNumber(dto.getFlightNumber());
        f.setFlightTime(dto.getFlightTime());
        f.setRecurring(dto.isRecurring());

        // Optional fields (null-safe)
        if (dto.isActive() != null) f.setActive(dto.isActive());
        if (dto.getDepartTime() != null)  f.setDepartTime(dto.getDepartTime());
        if (dto.getArrivalTime() != null) f.setArrivalTime(dto.getArrivalTime());
        if (dto.getSeatsRemaining() > 0)  f.setSeatsRemaining(dto.getSeatsRemaining());
        if (dto.getDelayHours() != null)  f.setDelayHours(dto.getDelayHours());
        if (dto.getStatus() != null)      f.setFlightStatus(dto.getStatus());

        Flight saved = flightRepository.save(f);
        return toResponse(saved);
    }

    // --------- DELETE ----------
    @Transactional
    public void deleteFlight(long id) {
        Optional<Flight> opt = flightRepository.findById(id);
        if (opt.isEmpty()) throw new IllegalArgumentException("No Flight with ID " + id);
        flightRepository.delete(opt.get());
    }

    // --------- MAPPER ----------
    private FlightResponseDTO toResponse(Flight f) {
        int delay = f.getDelayHours();  // no null check for primitive

        return new FlightResponseDTO(
                f.getFlightId(),
                f.getCapacity(),
                f.getSeatsRemaining(),
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
    }

       public List<FlightResponseDTO> searchFlightsBetweenDates(LocalDateTime start, LocalDateTime end) {
        List<Flight> flights = flightRepository.searchFlightsBetweenDates(start, end);

         return flights.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        
    }
}
