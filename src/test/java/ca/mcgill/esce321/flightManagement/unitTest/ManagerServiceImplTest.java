package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.Dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.*;
import ca.mcgill.esce321.flightManagement.service.ManagerServiceImpl;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManagerServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- Manager Tests ----------
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
        verify(personRepository, times(1)).save(any(Manager.class));
    }

    @Test
    void testFindManagerById_existing() {
        Manager manager = new Manager("e", "p", "f", "l");
        manager.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(manager));

        ManagerResponseDTO result = managerService.findManagerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindManagerById_notFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> managerService.findManagerById(1L));
    }

    @Test
    void testFindAllManagers() {
        Manager manager1 = new Manager("e1", "p1", "f1", "l1");
        manager1.setId(1L);
        Manager manager2 = new Manager("e2", "p2", "f2", "l2");
        manager2.setId(2L);

        when(personRepository.findAll()).thenReturn(List.of(manager1, manager2));

        List<ManagerResponseDTO> result = managerService.findAllManagers();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdateManager_existing() {
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
    void testDeleteManager_existing() {
        Manager manager = new Manager("e", "p", "f", "l");
        manager.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(manager));

        assertDoesNotThrow(() -> managerService.deleteManager(1L));
        verify(personRepository, times(1)).delete(manager);
    }

    // ---------- Seat Tests ----------
    @Test
    void testSetSeatPrice_existing() {
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

    // ---------- Flight Tests ----------
    @Test
    void testAddFlight() {
        FlightRequestDTO dto = new FlightRequestDTO();
        dto.setCapacity(100);
        dto.setDepartLocation("Montreal");
        dto.setArrivalLocation("Toronto");
        dto.setFlightNumber("100");
        dto.setFlightTime(120);
        dto.setRecurring(false);

        boolean result = managerService.addFlight(dto);

        assertTrue(result);
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void testUpdateFlight_existing() {
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
        dto.setExpectedDepartTime(new Date());

        boolean result = managerService.updateFlight(1L, dto);

        assertTrue(result);
        assertEquals(200, flight.getCapacity());
    }

    @Test
    void testDeleteFlight_existing() {
        Flight flight = new Flight();
        flight.setFlightId(1L);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        boolean result = managerService.deleteFlight(1L);

        assertTrue(result);
        verify(flightRepository, times(1)).delete(flight);
    }

    @Test
    void testDeleteFlight_notFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());
        boolean result = managerService.deleteFlight(1L);
        assertFalse(result);
    }

    // ---------- Booking Tests ----------
    @Test
    void testDeleteBooking_existing() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        boolean result = managerService.deleteBooking(1L);

        assertTrue(result);
        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void testDeleteBooking_notFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = managerService.deleteBooking(1L);

        assertFalse(result);
    }

    // ---------- View Methods ----------
    @Test
    void testViewAllPersons() {
        Person p1 = new Manager("e1","p","f","l");
        p1.setId(1L);
        Person p2 = new Pilot("e2","p","f","l");
        p2.setId(2L);

        when(personRepository.findAll()).thenReturn(List.of(p1,p2));

        List<PersonResponseDTO> result = managerService.viewAllPersons();

        assertEquals(2,result.size());
    }

    @Test
    void testViewAllFlights() {
        Flight f = new Flight();
        f.setFlightId(1L);
        when(flightRepository.findAll()).thenReturn(List.of(f));

        List<FlightResponseDTO> result = managerService.viewAllFlights();

        assertEquals(1,result.size());
    }

    @Test
    void testViewAllBookings() {
        Booking b = new Booking();
        b.setBookingId(1L);
        Customer c = new Customer();
        c.setId(1L);
        Seat s = new Seat();
        s.setSeatId(1L);
        b.setCustomer(c);
        b.setSeat(s);
        when(bookingRepository.findAll()).thenReturn(List.of(b));

        List<BookingResponseDTO> result = managerService.viewAllBookings();

        assertEquals(1,result.size());
    }

    // ---------- Flight Recurring ----------
    @Test
    void testMakeFlightRecurring_existing() {
        Flight f = new Flight();
        f.setFlightId(1L);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(f));
        when(flightRepository.save(f)).thenReturn(f);

        boolean result = managerService.makeFlightRecurring(1L);

        assertTrue(result);
        assertTrue(f.isRecurring());
    }

    @Test
    void testMakeFlightRecurring_notFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = managerService.makeFlightRecurring(1L);

        assertFalse(result);
    }

    // ---------- Assign Flight ----------
    @Test
    void testAssignFlight() {
        Flight flight = new Flight();
        flight.setFlightId(1L);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        Pilot pilot = new Pilot("p@e","pass","F","L");
        pilot.setId(2L);
        FlightAttendant fa = new FlightAttendant("f@e","pass","F","L");
        fa.setId(3L);
        Manager manager = new Manager("m@e","pass","F","L");
        manager.setId(4L);

        when(personRepository.findById(2L)).thenReturn(Optional.of(pilot));
        when(personRepository.findById(3L)).thenReturn(Optional.of(fa));
        when(personRepository.findById
