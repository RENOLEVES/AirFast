package ca.mcgill.esce321.flightManagement.service;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.esce321.flightManagement.Dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.PilotRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.PilotResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Pilot;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.model.Manager;
import jakarta.transaction.Transactional;


import ca.mcgill.esce321.flightManagement.model.Booking;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;

import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ca.mcgill.esce321.flightManagement.repo.PersonRepository;


import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Validated
public class PilotService {
    //CRUD
    private final PersonRepository personRepository;
    private final FlightRepository flightRepository;


     PilotService(PersonRepository personRepository, FlightRepository flightRepository){
        this.personRepository = personRepository;
        this.flightRepository = flightRepository;
    }


      public PilotResponseDTO createPilot(PilotRequestDTO dto) {

        
        // Date today = Date.valueOf(LocalDate.now());
        
        Pilot pilotToCreate = new Pilot(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        Pilot saved = personRepository.save(pilotToCreate);
        List<Long> newFlightIds = new ArrayList<Long>();

        for (Flight e : saved.getFlights()) {
            newFlightIds.add(e.getFlightId());
            
        }
        return new PilotResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getFirstName(),
                saved.getLastName(),
                newFlightIds
                );
    }



}
