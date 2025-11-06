package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.Dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;
import ca.mcgill.esce321.flightManagement.service.OwnerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OwnerServiceImplTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private OwnerServiceImpl ownerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- TEST viewAllCustomers ----------
    @Test
    void testViewAllCustomers() {
        Customer c = new Customer();
        c.setEmail("test@mail.com");
        c.setPassword("pass");
        c.setFirstName("John");
        c.setLastName("Doe");
        c.setMembershipNumber(123);
        c.setPoints(200);
        c.setTimeInFlight(15);

        when(personRepository.findAll()).thenReturn(List.of(c));

        List<CustomerResponseDTO> result = ownerService.viewAllCustomers();

        assertEquals(1, result.size());
        assertEquals("test@mail.com", result.get(0).getEmail());
        verify(personRepository, times(1)).findAll();
    }

    // ---------- TEST viewAllEmployees ----------
    @Test
    void testViewAllEmployees_PilotManagerAttendant() {
        Flight flight = new Flight();
        flight.setFlightId(1L);

        Pilot pilot = new Pilot();
        pilot.setEmail("pilot@mail.com");
        pilot.setFlights(List.of(flight));

        Manager manager = new Manager();
        manager.setEmail("manager@mail.com");
        manager.setFlights(List.of(flight));

        FlightAttendant attendant = new FlightAttendant();
        attendant.setEmail("attendant@mail.com");
        attendant.setFlights(List.of(flight));

        when(personRepository.findAll()).thenReturn(List.of(pilot, manager, attendant));

        List<EmployeeResponseDTO> result = ownerService.viewAllEmployees();

        assertEquals(3, result.size());
        assertTrue(result.get(0) instanceof PilotResponseDTO);
        assertTrue(result.get(1) instanceof ManagerResponseDTO);
        assertTrue(result.get(2) instanceof FlightAttendantResponseDTO);
    }

    // ---------- TEST viewAllFlights ----------
    @Test
    void testViewAllFlights() {
        Flight flight = new Flight();
        flight.setFlightId(10L);
        flight.setCapacity(100);
        flight.setSeatsRemaining(50);
        flight.setDepartTime(LocalDateTime.now());
        flight.setArrivalTime(LocalDateTime.now().plusHours(2));
        flight.setDepartLocation("Montreal");
        flight.setArrivalLocation("Toronto");
        flight.setFlightNumber("AC123");
        flight.setFlightTime(120);
        flight.setRecurring(true);
        flight.setActive(true);

        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<FlightResponseDTO> result = ownerService.viewAllFlights();

        assertEquals(1, result.size());
        assertEquals("AC123", result.get(0).getFlightNumber());
        verify(flightRepository, times(1)).findAll();
    }

    // ---------- TEST viewAllBookings ----------
    @Test
    void testViewAllBookings() {
        Customer customer = new Customer();
        customer.setId(1L);

        Seat seat = new Seat();
        seat.setSeatId(2L);

        Booking booking = new Booking();
        booking.setBookingId(100L);
        booking.setCustomer(customer);
        booking.setSeat(seat);
        booking.setBookingDate(LocalDateTime.now());
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<BookingResponseDTO> result = ownerService.viewAllBookings();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCustomerId());
        assertEquals(2L, result.get(0).getSeatId());
        assertEquals(PaymentStatus.PAID, result.get(0).getPaymentStatus());
    }

    // ---------- TEST viewAllSeats ----------
    @Test
    void testViewAllSeats() {
        Flight flight = new Flight();
        flight.setFlightId(1L);

        Seat seat = new Seat();
        seat.setSeatId(10L);
        seat.setFlight(flight);
        seat.setSeatClass(SeatClass.ECONOMY);
        seat.setPrice(300.0);
        seat.setSeatNumber("A1");
        seat.setSeatStatus(SeatStatus.AVAILABLE);

        when(seatRepository.findAll()).thenReturn(List.of(seat));

        List<SeatResponseDTO> result = ownerService.viewAllSeats();

        assertEquals(1, result.size());
        assertEquals(300.0, result.get(0).getPrice());
        assertEquals("A1", result.get(0).getSeatNumber());
        verify(seatRepository, times(1)).findAll();
    }

    // ---------- TEST viewSalary ----------
    @Test
    void testViewSalary() {
        Employee employee = new Pilot();
        employee.setSalary(50000.0);

        double salary = ownerService.viewSalary(employee);
        double salaryNull = ownerService.viewSalary(null);

        assertEquals(50000.0, salary);
        assertEquals(0.0, salaryNull);
    }

    // ---------- TEST calculateTotalRevenue ----------
    @Test
    void testCalculateTotalRevenue() {
        // Prepare booking with PAID status
        Booking booking = new Booking();
        booking.setBookingId(1L);
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        Seat seat = new Seat();
        seat.setSeatId(10L);
        seat.setPrice(150.0);

        booking.setSeat(seat);

        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        when(seatRepository.findById(10L)).thenReturn(Optional.of(seat));

        double totalRevenue = ownerService.calculateTotalRevenue();

        assertEquals(150.0, totalRevenue);
        verify(bookingRepository, times(1)).findAll();
        verify(seatRepository, times(1)).findById(10L);
    }
}
