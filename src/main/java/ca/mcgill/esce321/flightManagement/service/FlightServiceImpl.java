package ca.mcgill.esce321.flightManagement.service;

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
        Flight f = new Flight(
                dto.getCapacity(),
                dto.getExpectedDepartTime(),
                dto.getDepartLocation(),
                dto.getArrivalLocation(),
                dto.getFlightNumber(),
                dto.getFlightTime(),
                dto.isRecurring()
        );
        f.setDepartTime(dto.getDepartTime());
        f.setArrivalTime(dto.getArrivalTime());
        f.setActive(dto.isActive());
        // seatsRemaining typically starts at capacity (until bookings reduce it)
        if (dto.getSeatsRemaining() > 0) {
            f.setSeatsRemaining(dto.getSeatsRemaining());
        } else {
            f.setSeatsRemaining(dto.getCapacity());
        }
        f.setDelayInHours(dto.getDelayHours());

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

        // map fields one-by-one to avoid null overwrites
        f.setCapacity(dto.getCapacity());
        f.setExpectedDepartTime(dto.getExpectedDepartTime());
        f.setDepartLocation(dto.getDepartLocation());
        f.setArrivalLocation(dto.getArrivalLocation());
        f.setFlightNumber(dto.getFlightNumber());
        f.setFlightTime(dto.getFlightTime());
        f.setRecurring(dto.isRecurring());
        f.setActive(dto.isActive());

        if (dto.getDepartTime() != null) f.setDepartTime(dto.getDepartTime());
        if (dto.getArrivalTime() != null) f.setArrivalTime(dto.getArrivalTime());
        if (dto.getSeatsRemaining() > 0) f.setSeatsRemaining(dto.getSeatsRemaining());
        f.setDelayInHours(dto.getDelayHours());

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

    // --------- mapper ----------
    private FlightResponseDTO toResponse(Flight f) {
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
}


