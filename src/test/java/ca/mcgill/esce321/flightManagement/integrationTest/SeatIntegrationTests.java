package ca.mcgill.esce321.flightManagement.integrationTest;

import ca.mcgill.esce321.flightManagement.controller.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.SeatResponse;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // rollback between tests
public class SeatIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FlightRepository flightRepository;

    private SeatRequestDTO request;
    Flight f;

    @BeforeEach
    void setup() {
        f = new Flight();
        flightRepository.save(f);

        request = new SeatRequestDTO();
        request.setFlightId(f.getFlightId());
        request.setPrice(100);
        request.setSeatClass(SeatClass.BUSINESS);
        request.setSeatNumber("a1");
        request.setSeatStatus(SeatStatus.AVAILABLE);
    }

    @Test
    @DisplayName("POST /api/seats/ - should create seat and return 201")
    void testCreateOwner() throws Exception {
        // Arrange: create request JSON
        SeatRequestDTO request = new SeatRequestDTO();
        request.setFlightId(f.getFlightId());
        request.setPrice(100);
        request.setSeatClass(SeatClass.BUSINESS);
        request.setSeatNumber("a1");
        request.setSeatStatus(SeatStatus.AVAILABLE);

        String body = objectMapper.writeValueAsString(request);

        // Act: perform POST
        String jsonResponse = mockMvc.perform(post("/api/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value("100.0"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Parse response back to DTO
        SeatResponse createdDto = objectMapper.readValue(jsonResponse, SeatResponse.class);

        // Assert: check repository for saved entity
        Optional<Seat> fromDb = seatRepository.findById(createdDto.getSeatId())
                .map(p -> (Seat) p);

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getSeatNumber()).isEqualTo("a1");
    }


    @Test
    @DisplayName("GET /api/seats/{id} - should retrieve created seat")
    void testGetSeatById() throws Exception {
        // Arrange
        Seat saved = seatRepository.save(new Seat(SeatClass.BUSINESS, 100.0, "a1", SeatStatus.AVAILABLE,f));

        mockMvc.perform(get("/api/seats/{id}", saved.getSeatId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value("100.0"))
                .andExpect(jsonPath("$.seatNumber").value("a1"));
    }

    @Test
    @DisplayName("PUT /api/seats/{id} - should update seat")
    void testUpdateOwner() throws Exception {
        Seat saved = seatRepository.save(new Seat(SeatClass.BUSINESS, 100.0, "a1", SeatStatus.AVAILABLE,f));

        SeatRequestDTO update = new SeatRequestDTO();
        update.setFlightId(f.getFlightId());
        update.setPrice(200);
        update.setSeatClass(SeatClass.ECONOMY);
        update.setSeatNumber("b2");
        update.setSeatStatus(SeatStatus.AVAILABLE);

        mockMvc.perform(put("/api/seats/{id}", saved.getSeatId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value("200.0"))
                .andExpect(jsonPath("$.seatNumber").value("b2"));
    }

    @Test
    @DisplayName("DELETE /api/seats/{id} - should delete seat")
    void testDeleteOwner() throws Exception {
        // Create and save an seat
        Seat saved = seatRepository.save(new Seat(SeatClass.BUSINESS, 200.0, "b2", SeatStatus.AVAILABLE,f));

        // Perform DELETE request and expect 204 No Content
        mockMvc.perform(delete("/api/seats/{id}", saved.getSeatId()))
                .andExpect(status().isNoContent());

        // Verify the seat has been deleted
        Optional<Seat> deleted = seatRepository.findById(saved.getSeatId())
                .map(p -> (Seat) p);
        assertThat(deleted).isEmpty();
    }


    @Test
    @DisplayName("GET /api/seats - should return list of seats")
    void testGetAllSeats() throws Exception {
        seatRepository.save(new Seat(SeatClass.BUSINESS, 200.0, "b2", SeatStatus.AVAILABLE,f));
        seatRepository.save(new Seat(SeatClass.ECONOMY, 233.0, "c3", SeatStatus.AVAILABLE,f));

        mockMvc.perform(get("/api/seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").exists());
    }

}
