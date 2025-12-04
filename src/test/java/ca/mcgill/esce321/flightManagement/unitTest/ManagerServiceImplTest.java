package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;

import ca.mcgill.esce321.flightManagement.dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PersonResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Booking;
import ca.mcgill.esce321.flightManagement.model.Customer;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.FlightAttendant;
import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.model.Pilot;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.service.ManagerServiceImpl;
import ca.mcgill.esce321.flightManagement.repo.*;
import ca.mcgill.esce321.flightManagement.service.FlightServiceImpl;
import java.util.Collections;




import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;



class ManagerServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;
    
    @InjectMocks
    private FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateManager() {
        ManagerRequestDTO dto = new ManagerRequestDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("pass");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        Manager savedManager = new Manager(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        savedManager.setId(1L);

        when(personRepository.save(any(Manager.class))).thenReturn(savedManager);

        ManagerResponseDTO result = managerService.createManager(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("test@example.com", result.getEmail());
        verify(personRepository, times(1)).save(any(Manager.class));
    }



    
    @Test
    void testUpdateFlight() {
        Flight flight = new Flight();
        flight.setFlightId(1L);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flightRepository.save(flight)).thenReturn(flight);

        FlightRequestDTO dto = new FlightRequestDTO();
        dto.setCapacity(200);
        dto.setDepartLocation("Mtl");
        dto.setArrivalLocation("Tor");
        dto.setFlightNumber("AC101");
        dto.setFlightTime(180);
        dto.setRecurring(true);
        dto.setActive(true);

        boolean result = managerService.updateFlight(1L, dto);

        assertTrue(result);
        verify(flightRepository, times(1)).findById(1L);
        verify(flightRepository, times(1)).save(flight);


    }

    
    @Test
    void updateFlightFlightDoesNotExist() {
        long nonExistentFlightId = 999L;

        FlightRequestDTO dto = new FlightRequestDTO();
        dto.setCapacity(100);
        dto.setDepartLocation("Montreal");
        dto.setArrivalLocation("Toronto");
        // dto.setExpectedDepartTime(2025-12-01T10:00);
        dto.setFlightNumber("AC123");
        dto.setFlightTime(180);
        dto.setRecurring(false);
        dto.setActive(true);

        // Arrange: repository returns empty
        when(flightRepository.findById(nonExistentFlightId)).thenReturn(Optional.empty());

        // Act & Assert: expect RuntimeException
        assertThrows(RuntimeException.class, () -> {
            flightService.updateFlight(nonExistentFlightId, dto);
        });

        // Verify save is never called
        verify(flightRepository, never()).save(any());
    }

    @Test
    void testDeleteFlight() {
        Flight flight = new Flight();
        flight.setFlightId(1L);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        boolean result = managerService.deleteFlight(1L);

        assertTrue(result);
        verify(flightRepository, times(1)).delete(flight);
    }

    @Test
    void testDeleteFlightNotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());
        boolean result = managerService.deleteFlight(1L);
        assertFalse(result);
    }

    @Test
    void testSetSeatPrice() {
        Seat seat = new Seat();
        seat.setSeatId(1L);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        boolean result = managerService.setSeatPrice(1L, 200.0);

        assertTrue(result);
        assertEquals(200.0, seat.getPrice());
        verify(seatRepository, times(1)).save(seat);
    }

    @Test
    void testSetSeatPrice_notFound() {
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = managerService.setSeatPrice(1L, 200.0);

        assertFalse(result);
    }

    @Test
    void testAddFlight() {
        FlightRequestDTO dto = new FlightRequestDTO();
        dto.setCapacity(100);
        dto.setDepartLocation("Montreal");
        dto.setArrivalLocation("Toronto");
        dto.setFlightNumber("1");
        dto.setFlightTime(120);
        dto.setRecurring(false);

        boolean result = managerService.addFlight(dto);

        assertTrue(result);
        verify(flightRepository, times(1)).save(any(Flight.class));
    }


   @Test
    void testFindManagerById_Success() {
        Manager manager = new Manager("manager@example.com", "pass", "Alice", "Smith");
        manager.setId(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(manager));

        ManagerResponseDTO result = managerService.findManagerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getFirstName());
    }

    @Test
    void testFindManagerById_NotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> managerService.findManagerById(1L));
    }

      @Test
    void testFindAllManagersReturnsManagers() {
        // Arrange: repository returns a mix of Managers and non-Managers
        List<Person> allPersons = new ArrayList<>();
        Manager manager1 = new Manager();
        Manager manager2 = new Manager();
        allPersons.add(manager1);
        allPersons.add(manager2);
        // Could also add some Person instances that are not Manager
        when(personRepository.findAll()).thenReturn(allPersons);

        // Act
        List<ManagerResponseDTO> result = managerService.findAllManagers();

        // Assert
        assertEquals(result.size(), 2);
    
    }

    @Test
    void testFindAllManagersThrowsIfNoManagers() {
        // Arrange: repository returns only non-manager Persons
        List<Person> allPersons = new ArrayList<>();
        // Manager p1 = new Person("john@test.com", "pw", "John", "Doe");
        // allPersons.add(p1);
        when(personRepository.findAll()).thenReturn(allPersons);

        // Act & Assert
        assertThatThrownBy(() -> managerService.findAllManagers())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There are no Managers in the database.");
    }


    @Test
    void testUpdateManager_Success() {
        Manager existing = new Manager("old@example.com", "oldpass", "Old", "Name");
        existing.setId(1L);

        ManagerRequestDTO dto = new ManagerRequestDTO();
        dto.setEmail("new@example.com");
        dto.setPassword("newpass");
        dto.setFirstName("New");
        dto.setLastName("Name");

        when(personRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(personRepository.save(existing)).thenReturn(existing);

        ManagerResponseDTO result = managerService.updateManager(1L, dto);

        assertEquals("New", result.getFirstName());
        assertEquals("new@example.com", result.getEmail());
        verify(personRepository, times(1)).save(existing);
    }


    @Test
    void testUpdateManager() {
        Manager manager = new Manager("e", "p", "f", "l");
        manager.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(manager));
        when(flightRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(bookingRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(personRepository.save(any(Manager.class))).thenReturn(manager);

        ManagerRequestDTO dto = new ManagerRequestDTO();
        dto.setEmail("new@example.com");
        dto.setPassword("newp");
        dto.setFirstName("NewF");
        dto.setLastName("NewL");
        dto.setFlightIds(Collections.emptyList());
        dto.setBookingIds(Collections.emptyList());

        ManagerResponseDTO result = managerService.updateManager(1L, dto);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void updateManagerWhenManagerDoesNotExist() {
        long nonExistentId = 999L;
        ManagerRequestDTO dto = new ManagerRequestDTO();
        dto.setEmail("test@mcgill.ca");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPassword("password123");

        // Arrange: repository returns empty
        when(personRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert: expect IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            managerService.updateManager(nonExistentId, dto);
        });

        // Verify save is never called
        verify(personRepository, never()).save(any());
    }


    @Test
    void testDeleteManager_Success() {
        Manager manager = new Manager("delete@example.com", "pass", "Del", "Me");
        manager.setId(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(manager));

        managerService.deleteManager(1L);

        verify(personRepository, times(1)).delete(manager);
    }

    @Test
    void testDeleteManager_NotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> managerService.deleteManager(1L));
    }


    @Test
    void testDeleteBooking_Success() {
        Booking booking = new Booking();
        booking.setBookingId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        boolean result = managerService.deleteBooking(1L);

        assertTrue(result);
        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void testDeleteBooking_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = managerService.deleteBooking(1L);

        assertFalse(result);
        verify(bookingRepository, never()).delete(any());
    }

    @Test
    void testViewAllPersons() {
      
        Manager p1 = new Manager("@example.com", "123", "v", "h");

        List<Person> persons = List.of(p1);
        when(personRepository.findAll()).thenReturn(persons);

        List<PersonResponseDTO> dtoList = managerService.viewAllPersons();

        assertEquals(1, dtoList.size());
        assertEquals("v", dtoList.get(0).getFirstName());
    }

    @Test
    void testViewAllFlights() {
        Flight f1 = new Flight();
        f1.setFlightId(1L);
        f1.setCapacity(100);
        f1.setSeatsRemaining(50);
        f1.setDepartTime(LocalDateTime.now());
        f1.setArrivalTime(LocalDateTime.now().plusHours(2));

        when(flightRepository.findAll()).thenReturn(List.of(f1));

        List<FlightResponseDTO> flights = managerService.viewAllFlights();

        assertEquals(1, flights.size());
        assertEquals(1L, flights.get(0).getFlightId());
        assertEquals(50, flights.get(0).getSeatsRemaining());
    }

    @Test
    void testViewAllBookings() {
        Customer customer = new Customer();
        customer.setId(1L);

        Seat seat = new Seat();
        seat.setSeatId(10L);

        Booking booking = new Booking();
        booking.setBookingId(100L);
        booking.setCustomer(customer);
        booking.setSeat(seat);

        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<BookingResponseDTO> dtoList = managerService.viewAllBookings();

        assertEquals(1, dtoList.size());
        assertEquals(100L, dtoList.get(0).getBookingId());
        assertEquals(1L, dtoList.get(0).getCustomerId());
        assertEquals(10L, dtoList.get(0).getSeatId());
    }

    @Test
    void testMakeFlightRecurring_Success() {
        Flight flight = new Flight();
        flight.setFlightId(1L);
        flight.setRecurring(false);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        boolean result = managerService.makeFlightRecurring(1L);

        assertTrue(result);
        assertTrue(flight.isRecurring());
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void testMakeFlightRecurring_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = managerService.makeFlightRecurring(1L);

        assertFalse(result);
        verify(flightRepository, never()).save(any());
    }

    @Test
    void testAssignFlight() {
        Flight flight = new Flight();
        flight.setFlightId(1L);

        Pilot pilot = new Pilot("p@test.com", "pass", "John", "Pilot");
        pilot.setE_id(101L);

        FlightAttendant attendant = new FlightAttendant("f@test.com", "pass", "Jane", "Attendant");
        attendant.setE_id(102L);

        Manager manager = new Manager("m@test.com", "pass", "Mary", "Manager");
        manager.setE_id(103L);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(personRepository.findById(101L)).thenReturn(Optional.of(pilot));
        when(personRepository.findById(102L)).thenReturn(Optional.of(attendant));
        when(personRepository.findById(103L)).thenReturn(Optional.of(manager));

        FlightResponseDTO result = managerService.assignFlight(1L, List.of(101L, 102L, 103L));
        boolean resultt = result.getFlightattendants().size() + result.getPilots().size() == 3;
        assertTrue(resultt);
        assertEquals(manager, flight.getManager());
        assertEquals(1, flight.getPilots().size());
        assertEquals(1, flight.getAttendants().size());
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void testCreateEmployee_Pilot() {
        boolean result = managerService.createEmployee("p@test.com", "pass", "John", "Pilot", "Pilot");

        assertTrue(result);
        verify(personRepository, times(1)).save(any(Pilot.class));
    }




    @Test
    void testCreateEmployee_FlightAttendant() {
        boolean result = managerService.createEmployee("f@test.com", "pass", "Jane", "Attendant", "FlightAttendant");

        assertTrue(result);
        verify(personRepository, times(1)).save(any(FlightAttendant.class));
    }



    @Test
    void testViewFlightStats_Found() {
        Flight flight = new Flight();
        flight.setFlightId(1L);
        flight.setCapacity(100);
        flight.setSeatsRemaining(50);
        flight.setDepartTime(LocalDateTime.now());
        flight.setArrivalTime(LocalDateTime.now().plusHours(2));

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        FlightResponseDTO dto = managerService.viewFlightStats(1L);


        assertNotNull(dto);
        assertEquals(1L, dto.getFlightId());
        assertEquals(50, dto.getSeatsRemaining());
    }


    @Test
    void testViewFlightStats_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        FlightResponseDTO dto = managerService.viewFlightStats(1L);

        assertNull(dto);
    }





}



