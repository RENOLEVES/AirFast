package ca.mcgill.ecse321.flightManagement.service;

import ca.mcgill.ecse321.flightManagement.dto.FlightCreateDTO;
import ca.mcgill.ecse321.flightManagement.dto.FlightUpdateDTO;
import ca.mcgill.ecse321.flightManagement.model.Flight;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public interface FlightService {

    Flight create(FlightCreateDTO dto);

    Flight update(Long flightId, FlightUpdateDTO dto);

    void delete(Long flightId); // airline cancel: mark CANCELLED and cascade to bookings

    Flight get(Long flightId);

    List<Flight> search(String from, String to, LocalDate date);

    Flight delay(Long flightId, Duration delay); // set status=DELAYED and shift times
}
