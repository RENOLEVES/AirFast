package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.Dto.request.FlightAttendantRequestDTO;
import ca.mcgill.esce321.flightManagement.model.FlightAttendant;

import java.util.List;

public interface FlightAttendantService {

    FlightAttendant createFlightAttendant(FlightAttendantRequestDTO dto);

    FlightAttendant getFlightAttendantById(Long id);

    List<FlightAttendant> getAllFlightAttendants();

    FlightAttendant updateFlightAttendant(Long id, FlightAttendantRequestDTO dto);

    void deleteFlightAttendant(Long id);
}
