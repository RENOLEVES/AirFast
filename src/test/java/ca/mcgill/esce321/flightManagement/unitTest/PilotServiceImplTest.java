package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.controller.request.PilotRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PilotResponse;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;
import ca.mcgill.esce321.flightManagement.service.PilotServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PilotServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private PilotServiceImpl pilotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- TEST CREATE ----------
    @Test
    void testCreatePilot() {
        PilotRequestDTO dto = new PilotRequestDTO();
        dto.setEmail("pilot@example.com");
        dto.setPassword("securepass");
        dto.setFirstName("Amelia");
        dto.setLastName("Earhart");
        dto.setFlightIds(List.of(10L,20L));
        

        // String email, String password, String firstName, String lastName, List<Long> flightIds

        Pilot savedPilot = new Pilot(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        savedPilot.setId(1L);
        Flight f = new Flight();
        List<Flight> flights = new ArrayList<>();
        flights.add(f);
        savedPilot.setFlights(flights);

        when(personRepository.save(any(Pilot.class))).thenReturn(savedPilot);

        PilotResponse result = pilotService.createPilot(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Amelia", result.getFirstName());
        assertEquals("Earhart", result.getLastName());
        assertEquals("pilot@example.com", result.getEmail());

        verify(personRepository, times(1)).save(any(Pilot.class));
    }

    // ---------- TEST FIND BY ID ----------
    @Test
    void testFindPilotById() {
        Flight f = new Flight();
        List<Flight> flights = new ArrayList<>();
        flights.add(f);

        Pilot pilot = new Pilot("pilot@example.com", "pass", "John", "Smith");
        pilot.setId(5L);
        pilot.setFlights(flights);

        when(personRepository.findById(5L)).thenReturn(Optional.of(pilot));

        PilotResponse result = pilotService.getPilotById(5L);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("John", result.getFirstName());

        verify(personRepository).findById(5L);
    }

    @Test
    void testFindPilotByIdNotFound() {
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> pilotService.getPilotById(999L)
        );

        assertEquals("There is no Pilot with ID 999.", ex.getMessage());
        verify(personRepository).findById(999L);
    }

    // ---------- TEST FIND ALL ----------
    @Test
    void testFindAllPilots() {
        Pilot pilot1 = new Pilot("pilot1@example.com", "pass1", "Alice", "Wright");
        Pilot pilot2 = new Pilot("pilot2@example.com", "pass2", "Bob", "Johnson");
        
        Flight f = new Flight();
        List<Flight> flights = new ArrayList<>();
        flights.add(f);
        pilot1.setFlights(flights);
        pilot2.setFlights(flights);

        when(personRepository.findAll()).thenReturn(List.of(pilot1, pilot2));

        List<PilotResponse> result = pilotService.getAllPilots();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        assertEquals("Bob", result.get(1).getFirstName());

        verify(personRepository, times(1)).findAll();
    }

    @Test
    void testFindAllPilotsNoneFound() {
        when(personRepository.findAll()).thenReturn(Collections.emptyList());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> pilotService.getAllPilots()
        );

        assertEquals("There are no Pilots in the database.", ex.getMessage());
        verify(personRepository).findAll();
    }

    // ---------- TEST UPDATE ----------
    @Test
    void testUpdatePilot() {
        Pilot existing = new Pilot("old@example.com", "oldpass", "Old", "Name");
        existing.setId(10L);

        PilotRequestDTO dto = new PilotRequestDTO();
        dto.setEmail("new@example.com");
        dto.setPassword("newpass");
        dto.setFirstName("New");
        dto.setLastName("Name");

        when(personRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(personRepository.save(any(Pilot.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PilotResponse result = pilotService.updatePilot(10L, dto);

        assertNotNull(result);
        assertEquals("New", result.getFirstName());
        verify(personRepository).save(existing);
    }

    @Test
    void testUpdatePilotNotFound() {
        PilotRequestDTO dto = new PilotRequestDTO();
        dto.setEmail("new@example.com");

        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> pilotService.updatePilot(999L, dto)
        );

        assertEquals("No Pilot found with ID 999", ex.getMessage());
        verify(personRepository).findById(999L);
        verify(personRepository, never()).save(any());
    }

    // ---------- TEST DELETE ----------
    @Test
    void testDeletePilot() {
        Pilot pilot = new Pilot("pilot@example.com", "pass", "Jane", "Doe");
        pilot.setId(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(pilot));

        pilotService.deletePilot(1L);

        verify(personRepository).findById(1L);
        verify(personRepository).delete(pilot);
    }

    @Test
    void testDeletePilot_NotFound() {
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> pilotService.deletePilot(999L)
        );

        assertEquals("No Pilot found with ID 999", ex.getMessage());
        verify(personRepository).findById(999L);
        verify(personRepository, never()).delete(any());
    }
}
