package ca.mcgill.esce321.flightManagement.integrationTest;


import static org.junit.jupiter.api.Assertions.*;

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
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDTO;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManagerIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    private long managerId;

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

    // =================== 2. Update Manager ===================
    @Test
    @Order(2)
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
        assertNotNull(response.getBody());
        assertEquals("AliceUpdated", response.getBody().getFirstName());
    }

    // =================== 3. Delete Manager ===================
    @Test
    @Order(3)
    public void testDeleteManager_Success() {
        ResponseEntity<Void> response = client.exchange("/api/managers/" + managerId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
