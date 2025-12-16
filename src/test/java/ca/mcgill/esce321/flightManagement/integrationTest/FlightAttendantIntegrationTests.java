package ca.mcgill.esce321.flightManagement.integrationTest;

import ca.mcgill.esce321.flightManagement.dto.response.FlightAttendantResponse;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlightAttendantIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private PersonRepository personRepo;

    private Long createdId;

    @BeforeAll
    void cleanDb() {
        personRepo.deleteAll();
    }

    // ---------- CREATE ----------
    @Test
    @Order(1)
    void create_valid_201_returnsDtoWithId() {
        Map<String, Object> body = new HashMap<>();
        body.put("email", "fa.integration@mail.com");
        body.put("firstName", "Ivy");
        body.put("lastName", "Lo");
        body.put("password", "strongPass!");

        ResponseEntity<FlightAttendantResponse> response =
                client.postForEntity("/api/flight-attendants", body, FlightAttendantResponse.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        FlightAttendantResponse dto = response.getBody();
        assertNotNull(dto);
        assertNotNull(dto.getId());
        assertTrue(dto.getId() > 0);
        assertEquals("fa.integration@mail.com", dto.getEmail());
        assertEquals("Ivy", dto.getFirstName());
        assertEquals("Lo", dto.getLastName());

        createdId = dto.getId();
    }

    // ---------- READ ONE ----------
    @Test
    @Order(2)
    void getById_valid_200_returnsDto() {
        String url = "/api/flight-attendants/" + createdId;
        ResponseEntity<FlightAttendantResponse> response =
                client.getForEntity(url, FlightAttendantResponse.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightAttendantResponse dto = response.getBody();
        assertNotNull(dto);
        assertEquals(createdId, dto.getId());
        assertEquals("fa.integration@mail.com", dto.getEmail());
    }

    // ---------- UPDATE ----------
    @Test
    @Order(3)
    void update_valid_200_appliesChanges() {
        String url = "/api/flight-attendants/" + createdId;

        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Ivy-Updated");
        body.put("password", "newStrongPass!");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<FlightAttendantResponse> response =
                client.exchange(url, HttpMethod.PUT, entity, FlightAttendantResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        FlightAttendantResponse dto = response.getBody();
        assertNotNull(dto);
        assertEquals("Ivy-Updated", dto.getFirstName());
    }

    // ---------- DELETE ----------
    @Test
    @Order(4)
    void delete_valid_204_thenGet404() {
        String url = "/api/flight-attendants/" + createdId;

        ResponseEntity<Void> del = client.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, del.getStatusCode());

        ResponseEntity<String> getAfterDelete = client.getForEntity(url, String.class);
        assertEquals(HttpStatus.NOT_FOUND, getAfterDelete.getStatusCode());
    }
}
