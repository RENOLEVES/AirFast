package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl {

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    public List<FlightResponseDTO> viewAllFlights() {
        return flightRepository.findAll().stream()
                .map(f -> new FlightResponseDTO(
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
                )).collect(Collectors.toList());
    }

    public List<SeatResponseDTO> viewAllSeatsByFlightId(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found with ID: " + flightId));

        return flight.getSeats().stream()
                .map(s -> new SeatResponseDTO(
                        s.getSeatId(),
                        flightId,
                        s.getSeatClass(),
                        s.getPrice(),
                        s.getSeatNumber(),
                        s.getSeatStatus()
                ))
                .collect(Collectors.toList());
    }
}
