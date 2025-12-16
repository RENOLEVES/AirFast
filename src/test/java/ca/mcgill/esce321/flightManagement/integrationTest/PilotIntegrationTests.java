package ca.mcgill.esce321.flightManagement.integrationTest;

import ca.mcgill.esce321.flightManagement.controller.request.PilotRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PilotResponse;
import ca.mcgill.esce321.flightManagement.model.Pilot;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.boot.test.web.client.TestRestTemplate;


import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PilotIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private PersonRepository personRepository;

    private long pilotId;

    @Test
    @Order(1)
    @DisplayName("POST /api/pilots - should create pilot and return 201")
    void testCreatePilot() throws Exception {
        // Arrange
        PilotRequestDTO request = new PilotRequestDTO();
        request.setEmail("pilot@test.com");
        request.setPassword("securepass");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setFlightIds(new ArrayList<>());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PilotRequestDTO> entity = new HttpEntity<>(request, headers);

        // Act
        ResponseEntity<PilotResponse> response = client.postForEntity("/api/pilots", entity, PilotResponse.class);

        // Assert HTTP status
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Assert response body
        PilotResponse created = response.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getEmail()).isEqualTo("pilot@test.com");
        assertThat(created.getFirstName()).isEqualTo("John");
        assertThat(created.getLastName()).isEqualTo("Doe");

        pilotId = created.getId(); // save for future tests

        // Assert DB state
        Optional<Pilot> fromDb = personRepository.findById(pilotId)
                .filter(p -> p instanceof Pilot)
                .map(p -> (Pilot) p);

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getEmail()).isEqualTo("pilot@test.com");
    }

    @Test
    @Order(2)
    @DisplayName("PUT /api/pilots/{id} - should update pilot and return 200")
    void testUpdatePilot() {
        // Arrange: create request for update
        PilotRequestDTO updateRequest = new PilotRequestDTO();
        updateRequest.setEmail("updated@test.com");
        updateRequest.setPassword("newpass");
        updateRequest.setFirstName("JaneUpdated");
        updateRequest.setLastName("DoeUpdated");
        updateRequest.setFlightIds(new ArrayList<>()); // keep empty for simplicity

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PilotRequestDTO> entity = new HttpEntity<>(updateRequest, headers);

        // Act: perform PUT request
        ResponseEntity<PilotResponse> response = client.exchange(
                "/api/pilots/{id}", HttpMethod.PUT, entity, PilotResponse.class, pilotId
        );

        // Assert HTTP status
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Assert response body
        PilotResponse updated = response.getBody();
        assertThat(updated).isNotNull();
        assertThat(updated.getEmail()).isEqualTo("updated@test.com");
        assertThat(updated.getFirstName()).isEqualTo("JaneUpdated");
        assertThat(updated.getLastName()).isEqualTo("DoeUpdated");

        // Assert DB state
        Optional<Pilot> fromDb = personRepository.findById(updated.getId())
                .filter(p -> p instanceof Pilot)
                .map(p -> (Pilot) p);

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getEmail()).isEqualTo("updated@test.com");
        assertThat(fromDb.get().getFirstName()).isEqualTo("JaneUpdated");
        assertThat(fromDb.get().getLastName()).isEqualTo("DoeUpdated");
    }

    @Test
    @Order(3)
    @DisplayName("DELETE /api/pilots/{id} - should delete pilot and return 204")
    void testDeletePilot() {
        // Arrange headers (no body needed for delete)
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Act: perform DELETE request
        ResponseEntity<Void> response = client.exchange(
                "/api/pilots/{id}", HttpMethod.DELETE, entity, Void.class, pilotId
        );

        // Assert HTTP status
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Assert DB state: pilot should no longer exist
        Optional<Pilot> fromDb = personRepository.findById(pilotId)
                .filter(p -> p instanceof Pilot)
                .map(p -> (Pilot) p);

        assertThat(fromDb).isEmpty();
    }
}



