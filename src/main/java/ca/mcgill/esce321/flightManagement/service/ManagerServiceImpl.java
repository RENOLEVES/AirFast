package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.model.Booking;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;

import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;


@Service
@Validated
public class ManagerServiceImpl {

    private final PersonRepository personRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    ManagerServiceImpl(PersonRepository personRepository,
                       FlightRepository flightRepository,
                       BookingRepository bookingRepository){
        this.personRepository = personRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    public ManagerResponseDTO createManager(ManagerRequestDTO dto) {
        // Date today = Date.valueOf(LocalDate.now());
        
        Manager managerToCreate = new Manager(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        Manager saved = personRepository.save(managerToCreate);
        return new ManagerResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getFirstName(),
                saved.getLastName()
                );
    }

    public ManagerResponseDTO findManagerById(long id) {
        Optional<Person> p = personRepository.findById(id);
        if (p.isPresent() && p.get() instanceof Manager manager) {

            return new ManagerResponseDTO(
                    manager.getId(),
                    manager.getEmail(),
                    manager.getPassword(),
                    manager.getFirstName(),
                    manager.getLastName(),
                    manager.getFlights().stream().map(Flight::getFlightId).toList(),
                    manager.getBookings().stream().map(Booking::getBookingId).toList()
            );
        } else {
            throw new IllegalArgumentException("There is no Manager with ID " + id + ".");
        }
    }

    public List<ManagerResponseDTO> findAllManagers() {
        List<Person> allPersons = personRepository.findAll();
        List<ManagerResponseDTO> allManagers = new ArrayList<>();

        for (Person p : allPersons) {
            if (p instanceof Manager manager) {

                allManagers.add(new ManagerResponseDTO(
                        manager.getId(),
                        manager.getEmail(),
                        manager.getPassword(),
                        manager.getFirstName(),
                        manager.getLastName(),
                        manager.getFlights().stream().map(Flight::getFlightId).toList(),
                        manager.getBookings().stream().map(Booking::getBookingId).toList()));
            }
        }
        if(allManagers.isEmpty()) {
            throw new IllegalArgumentException("There are no Managers in the database.");
        }
        return allManagers;
    }

    public ManagerResponseDTO updateManager(long id, ManagerRequestDTO dto) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Manager managerToUpdate) {
            managerToUpdate.setEmail(dto.getEmail());
            managerToUpdate.setFirstName(dto.getFirstName());
            managerToUpdate.setLastName(dto.getLastName());
            managerToUpdate.setPassword(dto.getPassword());

            List<Flight> flights = flightRepository.findAllById(dto.getFlightIds());
            List<Booking> bookings = bookingRepository.findAllById(dto.getBookingIds());

            managerToUpdate.setFlights(flights);
            managerToUpdate.setBookings(bookings);

            Manager updated = personRepository.save(managerToUpdate);

            return new ManagerResponseDTO(
                    updated.getId(),
                    updated.getEmail(),
                    updated.getPassword(),
                    updated.getFirstName(),
                    updated.getLastName(),
                    updated.getFlights().stream().map(Flight::getFlightId).toList(),
                    updated.getBookings().stream().map(Booking::getBookingId).toList()
            );
        } else {
            throw new IllegalArgumentException("No Manager found with ID " + id);
        }
    }

    public void deleteManager(long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Manager manager) {
            personRepository.delete(manager);
        } else {
            throw new IllegalArgumentException("No Manager found with ID " + id);
        }
    }
}