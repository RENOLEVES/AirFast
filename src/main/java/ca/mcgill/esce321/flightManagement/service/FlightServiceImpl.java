package ca.mcgill.esce321.flightManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ca.mcgill.esce321.flightManagement.model.FlightAttendant;
import ca.mcgill.esce321.flightManagement.model.FlightStatus;
import ca.mcgill.esce321.flightManagement.model.Pilot;
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

        if (dto.getDepartTime() != null) f.setDepartTime(dto.getDepartTime());
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
        return convertToDTO(saved);
    }

    // --------- READ ----------
    public FlightResponseDTO getFlightById(long id) {
        Flight f = flightRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Flight with ID " + id));
        return convertToDTO(f);
    }

    public List<FlightResponseDTO> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(this::convertWithEmployee)
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
        if (dto.getDepartTime() != null) f.setDepartTime(dto.getDepartTime());
        if (dto.getArrivalTime() != null) f.setArrivalTime(dto.getArrivalTime());
        if (dto.getSeatsRemaining() > 0) f.setSeatsRemaining(dto.getSeatsRemaining());
        if (dto.getDelayHours() != null) f.setDelayHours(dto.getDelayHours());
        if (dto.getStatus() != null) f.setFlightStatus(dto.getStatus());

        Flight saved = flightRepository.save(f);
        return convertToDTO(saved);
    }

    // --------- DELETE ----------
    @Transactional
    public void deleteFlight(long id) {
        Optional<Flight> opt = flightRepository.findById(id);
        if (opt.isEmpty()) throw new IllegalArgumentException("No Flight with ID " + id);
        flightRepository.delete(opt.get());
    }

    // --------- MAPPER ----------

    public List<FlightResponseDTO> searchFlights(
            LocalDateTime start,
            LocalDateTime end,
            String departureLocation,
            String arrivalLocation
    ) {
        System.out.println("========== SEARCH DEBUG ==========");
        System.out.println("Start DateTime: " + start);
        System.out.println("End DateTime: " + end);
        System.out.println("Departure Location: '" + departureLocation + "'");
        System.out.println("Arrival Location: '" + arrivalLocation + "'");

        // Test 1: Get ALL flights first
        List<Flight> allFlights = flightRepository.findAll();
        System.out.println("Total flights in database: " + allFlights.size());

        if (!allFlights.isEmpty()) {
            Flight first = allFlights.get(0);
            System.out.println("\nFirst flight details:");
            System.out.println("  Depart Time: " + first.getDepartTime());
            System.out.println("  Depart Location: '" + first.getDepartLocation() + "'");
            System.out.println("  Arrival Location: '" + first.getArrivalLocation() + "'");
        }

        // Test 2: Try date-only search
        List<Flight> dateOnlyFlights = flightRepository.findByDepartTimeBetween(start, end);
        System.out.println("\nFlights in date range (no location filter): " + dateOnlyFlights.size());

        // Test 3: Your actual search
        List<Flight> flights = flightRepository.findFlightsByDateRangeAndLocations(
                start, end, departureLocation, arrivalLocation
        );
        System.out.println("\nFlights found with all filters: " + flights.size());
        System.out.println("==================================\n");

        return flights.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Flight entity to FlightResponseDTO
    private FlightResponseDTO convertToDTO(Flight flight) {
        return new FlightResponseDTO(
                flight.getFlightId(),
                flight.getCapacity(),
                flight.getSeatsRemaining(),
                flight.getDelayHours(),
                flight.getDepartTime(),
                flight.getArrivalTime(),
                flight.getExpectedDepartTime(),
                flight.getDepartLocation(),
                flight.getArrivalLocation(),
                flight.getFlightNumber(),
                flight.getFlightTime(),
                flight.isRecurring(),
                flight.isActive(),
                flight.getFlightStatus()
        );
    }

    private FlightResponseDTO convertWithEmployee(Flight flight) {
        return new FlightResponseDTO(
                flight.getFlightId(),
                flight.getCapacity(),
                flight.getSeatsRemaining(),
                flight.getDelayHours(),
                flight.getDepartTime(),
                flight.getArrivalTime(),
                flight.getExpectedDepartTime(),
                flight.getDepartLocation(),
                flight.getArrivalLocation(),
                flight.getFlightNumber(),
                flight.getFlightTime(),
                flight.isRecurring(),
                flight.isActive(),
                flight.getFlightStatus(),
                flight.getPilots().stream().map(Pilot::getId).toList(),
                flight.getAttendants().stream().map(FlightAttendant::getId).toList()
        );
    }
}
