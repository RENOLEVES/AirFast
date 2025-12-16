package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.controller.request.OwnerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;
import ca.mcgill.esce321.flightManagement.service.OwnerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OwnerServiceImplTest {

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
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- Manager Tests ----------
    @Test
    void testCreateOwner() {
        OwnerRequestDTO dto = new OwnerRequestDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("pass");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        Owner savedOwner = new Owner(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        savedOwner.setId(1L);

        when(personRepository.save(any(Owner.class))).thenReturn(savedOwner);

        OwnerResponse result = ownerService.createOwner(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("test@example.com", result.getEmail());
        verify(personRepository, times(1)).save(any(Manager.class));
    }

    @Test
    void testFindOwnerById_existing() {
        Owner owner = new Owner("e", "p", "f", "l");
        owner.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(owner));

        OwnerResponse result = ownerService.findOwnerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("e", result.getEmail());
        assertEquals("f", result.getFirstName());
        assertEquals("l", result.getLastName());
    }

    @Test
    void testFindOwnerById_notFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> ownerService.findOwnerById(1L));
    }

    @Test
    void testFindAllOwners() {
        Owner o1 = new Owner("e1", "p1", "f1", "l1");
        o1.setId(1L);
        Owner o2 = new Owner("e2", "p2", "f2", "l2");
        o2.setId(2L);

        when(personRepository.findAll()).thenReturn(List.of(o1, o2));

        List<OwnerResponse> result = ownerService.findOwner();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdateOwner_existing() {
        Owner owner = new Owner("e", "p", "f", "l");
        owner.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(flightRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(bookingRepository.findAllById(any())).thenReturn(Collections.emptyList());
        when(personRepository.save(any(Owner.class))).thenReturn(owner);

        OwnerRequestDTO dto = new OwnerRequestDTO();
        dto.setEmail("new@example.com");
        dto.setPassword("newp");
        dto.setFirstName("NewF");
        dto.setLastName("NewL");

        OwnerResponse result = ownerService.updateOwner(1L, dto);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void testDeleteManager_existing() {
        Owner owner = new Owner("e", "p", "f", "l");
        owner.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(owner));

        assertDoesNotThrow(() -> ownerService.deleteOwner(1L));
        verify(personRepository, times(1)).delete(owner);
    }

    // ---------- Seat Tests ----------
    @Test
    void testViewSeats_2() {
        Seat s1 = new Seat();
        s1.setSeatId(1L);
        Seat s2 = new Seat();
        s2.setSeatId(2L);
        when(seatRepository.findAll()).thenReturn(List.of(s1, s2));

        List<SeatResponse> result = ownerService.viewAllSeats();

        assertEquals(2, result.size());
    }


    // ---------- Flight Tests ----------
    @Test
    void testViewFlights_2() {
        Flight f1 = new Flight();
        f1.setFlightId(1L);
        Flight f2 = new Flight();
        f2.setFlightId(2L);
        when(flightRepository.findAll()).thenReturn(List.of(f1, f2));

        List<FlightResponse> result = ownerService.viewAllFlights();

        assertEquals(2, result.size());
    }

    // ---------- Booking Tests ----------
    @Test
    void testViewBookings_2() {
        Booking b1 = new Booking();
        b1.setBookingId(1L);
        Booking b2 = new Booking();
        b2.setBookingId(2L);
        when(bookingRepository.findAll()).thenReturn(List.of(b1, b2));

        List<BookingResponse> result = ownerService.viewAllBookings();

        assertEquals(2, result.size());
    }


    // ---------- View Methods ----------
    @Test
    void testViewAllPersons() {
        Person p1 = new Manager("e1","p","f","l");
        p1.setId(1L);
        Person p2 = new Pilot("e2","p","f","l");
        p2.setId(2L);

        when(personRepository.findAll()).thenReturn(List.of(p1,p2));

        List<EmployeeResponse> result = ownerService.viewAllEmployees();

        assertEquals(2,result.size());
    }

    @Test
    void testViewAllFlights() {
        Flight f = new Flight();
        f.setFlightId(1L);
        when(flightRepository.findAll()).thenReturn(List.of(f));

        List<FlightResponse> result = ownerService.viewAllFlights();

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

        List<BookingResponse> result = ownerService.viewAllBookings();

        assertEquals(1,result.size());
    }

    @Test
    void testViewAllCustomers() {
        Person p1 = new Customer("e1","p","f","l",1, 0, 0);
        p1.setId(1L);
        Person p2 = new Customer("e2","p2","f2","l2",2, 0, 0);
        p2.setId(2L);
        when(personRepository.findAll()).thenReturn(List.of(p1,p2));

        List<CustomerResponse> result = ownerService.viewAllCustomers();

        assertEquals(2,result.size());
    }


    @Test
    void testViewAllSalary() {
        Employee p1 = new Pilot("e2","p","f","l");
        p1.setId(2L);
        p1.setSalary(200000d);
        when(personRepository.findAll()).thenReturn(List.of(p1));

        double result = ownerService.viewSalary(p1.getId());

        assertEquals(200000d,result);
    }

    @Test
    void testViewTotalRevenue() {
        BookingResponse booking1 = new BookingResponse();
        booking1.setPaymentStatus(PaymentStatus.PAID);
        booking1.setSeatId(1L);

        BookingResponse booking2 = new BookingResponse();
        booking2.setPaymentStatus(PaymentStatus.PAID);
        booking2.setSeatId(2L);

        BookingResponse booking3 = new BookingResponse();
        booking3.setPaymentStatus(PaymentStatus.NOTPAID);
        booking3.setSeatId(3L);

        // Mock viewAllBookings() to return the list
        doReturn(List.of(booking1, booking2, booking3)).when(ownerService).viewAllBookings();

        Seat s1 = new Seat();
        s1.setSeatId(1L);
        s1.setPrice(2000);

        Seat s2 = new Seat();
        s2.setSeatId(2L);
        s2.setPrice(1000);

        Seat s3 = new Seat();
        s3.setSeatId(3L);
        s3.setPrice(1333);

        // Mock seatRepository.findById()
        when(seatRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(seatRepository.findById(2L)).thenReturn(Optional.of(s2));
        when(seatRepository.findById(3L)).thenReturn(Optional.of(s3));
        // Call the method
        double totalRevenue = ownerService.calculateTotalRevenue();

        // Assert
        assertEquals(4333, totalRevenue, 0.001);
    }
}
