package ca.mcgill.esce321.flightManagement.service;

// import ca.mcgill.ecse321.flightManagement.repo.PersonRepository;
import jakarta.transaction.Transactional;
// import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.Dto.response.*;
import ca.mcgill.esce321.flightManagement.Dto.request.*;

// import ca.mcgill.ecse321.eventregistration.dto.PersonCreationDto;
// import ca.mcgill.ecse321.flightManagement.exception.FlightManagementException;
import ca.mcgill.esce321.flightManagement.model.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.ArrayList;



@Service
@Validated
public class ManagerServiceImpl {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BookingRepository bookingRepository;



    // public Manager createManager(Manager manager) {
    //     return (Manager) personRepository.save(manager);
    // }

    @Transactional
    public ManagerResponseDTO createManager(ManagerRequestDTO dto) {
        // Date today = Date.valueOf(LocalDate.now());
        
        Manager managerToCreate = new Manager(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        Manager saved = personRepository.save(managerToCreate);

        List<Long> fIds = new ArrayList<>();

        for (Flight flight : managerToCreate.getFlights()) {
            fIds.add(flight.getFlightId()); // or whatever attribute you need
        }
       
        return new ManagerResponseDTO(managerToCreate.getEmail(), managerToCreate.getPassword(), managerToCreate.getFirstName(), managerToCreate.getLastName(), fIds);

        // String email, String password, String firstName, String lastName,
        //                       List<Long> flightIds
    }

    public ManagerResponseDTO findManagerById(long id) {
        Optional<Person> p =  personRepository.findById(id);
        List<Long> fIds = new ArrayList<>();

        if(p.isPresent() && p.get() instanceof Manager manager) {


            for (Flight flight : manager.getFlights()) {
                fIds.add(flight.getFlightId()); // or whatever attribute you need
            }
            return new ManagerResponseDTO(manager.getEmail(), manager.getPassword(), manager.getFirstName(), manager.getLastName(), fIds);
        }
        else {
            throw new IllegalArgumentException("There is no Manager with ID " + id + ".");
        }
    }

    public List<ManagerResponseDTO> findAllManagers() {
        List<Person> allPersons = personRepository.findAll();
        List<ManagerResponseDTO> allManagers = new ArrayList<>();
        List<Long> fIds = new ArrayList<>();


        

        for (Person p : allPersons) {
            if (p instanceof Manager manager) {

                // get flightId's each time creating manager
                for (Flight flight : manager.getFlights()) {
                    fIds.add(flight.getFlightId()); // or whatever attribute you need
                }
                allManagers.add(new ManagerResponseDTO(manager.getEmail(), manager.getPassword(), manager.getFirstName(), manager.getLastName(), fIds));

                // clear the fIds before moving into next Manager creation
                fIds.clear();
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
     List<Long> fIds = new ArrayList<>();

        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Manager managerToUpdate) {
            managerToUpdate.setEmail(dto.getEmail());
            managerToUpdate.setFirstName(dto.getFirstName());
            managerToUpdate.setLastName(dto.getLastName());
            managerToUpdate.setPassword(dto.getPassword());

            for (Flight flight : managerToUpdate.getFlights()) {
                fIds.add(flight.getFlightId()); // or whatever attribute you need
            }

            Manager updated = personRepository.save(managerToUpdate);
            return new ManagerResponseDTO(managerToUpdate.getEmail(), managerToUpdate.getPassword(), managerToUpdate.getFirstName(), managerToUpdate.getLastName(), fIds);
        } else {
            throw new IllegalArgumentException("No Manager found with ID " + id);
        }
    }

     // DELETE
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
    public boolean setSeatPrice(Long seatId, double newPrice) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new NoSuchElementException("Seat not found: " + seatId));
        seat.setPrice(newPrice);
        seatRepository.save(seat);
        return true;
    }

  
    public FlightResponseDTO addFlight(FlightRequestDTO dto) {
        // 1️⃣ Create a new Flight entity
        Flight flight = new Flight();
        flight.setDepartLocation(dto.getDepartLocation());
        flight.setArrivalLocation(dto.getArrivalLocation());
        flight.setDepartTime(dto.getDepartTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setExpectedDepartTime(dto.getExpectedDepartTime());
        flight.setCapacity(dto.getCapacity());
        flight.setSeatsRemaining(dto.getSeatsRemaining());
        flight.setDelayHours(dto.getDelayHours());
        // flight.setFlightNumber(dto.getFlightNumber()); // error
        flight.setFlightTime(dto.getFlightTime());
        flight.setSeatsRemaining(dto.getSeatsRemaining());
        flight.setRecurring(dto.isRecurring());
        flight.setActive(dto.isActive());




        // 2️⃣ Save to database
        Flight savedFlight = flightRepository.save(flight);

        // Long flightId, int capacity, int seatsRemaining,
        //                      LocalDateTime departTime, LocalDateTime arrivalTime, LocalDateTime expectedDepartTime,
        //                      String departLocation, String arrivalLocation, String flightNumber, double flightTime,
        //                      boolean isRecurring, boolean isActive

        // 3️⃣ Return DTO
        return new FlightResponseDTO(
                savedFlight.getFlightId(),
                savedFlight.getCapacity(),
                savedFlight.getSeatsRemaining(),
                savedFlight.getDepartTime(),
                savedFlight.getArrivalTime(),
                savedFlight.getExpectedDepartTime(),
                savedFlight.getDepartLocation(),
                savedFlight.getArrivalLocation(),
                savedFlight.getFlightNumber(),
                savedFlight.getFlightTime(),
                savedFlight.isRecurring(),
                savedFlight.isActive()
        );
    }
    
    public FlightResponseDTO updateFlight(Long flightId, FlightRequestDTO dto) {
        // 1️⃣ Find existing flight
        Flight existingFlight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        // 2️⃣ Update fields
        existingFlight.setDepartLocation(dto.getDepartLocation());
        existingFlight.setArrivalLocation(dto.getArrivalLocation());
        existingFlight.setDepartTime(dto.getDepartTime());
        existingFlight.setArrivalTime(dto.getArrivalTime());
        existingFlight.setExpectedDepartTime(dto.getExpectedDepartTime());
        existingFlight.setCapacity(dto.getCapacity());
        existingFlight.setSeatsRemaining(dto.getSeatsRemaining());
        existingFlight.setDelayHours(dto.getDelayHours());
        // flight.setFlightNumber(dto.getFlightNumber()); // error
        existingFlight.setFlightTime(dto.getFlightTime());
        existingFlight.setSeatsRemaining(dto.getSeatsRemaining());
        existingFlight.setRecurring(dto.isRecurring());
        existingFlight.setActive(dto.isActive());

        // 3️⃣ Save updated flight
        Flight updatedFlight = flightRepository.save(existingFlight);

    

            return new FlightResponseDTO(
                updatedFlight.getFlightId(),
                updatedFlight.getCapacity(),
                updatedFlight.getSeatsRemaining(),
                updatedFlight.getDepartTime(),
                updatedFlight.getArrivalTime(),
                updatedFlight.getExpectedDepartTime(),
                updatedFlight.getDepartLocation(),
                updatedFlight.getArrivalLocation(),
                updatedFlight.getFlightNumber(),
                updatedFlight.getFlightTime(),
                updatedFlight.isRecurring(),
                updatedFlight.isActive()
        );

    }



     public void deleteFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + flightId));

        flightRepository.delete(flight);
    }

     public void deleteBooking(Long bookingId) {
        //Check if booking exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // 2️⃣ Delete it
        bookingRepository.delete(booking);
    }

    public List<Person> viewAllPersons() {
        // Simply fetch all records from the database
        return personRepository.findAll();
    }

    public List<Flight> viewAllFlights() {
        // Simply fetch all records from the database
        return flightRepository.findAll();
    }

    public List<Booking> viewAllBookings() {
        return bookingRepository.findAll();
    }

     
    public boolean makeFlightRecurring() {
        // Example: you might mark all flights as recurring
        List<Flight> flights = flightRepository.findAll();

        if (flights.isEmpty()) {
            return false; // no flights to mark
        }

        for (Flight flight : flights) {
            flight.setRecurring(true);
            flightRepository.save(flight);
        }

        return true; // successfully updated
    }

    public boolean assignFlight(Flight flight, List<Employee> employees) {

        // attendant, manager, pilot

        List<FlightAttendant> = new ArrayList<FlightAttendant>();
        List<Pilot> = new ArrayList<Pilot>();

       
        
    for (Employee e : employees) {
        // Assuming Employee has a method to add a flight
        // e.addFlight(flight);
        if (e instanceof Manager) {
            flight.setManager((Manager) e);
        }
    }
   
        

    }






    




}