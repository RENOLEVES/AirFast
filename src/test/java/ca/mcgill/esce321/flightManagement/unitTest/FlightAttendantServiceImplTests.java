package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.controller.request.FlightAttendantRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightAttendantResponse;
import ca.mcgill.esce321.flightManagement.service.FlightAttendantServiceImpl;
import ca.mcgill.esce321.flightManagement.model.FlightAttendant;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class FlightAttendantServiceImplTests {

    @Mock
    private PersonRepository repo;

    @InjectMocks
    private FlightAttendantServiceImpl service;

    // ---------- CREATE ----------
    @Test
    void createFlightAttendant_valid_returnsDTO_andSaves() {
        // Arrange: mock a DTO so we don't depend on its constructors
        FlightAttendantRequestDTO req = mock(FlightAttendantRequestDTO.class);
        when(req.getEmail()).thenReturn("fa1@mail.com");
        when(req.getFirstName()).thenReturn("Alex");
        when(req.getLastName()).thenReturn("Chen");
        when(req.getPassword()).thenReturn("secret123");

        FlightAttendant saved = new FlightAttendant();
        saved.setId(100L);
        saved.setEmail("fa1@mail.com");
        saved.setFirstName("Alex");
        saved.setLastName("Chen");
        saved.setPassword("secret123");

        when(repo.save(any(Person.class))).thenReturn(saved);

        // Act
        FlightAttendantResponse dto = service.createFlightAttendant(req);

        // Assert
        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("fa1@mail.com", dto.getEmail());
        assertEquals("Alex", dto.getFirstName());
        assertEquals("Chen", dto.getLastName());
        verify(repo, times(1)).save(any(Person.class));
    }

    @Test
    void createFlightAttendant_missingEmail_throws() {
        FlightAttendantRequestDTO req = mock(FlightAttendantRequestDTO.class);
        when(req.getEmail()).thenReturn(null);
        when(req.getFirstName()).thenReturn("A");
        when(req.getLastName()).thenReturn("B");
        when(req.getPassword()).thenReturn("C");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.createFlightAttendant(req));
        assertEquals("email is required", e.getMessage());
        verify(repo, never()).save(any());
    }

    // ---------- READ ONE ----------
    @Test
    void getFlightAttendantById_valid_returnsPerson() {
        Long id = 5L;
        FlightAttendant fa = new FlightAttendant();
        fa.setId(id);
        fa.setEmail("x@y.com");
        when(repo.findById(id)).thenReturn(Optional.of(fa));

        Person p = service.getFlightAttendantById(id);

        assertNotNull(p);
        assertTrue(p instanceof FlightAttendant);
        assertEquals(id, ((FlightAttendant)p).getId());
    }

    @Test
    void getFlightAttendantById_notFound_throws() {
        Long id = 999L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.getFlightAttendantById(id));
        assertEquals("FlightAttendant " + id + " not found", e.getMessage());
    }

    // ---------- READ ALL ----------
//    @Test
//    void getAllFlightAttendants_returnsListFromRepo() {
//        FlightAttendant a = new FlightAttendant(); a.setId(1L);
//        FlightAttendant b = new FlightAttendant(); b.setId(2L);
//        when(repo.findAll()).thenReturn(List.of(a, b));
//
//        List<Person> all = service.getAllFlightAttendants();
//
//        assertNotNull(all);
//        assertEquals(2, all.size());
//        assertTrue(all.get(0) instanceof FlightAttendant);
//    }

    // ---------- UPDATE ----------
    @Test
    void updateFlightAttendant_valid_updatesFields_andSaves_andReturnsDTO() {
        Long id = 77L;
        FlightAttendant existing = new FlightAttendant();
        existing.setId(id);
        existing.setEmail("old@mail.com");
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setPassword("oldpass");

        when(repo.findById(id)).thenReturn(Optional.of(existing));

        FlightAttendantRequestDTO req = mock(FlightAttendantRequestDTO.class);
        when(req.getEmail()).thenReturn("new@mail.com");
        when(req.getFirstName()).thenReturn("New");
        when(req.getLastName()).thenReturn("Name");
        when(req.getPassword()).thenReturn("newpass");

        FlightAttendant saved = new FlightAttendant();
        saved.setId(id);
        saved.setEmail("new@mail.com");
        saved.setFirstName("New");
        saved.setLastName("Name");
        saved.setPassword("newpass");

        when(repo.save(any(Person.class))).thenReturn(saved);

        FlightAttendantResponse dto = service.updateFlightAttendant(id, req);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("new@mail.com", dto.getEmail());
        assertEquals("New", dto.getFirstName());
        assertEquals("Name", dto.getLastName());
        verify(repo, times(1)).save(any(Person.class));
    }

    @Test
    void updateFlightAttendant_notFound_throws() {
        Long id = 404L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        FlightAttendantRequestDTO req = mock(FlightAttendantRequestDTO.class);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.updateFlightAttendant(id, req));
        assertEquals("FlightAttendant " + id + " not found", e.getMessage());
        verify(repo, never()).save(any());
    }

    // ---------- DELETE ----------
    @Test
    void deleteFlightAttendant_valid_deletes() {
        Long id = 22L;
        FlightAttendant fa = new FlightAttendant(); fa.setId(id);
        when(repo.findById(id)).thenReturn(Optional.of(fa));

        service.deleteFlightAttendant(id);

        verify(repo, times(1)).delete(fa);
    }

    @Test
    void deleteFlightAttendant_notFound_throws() {
        Long id = 123L;
        when(repo.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.deleteFlightAttendant(id));
        assertEquals("FlightAttendant " + id + " not found", e.getMessage());
        verify(repo, never()).delete(any());
    }
}
