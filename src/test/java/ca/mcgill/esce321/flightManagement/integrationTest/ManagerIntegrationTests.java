package ca.mcgill.esce321.flightManagement.integrationTest;

import ca.mcgill.esce321.flightManagement.controller.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponse;
import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional // rollback between tests
public class ManagerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    private ManagerRequestDTO request;

    @BeforeEach
    void setup() {
        request = new ManagerRequestDTO();
        request.setEmail("manager@test.com");
        request.setPassword("pass123");
        request.setFirstName("John");
        request.setLastName("Doe");
    }

    @Test
    @DisplayName("POST /api/manager/ - should create manager and return 201")
    void testCreateOwner() throws Exception {
        // Arrange: create request JSON
        ManagerRequestDTO request = new ManagerRequestDTO();
        request.setEmail("manager@test.com");
        request.setPassword("pass123");
        request.setFirstName("John");
        request.setLastName("Doe");

        String body = objectMapper.writeValueAsString(request);

        // Act: perform POST
        String jsonResponse = mockMvc.perform(post("/api/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("manager@test.com"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Parse response back to DTO
        ManagerResponse createdDto = objectMapper.readValue(jsonResponse, ManagerResponse.class);

        // Assert: check repository for saved entity
        Optional<Manager> fromDb = personRepository.findById(createdDto.getId())
                .map(p -> (Manager) p);

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getEmail()).isEqualTo("manager@test.com");
    }


    @Test
    @DisplayName("GET /api/managers/{id} - should retrieve created manager")
    void testGetManagerById() throws Exception {
        Manager manager = new Manager("o@a.com", "123", "Jane", "Smith");

        // Arrange
        Manager saved = personRepository.save(manager);

        mockMvc.perform(get("/api/managers/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("o@a.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    @DisplayName("PUT /api/managers/{id} - should update manager")
    void testUpdateOwner() throws Exception {
        Manager saved = personRepository.save(new Manager("old@x.com", "abc", "Old", "Name"));

        ManagerRequestDTO update = new ManagerRequestDTO();
        update.setEmail("new@x.com");
        update.setPassword("newpass");
        update.setFirstName("New");
        update.setLastName("Name");
        update.setFlightIds(new ArrayList<>());
        update.setBookingIds(new ArrayList<>());  // <-- must not be null
        update.setActive(false);

        mockMvc.perform(put("/api/managers/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@x.com"))
                .andExpect(jsonPath("$.firstName").value("New"));
    }

    @Test
    @DisplayName("DELETE /api/managers/{id} - should delete manager")
    void testDeleteOwner() throws Exception {
        // Create and save an manager
        Manager saved = personRepository.save(new Manager("del@a.com", "111", "Del", "User"));

        // Perform DELETE request and expect 204 No Content
        mockMvc.perform(delete("/api/managers/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        // Verify the owner has been deleted
        Optional<Manager> deleted = personRepository.findById(saved.getId())
                .map(p -> (Manager) p);
        assertThat(deleted).isEmpty();
    }


    @Test
    @DisplayName("GET /api/owners - should return list of owners")
    void testGetAllOwners() throws Exception {
        personRepository.save(new Manager("list1@test.com", "123", "List1", "User"));
        personRepository.save(new Manager("list2@test.com", "123", "List2", "User"));

        mockMvc.perform(get("/api/managers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").exists());
    }
}
