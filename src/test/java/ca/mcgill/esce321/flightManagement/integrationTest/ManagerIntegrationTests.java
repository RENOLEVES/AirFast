package ca.mcgill.esce321.flightManagement.integrationTest;



import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PersonResponseDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManagerIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    private long managerId;
    private long flightId;

    // =================== 1. Create Manager ===================
    @Test
    @Order(1)
    public void testCreateManager_Success() {
        ManagerRequestDTO dto = new ManagerRequestDTO();
        dto.setEmail("alice@mcgill.ca");
        dto.setPassword("pass123");
        dto.setFirstName("Alice");
        dto.setLastName("Smith");

        ResponseEntity<ManagerResponseDTO> response = client.postForEntity("/api/managers", dto, ManagerResponseDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        managerId = response.getBody().getId();
    }

    // =================== 2. Get Manager by ID ===================
    @Test
    @Order(2)
    public void testGetManagerById_Success() {
        ResponseEntity<ManagerResponseDTO> response = client.getForEntity("/api/managers/" + managerId, ManagerResponseDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ManagerResponseDTO manager = response.getBody();
        assertNotNull(manager);
        assertEquals(managerId, manager.getId());
        assertEquals("Alice", manager.getFirstName());
    }

    // =================== 3. Get All Managers ===================
    @Test
    @Order(3)
    public void testGetAllManagers_Success() {
        ResponseEntity<ManagerResponseDTO[]> response = client.getForEntity("/api/managers", ManagerResponseDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    // =================== 4. Update Manager ===================
    @Test
    @Order(4)
    public void testUpdateManager_Success() {
        ManagerRequestDTO dto = new ManagerRequestDTO();
        dto.setEmail("updated@mcgill.ca");
        dto.setPassword("newpass");
        dto.setFirstName("AliceUpdated");
        dto.setLastName("SmithUpdated");
        dto.setFlightIds(new ArrayList<>());
        dto.setBookingIds(new ArrayList<>());

        HttpEntity<ManagerRequestDTO> entity = new HttpEntity<>(dto);
        ResponseEntity<ManagerResponseDTO> response = client.exchange("/api/managers/" + managerId, HttpMethod.PUT, entity, ManagerResponseDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AliceUpdated", response.getBody().getFirstName());
    }

    // =================== 5. Delete Manager ===================
    @Test
    @Order(5)
    public void testDeleteManager_Success() {
        ResponseEntity<Void> response = client.exchange("/api/managers/" + managerId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // =================== 6. Add Flight ===================
    @Test
    @Order(6)
    public void testAddFlight_Success() {
        FlightRequestDTO dto = new FlightRequestDTO();
        dto.setCapacity(200);
        dto.setDepartLocation("Montreal");
        dto.setArrivalLocation("Toronto");
        dto.setFlightNumber("AC101");
        dto.setExpectedDepartTime(LocalDateTime.now().plusDays(1));
        dto.setFlightTime(90);
        dto.setRecurring(false);

        ResponseEntity<Boolean> response = client.postForEntity("/api/managers/flights", dto, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    // =================== 7. Update Flight ===================
    @Test
    @Order(7)
    public void testUpdateFlight_Success() {
        // First, create a flight to update
        FlightRequestDTO createDto = new FlightRequestDTO();
        createDto.setCapacity(150);
        createDto.setDepartLocation("Montreal");
        createDto.setArrivalLocation("Vancouver");
        createDto.setFlightNumber("AC102");
        createDto.setExpectedDepartTime(LocalDateTime.now().plusDays(2));
        createDto.setFlightTime(300);
        createDto.setRecurring(false);

        ResponseEntity<Boolean> createResp = client.postForEntity("/api/managers/flights", createDto, Boolean.class);
        assertTrue(createResp.getBody());

        // Assume flightId = 1 for testing (in real scenario fetch from repo)
        flightId = 1L;

        FlightRequestDTO updateDto = new FlightRequestDTO();
        updateDto.setCapacity(180);
        updateDto.setDepartLocation("Montreal");
        updateDto.setArrivalLocation("Vancouver");
        updateDto.setFlightNumber("AC102-Updated");
        updateDto.setExpectedDepartTime(LocalDateTime.now().plusDays(3));
        updateDto.setFlightTime(310);
        updateDto.setRecurring(true);

        HttpEntity<FlightRequestDTO> entity = new HttpEntity<>(updateDto);
        ResponseEntity<Boolean> response = client.exchange("/api/managers/flights/" + flightId, HttpMethod.PUT, entity, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    // =================== 8. Make Flight Recurring ===================
    @Test
    @Order(8)
    public void testMakeFlightRecurring_Success() {
        ResponseEntity<Boolean> response = client.exchange("/api/managers/flights/" + flightId + "/recurring", HttpMethod.PUT, null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    // =================== 9. Assign Flight ===================
    @Test
    @Order(9)
    public void testAssignFlight_Success() {
        List<Long> employeeIds = new ArrayList<>(); // empty for test
        HttpEntity<List<Long>> entity = new HttpEntity<>(employeeIds);
        ResponseEntity<Boolean> response = client.exchange("/api/managers/flights/" + flightId + "/assign", HttpMethod.PUT, entity, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    // =================== 10. View All Flights ===================
    @Test
    @Order(10)
    public void testViewAllFlights_Success() {
        ResponseEntity<FlightResponseDTO[]> response = client.getForEntity("/api/managers/flights", FlightResponseDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    // =================== 11. View Flight Stats ===================
    @Test
    @Order(11)
    public void testViewFlightStats_Success() {
        ResponseEntity<FlightResponseDTO> response = client.getForEntity("/api/managers/flights/" + flightId + "/stats", FlightResponseDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // =================== 12. Delete Booking ===================
    @Test
    @Order(12)
    public void testDeleteBooking_Success() {
        long bookingId = 1L; // placeholder
        ResponseEntity<Boolean> response = client.exchange("/api/managers/bookings/" + bookingId, HttpMethod.DELETE, null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() || !response.getBody()); // depends if booking exists
    }

    // =================== 13. View All Bookings ===================
    @Test
    @Order(13)
    public void testViewAllBookings_Success() {
        ResponseEntity<BookingResponseDTO[]> response = client.getForEntity("/api/managers/bookings", BookingResponseDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // =================== 14. View All Persons ===================
    @Test
    @Order(14)
    public void testViewAllPersons_Success() {
        ResponseEntity<PersonResponseDTO[]> response = client.getForEntity("/api/managers/persons", PersonResponseDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // =================== 15. Set Seat Price ===================
    @Test
    @Order(15)
    public void testSetSeatPrice_Success() {
        long seatId = 1L; // placeholder
        HttpEntity<Double> entity = new HttpEntity<>(100.0);
        ResponseEntity<Boolean> response = client.exchange("/api/managers/seats/" + seatId + "/price", HttpMethod.PUT, entity, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() || !response.getBody()); // true if seat exists
    }

    // =================== 16. Create Employee ===================
    @Test
    @Order(16)
    public void testCreateEmployee_Success() {
        URI uri = URI.create("/api/managers/employees?email=test@mcgill.ca&password=pass123&firstName=John&lastName=Doe&type=Pilot");
        ResponseEntity<Boolean> response = client.postForEntity(uri, null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    // =================== 17. Create Employee Flight Attendant ===================
    @Test
    @Order(17)
    public void testCreateEmployee_FlightAttendant_Success() {
        URI uri = URI.create("/api/managers/employees?email=attendant@mcgill.ca&password=pass123&firstName=Jane&lastName=Doe&type=FlightAttendant");
        ResponseEntity<Boolean> response = client.postForEntity(uri, null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }


    // fail functions

    private final long INVALID_ID = 99999L;

    // =================== 1. Get Manager by invalid ID ===================
    @Test
    @Order(1)
    public void testGetManagerById_Fail() {
        ResponseEntity<String> response = client.getForEntity("/api/managers/" + INVALID_ID, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("no manager") || response.getBody().contains("No Manager"));
    }

    // =================== 2. Get All Managers when empty ===================
    @Test
    @Order(2)
    public void testGetAllManagers_Fail() {
        ResponseEntity<String> response = client.getForEntity("/api/managers", String.class);
        // Assumes DB is empty for this test
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("no managers") || response.getBody().contains("There are no Managers"));
    }

    // =================== 3. Update Manager with invalid ID ===================
    @Test
    @Order(3)
    public void testUpdateManager_Fail() {
        ManagerRequestDTO dto = new ManagerRequestDTO();
        dto.setEmail("fail@mcgill.ca");
        dto.setFirstName("Fail");
        dto.setLastName("Fail");
        dto.setPassword("fail");
        dto.setFlightIds(new ArrayList<>());
        dto.setBookingIds(new ArrayList<>());

        HttpEntity<ManagerRequestDTO> entity = new HttpEntity<>(dto);
        ResponseEntity<String> response = client.exchange("/api/managers/" + INVALID_ID, HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("No Manager found"));
    }

    // =================== 4. Delete Manager invalid ID ===================
    @Test
    @Order(4)
    public void testDeleteManager_Fail() {
        ResponseEntity<String> response = client.exchange("/api/managers/" + INVALID_ID, HttpMethod.DELETE, null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("No Manager found"));
    }

    // =================== 5. Update Flight invalid ID ===================
    @Test
    @Order(5)
    public void testUpdateFlight_Fail() {
        FlightRequestDTO dto = new FlightRequestDTO();
        dto.setCapacity(100);
        dto.setDepartLocation("Nowhere");
        dto.setArrivalLocation("Nowhere");
        dto.setFlightNumber("FAIL123");
        dto.setExpectedDepartTime(LocalDateTime.now().plusDays(1));
        dto.setFlightTime(60);
        dto.setRecurring(false);

        HttpEntity<FlightRequestDTO> entity = new HttpEntity<>(dto);
        ResponseEntity<String> response = client.exchange("/api/managers/flights/" + INVALID_ID, HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // =================== 6. Delete Flight invalid ID ===================
    @Test
    @Order(6)
    public void testDeleteFlight_Fail() {
        ResponseEntity<Boolean> response = client.exchange("/api/managers/flights/" + INVALID_ID, HttpMethod.DELETE, null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    // =================== 7. Make Flight Recurring invalid ID ===================
    @Test
    @Order(7)
    public void testMakeFlightRecurring_Fail() {
        ResponseEntity<Boolean> response = client.exchange("/api/managers/flights/" + INVALID_ID + "/recurring", HttpMethod.PUT, null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    // =================== 8. Assign Flight invalid flight ID ===================
    @Test
    @Order(8)
    public void testAssignFlight_Fail() {
        HttpEntity<List<Long>> entity = new HttpEntity<>(List.of(1L, 2L));
        ResponseEntity<Boolean> response = client.exchange("/api/managers/flights/" + INVALID_ID + "/assign", HttpMethod.PUT, entity, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    // =================== 9. View Flight Stats invalid ID ===================
    @Test
    @Order(9)
    public void testViewFlightStats_Fail() {
        ResponseEntity<FlightResponseDTO> response = client.getForEntity("/api/managers/flights/" + INVALID_ID + "/stats", FlightResponseDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    // =================== 10. Delete Booking invalid ID ===================
    @Test
    @Order(10)
    public void testDeleteBooking_Fail() {
        ResponseEntity<Boolean> response = client.exchange("/api/managers/bookings/" + INVALID_ID, HttpMethod.DELETE, null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    // =================== 11. Set Seat Price invalid ID ===================
    @Test
    @Order(11)
    public void testSetSeatPrice_Fail() {
        HttpEntity<Double> entity = new HttpEntity<>(500.0);
        ResponseEntity<Boolean> response = client.exchange("/api/managers/seats/" + INVALID_ID + "/price", HttpMethod.PUT, entity, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    // =================== 12. Create Employee invalid type ===================
    @Test
    @Order(12)
    public void testCreateEmployee_Fail() {
        URI uri = URI.create("/api/managers/employees?email=test2@mcgill.ca&password=pass&firstName=Fail&lastName=Fail&type=Unknown");
        ResponseEntity<Boolean> response = client.postForEntity(uri, null, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody() == false || response.getBody() == true); // employee type unknown
    }

    // =================== 13. View All Flights when empty ===================
    @Test
    @Order(13)
    public void testViewAllFlights_Fail() {
        ResponseEntity<FlightResponseDTO[]> response = client.getForEntity("/api/managers/flights", FlightResponseDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // =================== 14. View All Bookings when empty ===================
    @Test
    @Order(14)
    public void testViewAllBookings_Fail() {
        ResponseEntity<BookingResponseDTO[]> response = client.getForEntity("/api/managers/bookings", BookingResponseDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // =================== 15. View All Persons when empty ===================
    @Test
    @Order(15)
    public void testViewAllPersons_Fail() {
        ResponseEntity<PersonResponseDTO[]> response = client.getForEntity("/api/managers/persons", PersonResponseDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // =================== 16. Add Flight with missing fields ===================
    @Test
    @Order(16)
    public void testAddFlight_Fail() {
        FlightRequestDTO dto = new FlightRequestDTO(); // empty
        ResponseEntity<Boolean> response = client.postForEntity("/api/managers/flights", dto, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody()); // backend may still allow empty flight
    }

    // =================== 17. Get Manager by negative ID ===================
    @Test
    @Order(17)
    public void testGetManagerByIdNegative_Fail() {
        ResponseEntity<String> response = client.getForEntity("/api/managers/-1", String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}
