package ca.mcgill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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

import ca.mcgill.esce321.flightManagement.dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.SeatResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.SeatStatus;
import ca.mcgill.esce321.flightManagement.model.SeatClass;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SeatIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private FlightRepository flightRepository;

    private Flight testFlight;
    private long createdSeatId;

    @Test
    @Order(1)
    public void testCreateSeat() {
        // Arrange: create a flight
        testFlight = new Flight();
        testFlight.setFlightNumber("TEST123");
        testFlight = flightRepository.save(testFlight);

        SeatRequestDTO request = new SeatRequestDTO();
        request.setFlightId(testFlight.getFlightId());
        request.setSeatClass(SeatClass.ECONOMY);
        request.setPrice(100.0);
        request.setSeatNumber("1");
        request.setSeatStatus(SeatStatus.AVAILABLE);

        // Act
        ResponseEntity<SeatResponseDTO> response = client.postForEntity("/api/seats", request, SeatResponseDTO.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        SeatResponseDTO createdSeat = response.getBody();
        assertNotNull(createdSeat);
        assertEquals(SeatClass.ECONOMY, createdSeat.getSeatClass());
        assertEquals(1, createdSeat.getSeatNumber());
        assertEquals(SeatStatus.AVAILABLE, createdSeat.getSeatStatus());
        assertEquals(testFlight.getFlightId(), createdSeat.getFlightId());
        assertNotNull(createdSeat.getSeatId());

        this.createdSeatId = createdSeat.getSeatId();
    }


  
    @Test
    @Order(2)
    public void testUpdateSeat() {
        // Arrange
        SeatRequestDTO updateRequest = new SeatRequestDTO();
        updateRequest.setFlightId(testFlight.getFlightId());
        updateRequest.setSeatClass(SeatClass.BUSINESS);
        updateRequest.setPrice(200.0);
        updateRequest.setSeatNumber("5");
        updateRequest.setSeatStatus(SeatStatus.TOBEDETERMINED);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SeatRequestDTO> entity = new HttpEntity<>(updateRequest, headers);

        // Act
        ResponseEntity<SeatResponseDTO> response = client.exchange(
                "/api/seats/" + createdSeatId,
                HttpMethod.PUT,
                entity,
                SeatResponseDTO.class
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        SeatResponseDTO updatedSeat = response.getBody();
        assertNotNull(updatedSeat);
        assertEquals(SeatClass.BUSINESS, updatedSeat.getSeatClass());
        assertEquals(200.0, updatedSeat.getPrice());
        assertEquals("5", updatedSeat.getSeatNumber());
        assertEquals(SeatStatus.TOBEDETERMINED, updatedSeat.getSeatStatus());
    }

    @Test
    @Order(3)
    public void testDeleteSeat() {
        // Act
        ResponseEntity<Void> response = client.exchange(
                "/api/seats/" + createdSeatId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify seat no longer exists
        ResponseEntity<String> getResponse = client.getForEntity("/api/seats/" + createdSeatId, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, getResponse.getStatusCode());
    }

}
