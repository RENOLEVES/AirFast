package ca.mcgill.esce321.flightManagement.integrationTest;

import ca.mcgill.esce321.flightManagement.controller.request.OwnerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.OwnerResponse;
import ca.mcgill.esce321.flightManagement.model.Owner;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // rollback between tests
public class OwnerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    private OwnerRequestDTO request;

    @BeforeEach
    void setup() {
        request = new OwnerRequestDTO();
        request.setEmail("owner@test.com");
        request.setPassword("pass123");
        request.setFirstName("John");
        request.setLastName("Doe");
    }

    @Test
    @DisplayName("POST /api/owners/ - should create owner and return 201")
    void testCreateOwner() throws Exception {
        // Arrange: create request JSON
        OwnerRequestDTO request = new OwnerRequestDTO();
        request.setEmail("owner@test.com");
        request.setPassword("pass123");
        request.setFirstName("John");
        request.setLastName("Doe");

        String body = objectMapper.writeValueAsString(request);

        // Act: perform POST
        String jsonResponse = mockMvc.perform(post("/api/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("owner@test.com"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Parse response back to DTO
        OwnerResponse createdDto = objectMapper.readValue(jsonResponse, OwnerResponse.class);

        // Assert: check repository for saved entity
        Optional<Owner> fromDb = personRepository.findById(createdDto.getId())
                .map(p -> (Owner) p);

        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getEmail()).isEqualTo("owner@test.com");
    }


    @Test
    @DisplayName("GET /api/owners/{id} - should retrieve created owner")
    void testGetOwnerById() throws Exception {
        // Arrange
        Owner saved = personRepository.save(new Owner("o@a.com", "123", "Jane", "Smith"));

        mockMvc.perform(get("/api/owners/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("o@a.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    @DisplayName("PUT /api/owners/{id} - should update owner")
    void testUpdateOwner() throws Exception {
        Owner saved = personRepository.save(new Owner("old@x.com", "abc", "Old", "Name"));

        OwnerRequestDTO update = new OwnerRequestDTO();
        update.setEmail("new@x.com");
        update.setPassword("newpass");
        update.setFirstName("New");
        update.setLastName("Name");

        mockMvc.perform(put("/api/owners/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@x.com"))
                .andExpect(jsonPath("$.firstName").value("New"));
    }

    @Test
    @DisplayName("DELETE /api/owners/{id} - should delete owner")
    void testDeleteOwner() throws Exception {
        // Create and save an owner
        Owner saved = personRepository.save(new Owner("del@a.com", "111", "Del", "User"));

        // Perform DELETE request and expect 204 No Content
        mockMvc.perform(delete("/api/owners/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        // Verify the owner has been deleted
        Optional<Owner> deleted = personRepository.findById(saved.getId())
                .map(p -> (Owner) p);
        assertThat(deleted).isEmpty();
    }


    @Test
    @DisplayName("GET /api/owners - should return list of owners")
    void testGetAllOwners() throws Exception {
        personRepository.save(new Owner("list1@test.com", "123", "List1", "User"));
        personRepository.save(new Owner("list2@test.com", "123", "List2", "User"));

        mockMvc.perform(get("/api/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").exists());
    }
}