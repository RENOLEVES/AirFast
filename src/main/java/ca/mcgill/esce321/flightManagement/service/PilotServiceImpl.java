package ca.mcgill.esce321.flightManagement.service;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.esce321.flightManagement.controller.request.PilotRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PilotResponse;
import ca.mcgill.esce321.flightManagement.model.Pilot;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.Person;

import ca.mcgill.esce321.flightManagement.repo.FlightRepository;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.Optional;

// public interface PilotService {
//     Pilot createPilot(PilotRequestDTO dto); V
//     Pilot getPilotById(Long id); V
//     List<Pilot> getAllPilots(); V
//     Pilot updatePilot(Long id, PilotRequestDTO dto); V
//     void deletePilot(Long id); V
// }

@Service
@Validated
public class PilotServiceImpl {
    //CRUD
    private final PersonRepository personRepository;
    private final FlightRepository flightRepository;


     PilotServiceImpl(PersonRepository personRepository, FlightRepository flightRepository){
        this.personRepository = personRepository;
        this.flightRepository = flightRepository;
    }


      public PilotResponse createPilot(PilotRequestDTO dto) {

        Pilot pilotToCreate = new Pilot(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        Pilot saved = personRepository.save(pilotToCreate);
     
        return new PilotResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getFlights().stream().map(Flight::getFlightId).toList()
                );
    }

    public PilotResponse getPilotById(long id) {
        Optional<Person> p = personRepository.findById(id);

     
        if (p.isPresent() && p.get() instanceof Pilot pilot) {

            return new PilotResponse(
                    pilot.getId(),
                    pilot.getEmail(),
                    pilot.getFirstName(),
                    pilot.getLastName(),
                    pilot.getFlights().stream().map(Flight::getFlightId).toList()
            );
        } else {
            throw new IllegalArgumentException("There is no Pilot with ID " + id + ".");
        }
    }

     public List<PilotResponse> getAllPilots() {
        List<Person> allPersons = personRepository.findAll();
        List<PilotResponse> allPilots = new ArrayList<>();
        for (Person p : allPersons) {
            if (p instanceof Pilot pilot) {

                allPilots.add(new PilotResponse(
                    pilot.getId(),
                    pilot.getEmail(),
                    pilot.getFirstName(),
                    pilot.getLastName(),
                    pilot.getFlights().stream().map(Flight::getFlightId).toList()));
            }
        }
        if(allPilots.isEmpty()) {
            throw new IllegalArgumentException("There are no Pilots in the database.");
        }
        return allPilots;
    }

    public PilotResponse updatePilot(long id, PilotRequestDTO dto) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Pilot pilotToUpdate) {
            pilotToUpdate.setEmail(dto.getEmail());
            pilotToUpdate.setFirstName(dto.getFirstName());
            pilotToUpdate.setLastName(dto.getLastName());
            pilotToUpdate.setPassword(dto.getPassword());


            List<Flight> flights = flightRepository.findAllById(dto.getFlightIds());

            pilotToUpdate.setFlights(flights);

            personRepository.save(pilotToUpdate);

            return new PilotResponse(
                    pilotToUpdate.getId(),
                    pilotToUpdate.getEmail(),
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
