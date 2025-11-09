package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock FlightRepository flightRepo;
    @InjectMocks FlightServiceImpl service;

    private FlightRequestDTO baseReq;

    @BeforeEach
    void setup() {
        baseReq = new FlightRequestDTO(
                120,
                LocalDateTime.now().plusDays(7),
                "YUL",
                "LAX",
                "AC101",
                5.5,
                true
        );
        baseReq.setActive(true);
    }

    @Test
    void createFlight_mapsAndSaves() {
        Flight saved = new Flight(
                baseReq.getCapacity(),
                baseReq.getExpectedDepartTime(),
                baseReq.getDepartLocation(),
                baseReq.getArrivalLocation(),
                baseReq.getFlightNumber(),
                baseReq.getFlightTime(),
                baseReq.isRecurring()
        );
        saved.setFlightId(42L);
        saved.setSeatsRemaining(baseReq.getCapacity());
        when(flightRepo.save(any(Flight.class))).thenReturn(saved);

        FlightResponseDTO out = service.createFlight(baseReq);

        assertThat(out.getFlightId()).isEqualTo(42L);
        assertThat(out.getCapacity()).isEqualTo(120);
        assertThat(out.getDepartLocation()).isEqualTo("YUL");
        assertThat(out.getArrivalLocation()).isEqualTo("LAX");
        assertThat(out.isRecurring()).isTrue();
    }

    @Test
    void getFlightById_found() {
        Flight f = new Flight(100,
                LocalDateTime.now().plusDays(2),
                "YUL","YYZ","AC11",3.0,true);
        f.setFlightId(10L);
        when(flightRepo.findById(10L)).thenReturn(Optional.of(f));

        FlightResponseDTO out = service.getFlightById(10L);

        assertThat(out.getFlightId()).isEqualTo(10L);
        assertThat(out.getCapacity()).isEqualTo(100);
    }

    @Test
    void getFlightById_notFoundThrows() {
        when(flightRepo.findById(10L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getFlightById(10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No Flight");
    }

    @Test
    void getAllFlights_returnsMappedList() {
        Flight f1 = new Flight(100, LocalDateTime.now().plusDays(1), "A","B","X1",2.0,false);
        f1.setFlightId(1L);
        Flight f2 = new Flight(110, LocalDateTime.now().plusDays(2), "C","D","X2",3.0,true);
        f2.setFlightId(2L);
        when(flightRepo.findAll()).thenReturn(List.of(f1, f2));

        List<FlightResponseDTO> out = service.getAllFlights();

        assertThat(out).hasSize(2);
        assertThat(out.get(0).getFlightId()).isEqualTo(1L);
        assertThat(out.get(1).getFlightId()).isEqualTo(2L);
    }

    @Test
    void updateFlight_mapsFields() {
        Flight existing = new Flight(90,
                LocalDateTime.now().plusDays(5),
                "YQB","YVR","AC9",4.0,false);
        existing.setFlightId(9L);
        existing.setSeatsRemaining(90);

        when(flightRepo.findById(9L)).thenReturn(Optional.of(existing));
        when(flightRepo.save(any(Flight.class))).thenAnswer(inv -> inv.getArgument(0));

        FlightRequestDTO update = new FlightRequestDTO(
                150,
                LocalDateTime.now().plusDays(10),
                "YUL",
                "SFO",
                "AC100",
                5.0,
                true
        );
        update.setActive(true);
        update.setSeatsRemaining(150);

        FlightResponseDTO out = service.updateFlight(9L, update);

        assertThat(out.getCapacity()).isEqualTo(150);
        assertThat(out.getDepartLocation()).isEqualTo("YUL");
        assertThat(out.getArrivalLocation()).isEqualTo("SFO");
        assertThat(out.getFlightNumber()).isEqualTo("AC100");
        assertThat(out.isRecurring()).isTrue();
        assertThat(out.isActive()).isTrue();
        assertThat(out.getSeatsRemaining()).isEqualTo(150);
    }

    @Test
    void deleteFlight_success() {
        Flight f = new Flight(100, LocalDateTime.now().plusDays(1), "A","B","X1",2.0,false);
        f.setFlightId(1L);
        when(flightRepo.findById(1L)).thenReturn(Optional.of(f));

        service.deleteFlight(1L);

        verify(flightRepo).delete(f);
    }

    @Test
    void deleteFlight_notFoundThrows() {
        when(flightRepo.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deleteFlight(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
