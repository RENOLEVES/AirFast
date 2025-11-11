package ca.mcgill.esce321.flightManagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PersonResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Booking;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.FlightAttendant;
import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.model.Pilot;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import jakarta.transaction.Transactional;


@Service
@Validated
public class ManagerServiceImpl {

    private final PersonRepository personRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    
    ManagerServiceImpl(PersonRepository personRepository,
                       FlightRepository flightRepository,
                       BookingRepository bookingRepository, SeatRepository seatRepository){
        this.personRepository = personRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public ManagerResponseDTO createManager(ManagerRequestDTO dto) {        
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


    @Transactional
    public ManagerResponseDTO updateManager(long id, ManagerRequestDTO dto) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Manager managerToUpdate) {
            managerToUpdate.setEmail(dto.getEmail());
            managerToUpdate.setFirstName(dto.getFirstName());
            managerToUpdate.setLastName(dto.getLastName());
            managerToUpdate.setPassword(dto.getPassword());


            List<Flight> flights = flightRepository.findAllById(dto.getFlightIds());
            List<Booking> bookings = bookingRepository.findAllById(dto.getBookingIds());


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


    @Transactional
    public void deleteManager(long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Manager manager) {
            personRepository.delete(manager);
        } else {
            throw new IllegalArgumentException("No Manager found with ID " + id);
        }
    }

    // functionalities

    

   @Transactional
    public boolean setSeatPrice(long id, double newPrice) {
        Optional<Seat> seatToUpdate = seatRepository.findById(id);
        if (seatToUpdate.isPresent()) {
            Seat s = seatToUpdate.get();
            s.setPrice(newPrice);
            seatRepository.save(s);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean addFlight(FlightRequestDTO dto) {
        Flight flight = new Flight(
            dto.getCapacity(),
            dto.getExpectedDepartTime(),
            dto.getDepartLocation(),
            dto.getArrivalLocation(),
            dto.getFlightNumber(),
            dto.getFlightTime(),
            dto.isRecurring()
        );

        flightRepository.save(flight);

        return true;
    }
    
    @Transactional
    public boolean updateFlight(long flightId, FlightRequestDTO dto) {
        Flight existingFlight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

       
        existingFlight.setCapacity(dto.getCapacity());
        existingFlight.setExpectedDepartTime(dto.getExpectedDepartTime());
        existingFlight.setDepartLocation(dto.getDepartLocation());
        existingFlight.setArrivalLocation(dto.getArrivalLocation());
        existingFlight.setFlightNumber(dto.getFlightNumber());
        existingFlight.setFlightTime(dto.getFlightTime());
        existingFlight.setRecurring(dto.isRecurring());
        existingFlight.setActive(dto.isActive());

        Flight updatedFlight = flightRepository.save(existingFlight);


        return true;

    }


    @Transactional
    public boolean deleteFlight(long id) {

        Optional<Flight> optionalFlight = flightRepository.findById(id);
        if (optionalFlight.isEmpty()) {
            return false; // or throw exception
        }

        flightRepository.delete(optionalFlight.get());
        return true;
    }

    @Transactional
    public boolean deleteBooking(long id) {

        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isEmpty()) {
            return false; 
        }

        bookingRepository.delete(optionalBooking.get());
        return true;
    }

    public List<PersonResponseDTO> viewAllPersons() {
        List<Person> persons = personRepository.findAll();

        List<PersonResponseDTO> dtoList = persons.stream()
            .map(p -> new PersonResponseDTO(
                    p.getId(),
                    p.getEmail(),
                    p.getPassword(),
                    p.getFirstName(),
                    p.getLastName()
            ))
            .collect(Collectors.toList());
        return dtoList;
    }


    public List<FlightResponseDTO> viewAllFlights() {
        return flightRepository.findAll().stream()
            .map(f -> {
                FlightResponseDTO dto = new FlightResponseDTO(
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
                

                return dto;
            })
            .collect(Collectors.toList());
    }   


    public List<BookingResponseDTO> viewAllBookings() {
    return bookingRepository.findAll().stream()
        .map(b -> {
            BookingResponseDTO dto = new BookingResponseDTO(b.getBookingId(), b.getCustomer().getId(), b.getSeat().getSeatId(), b.getBookingDate(), b.getPaymentStatus(), b.getBookingStatus());
            return dto;
        })
        .collect(Collectors.toList());
}   


     @Transactional
    public boolean makeFlightRecurring(long id) {

        Optional<Flight> optionalFlight = flightRepository.findById(id);

        if (optionalFlight.isEmpty()) {
            return false; 
        }

        Flight flight = optionalFlight.get();

        flight.setRecurring(true);

        flightRepository.save(flight);
        return true;
    }

    @Transactional
    public boolean assignFlight(Long flightId, List<Long> employeeIds) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isEmpty()) {
            return false; // flight not found
        }

        Flight flight = optionalFlight.get();

        List<Pilot> pilots = new ArrayList<>();
        List<FlightAttendant> attendants = new ArrayList<>();
        Manager manager = null;

        for(Long id: employeeIds) {
            Optional<Person> optionalPerson = personRepository.findById(id);
            if (optionalPerson.isEmpty()) continue;

            Person person = optionalPerson.get();

            if (person instanceof Pilot pilot) {
                pilots.add(pilot);
            }

            else if (person instanceof FlightAttendant attendant) {
                attendants.add(attendant);
            }

            else if (person instanceof Manager man) {
                manager = man;
            }
            
        }


        flight.setManager(manager);
        flight.setPilots(pilots);
        flight.setAttendants(attendants);

        flightRepository.save(flight);

        return true;
    }

    // when we createEmployee, how do we specify what kind of employee it is. Pilot, FlightAttendant
    // like this for now
    @Transactional
    public boolean createEmployee(String email, String password, String firstName, String lastName, String type) {

        if(type.equals("FlightAttendant")) {
            FlightAttendant f = new FlightAttendant(email, password, firstName, lastName);
            Long e_id = System.currentTimeMillis();
            f.setE_id(e_id);
            personRepository.save(f);

            
        }
        else if (type.equals("Pilot")) {
            Pilot p = new Pilot(email, password, firstName, lastName);
            Long e_id = System.currentTimeMillis();
            p.setE_id(e_id);
            personRepository.save(p);

        }
      
        return true;
    }   


    @Transactional
    public boolean makeFlightRecurring(Long id) {
        Optional<Flight> optionalFlight = flightRepository.findById(id);
        if (optionalFlight.isEmpty()) {
            return false;
        }

        Flight flight = optionalFlight.get();
        flight.setRecurring(true);
        flightRepository.save(flight);
        return true;
    }


    public FlightResponseDTO viewFlightStats(long flightId) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isEmpty()) {
            return null;
        }

        Flight f = optionalFlight.get();

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