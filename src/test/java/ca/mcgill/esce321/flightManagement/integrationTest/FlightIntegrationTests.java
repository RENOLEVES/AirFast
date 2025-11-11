package ca.mcgill.esce321.flightManagement.integrationTest;

import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlightIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    private Long flightId;

    @Test
    @Order(1)
    void createFlight_success() {
        FlightRequestDTO dto = new FlightRequestDTO(
                120,
                LocalDateTime.now().plusDays(2),
                "YUL",
                "YYZ",
                "AC101",
                1.5,
                false, null, null
        );
        dto.setActive(true);

        ResponseEntity<FlightResponseDTO> resp =
                client.postForEntity("/api/flights", dto, FlightResponseDTO.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resp.getBody()).isNotNull();
        flightId = resp.getBody().getFlightId();
        assertThat(flightId).isNotNull();
        assertThat(resp.getBody().getDepartLocation()).isEqualTo("YUL");
    }

    @Test
    @Order(2)
    void getFlightById_success() {
        ResponseEntity<FlightResponseDTO> resp =
                client.getForEntity("/api/flights/" + flightId, FlightResponseDTO.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getFlightId()).isEqualTo(flightId);
    }

    @Test
    @Order(3)
    void getAllFlights_success() {
        ResponseEntity<FlightResponseDTO[]> resp =
                client.getForEntity("/api/flights", FlightResponseDTO[].class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotEmpty();
    }

    @Test
    @Order(4)
    void updateFlight_success() {
        FlightRequestDTO update = new FlightRequestDTO(
                150,
                LocalDateTime.now().plusDays(5),
                "YUL",
                "LAX",
                "AC101X",
                5.5,
                true, null, null
        );
        update.setActive(true);
        update.setSeatsRemaining(150);

        HttpEntity<FlightRequestDTO> entity = new HttpEntity<>(update);
        ResponseEntity<FlightResponseDTO> resp =
                client.exchange("/api/flights/" + flightId, HttpMethod.PUT, entity, FlightResponseDTO.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getArrivalLocation()).isEqualTo("LAX");
        assertThat(resp.getBody().getCapacity()).isEqualTo(150);
    }

    @Test
    @Order(5)
    void deleteFlight_success() {
        ResponseEntity<Void> resp =
                client.exchange("/api/flights/" + flightId, HttpMethod.DELETE, null, Void.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // verify it's gone
        ResponseEntity<String> getResp =
                client.getForEntity("/api/flights/" + flightId, String.class);
        assertThat(getResp.getStatusCode()).isIn(HttpStatus.NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
