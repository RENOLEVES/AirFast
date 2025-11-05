package ca.mcgill.esce321.flightManagement.service;

import java.time.LocalDateTime;
// import ca.mcgill.ecse321.flightManagement.repo.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import ca.mcgill.esce321.flightManagement.model.Booking;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;

import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ca.mcgill.esce321.flightManagement.Dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.EmployeeRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.BookingRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.FlightAttendantRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.PilotRequestDTO;



import ca.mcgill.esce321.flightManagement.Dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.PersonResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Booking;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.FlightAttendant;
import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.PaymentStatus;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.model.Pilot;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import jakarta.transaction.Transactional;

import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.Dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.ManagerResponseDTO;

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
    private final SeatRepository seatRepository;

    
    ManagerServiceImpl(PersonRepository personRepository,
                       FlightRepository flightRepository,
                       BookingRepository bookingRepository, SeatRepository seatRepository){
        this.personRepository = personRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
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

    // functionalities

    

   @Transactional
    public boolean setSeatPrice(long id, double newPrice) {
        Optional<Seat> seatToUpdate = seatRepository.findById(id);// need to get SeatID?? how??s
        if (seatToUpdate.isPresent()) {
            Seat s = seatToUpdate.get();
            s.setPrice(newPrice);
            seatRepository.save(s);
            return true;
        }
        return false;
    }

   
   // why so many parametrs.. 
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
            return false; // or throw exception if you prefer
        }

        bookingRepository.delete(optionalBooking.get());
        return true;
    }

    @Transactional
    public List<PersonResponseDTO> viewAllPersons() {
        List<Person> persons = personRepository.findAll();

        // String email, String password, String firstName, String lastName

        // Convert each Person entity to PersonResponseDTO
        List<PersonResponseDTO> dtoList = persons.stream()
            .map(p -> new PersonResponseDTO(
                    p.getEmail(),
                    p.getFirstName(),
                    p.getLastName()
            ))
            .collect(Collectors.toList());

        return dtoList;
    }


    @Transactional
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
                
            
                // // Summary stats
                // dto.setTotalPilots(f.getPilots() != null ? f.getPilots().size() : 0);
                // dto.setTotalAttendants(f.getFlightAttendants() != null ? f.getFlightAttendants().size() : 0);
                // dto.setTotalBookings(f.getBookings() != null ? f.getBookings().size() : 0);

                return dto;
            })
            .collect(Collectors.toList());
    }   


   @Transactional
    public List<BookingResponseDTO> viewAllBookings() {
    return bookingRepository.findAll().stream()
        .map(b -> {
            BookingResponseDTO dto = new BookingResponseDTO(b.getBookingId(), b.getCustomer().getId(), b.getSeat().getSeatId(), b.getBookingDate(), b.getPaymentStatus(), b.getBookingStatus());
            return dto;
        })
        .collect(Collectors.toList());
}   


     
    public boolean makeFlightRecurring(long id) {
        // List<Flight> flights = flightRepository.findAll();

        Optional<Flight> optionalFlight = flightRepository.findById(id);

        if (optionalFlight.isEmpty()) {
            return false; // or throw exception if you prefer
        }

        Flight flight = optionalFlight.get();

        flight.setRecurring(true);
        return true;
    }

    // ERRORS here:...
    @Transactional
    public boolean assignFlight(Long flightId, List<Long> employeeIds) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isEmpty()) {
            return false; // flight not found
        }

        Flight flight = optionalFlight.get();
        List<Person> employees = personRepository.findAllById(employeeIds);

        List<Pilot> pilots = new ArrayList<>();
        List<FlightAttendant> attendants = new ArrayList<>();
        Manager manager = null;

            // Assign relationships
        flight.setManager(manager);
        flight.setPilots(pilots);
        flight.setAttendants(attendants);

        flightRepository.save(flight);

        return true;
    }

    
 

    @Transactional
    public boolean createEmployeeId(EmployeeRequestDTO e) {
        if (e == null) {
            return false;
        }

        long id = ThreadLocalRandom.current().nextLong(100000, 999999);
        e.setId(id);

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


    @Transactional
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