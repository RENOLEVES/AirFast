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

        Pilot pilotToCreate = new Pilot(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        Pilot saved = personRepository.save(pilotToCreate);
     
        return new PilotResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getFlights().stream().map(Flight::getFlightId).toList()
                );
    }

    public PilotResponseDTO findPilotById(long id) {
        Optional<Person> p = personRepository.findById(id);

     
        if (p.isPresent() && p.get() instanceof Pilot pilot) {

            return new PilotResponseDTO(
                    pilot.getId(),
                    pilot.getEmail(),
                    pilot.getPassword(),
                    pilot.getFirstName(),
                    pilot.getLastName(),
                    pilot.getFlights().stream().map(Flight::getFlightId).toList()
            );
        } else {
            throw new IllegalArgumentException("There is no Pilot with ID " + id + ".");
        }
    }

     public List<PilotResponseDTO> findAllPilots() {
        List<Person> allPersons = personRepository.findAll();
        List<PilotResponseDTO> allPilots = new ArrayList<>();
        for (Person p : allPersons) {
            if (p instanceof Pilot pilot) {

                allPilots.add(new PilotResponseDTO(
                    pilot.getId(),
                    pilot.getEmail(),
                    pilot.getPassword(),
                    pilot.getFirstName(),
                    pilot.getLastName(),
                    pilot.getFlights().stream().map(Flight::getFlightId).toList()));
            }
        }
        if(allPilots.isEmpty()) {
            throw new IllegalArgumentException("There are no Managers in the database.");
        }
        return allPilots;
    }

    public PilotResponseDTO updatePilot(long id, PilotRequestDTO dto) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Pilot pilotToUpdate) {
            pilotToUpdate.setEmail(dto.getEmail());
            pilotToUpdate.setFirstName(dto.getFirstName());
            pilotToUpdate.setLastName(dto.getLastName());
            pilotToUpdate.setPassword(dto.getPassword());


            List<Flight> flights = flightRepository.findAllById(dto.getFlightIds());

            pilotToUpdate.setFlights(flights);

            personRepository.save(pilotToUpdate);

            return new PilotResponseDTO(
                    pilotToUpdate.getId(),
                    pilotToUpdate.getEmail(),
                    pilotToUpdate.getPassword(),
                    pilotToUpdate.getFirstName(),
                    pilotToUpdate.getLastName(),
                    pilotToUpdate.getFlights().stream().map(Flight::getFlightId).toList());
           
        } else {
            throw new IllegalArgumentException("No Pilot found with ID " + id);
        }
    }

    public void deletePilot(long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Pilot pilot) {
            personRepository.delete(pilot);
        } else {
            throw new IllegalArgumentException("No Pilot found with ID " + id);
        }
    }












}
