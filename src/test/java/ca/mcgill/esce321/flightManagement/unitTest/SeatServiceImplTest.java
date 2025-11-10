package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;
import ca.mcgill.esce321.flightManagement.service.SeatServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SeatServiceImplTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private SeatServiceImpl seatService;

    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // initializes @Mock and @InjectMocks
    }

    
    @Test
    void testCreateSeat() {
        Flight f = new Flight();
        Seat s = new Seat();
        s.setSeatClass(SeatClass.ECONOMY); //     BUSINESS, ECONOMY
        s.setPrice(100);
        s.setSeatNumber("A1");
        s.setSeatStatus(SeatStatus.AVAILABLE); // TOBEDETERMINED, AVAILABLE
        s.setFlight(f);
        
        Long seatId = 1L;
        s.setSeatId(seatId);


        when(seatRepository.findById(seatId)).thenReturn(Optional.of(s));

        SeatResponseDTO seat = seatService.getSeatById(seatId);


        assertNotNull(seat);
        assertEquals(SeatClass.ECONOMY, seat.getSeatClass());
        assertEquals(100, seat.getPrice());
        assertEquals("A1", seat.getSeatNumber());
        assertEquals(SeatStatus.AVAILABLE, seat.getSeatStatus());
        assertEquals(f.getFlightId(), seat.getFlightId());
        assertEquals(s.getSeatId(), seat.getSeatId());


        verify(seatRepository, times(1)).findById(seatId);
        
    }

     @Test
    void createSeatFlightDoesNotExist() {
        // Arrange
        SeatRequestDTO dto = new SeatRequestDTO();
        dto.setFlightId(999L); // Non-existent flight ID
        dto.setSeatClass(SeatClass.ECONOMY);
        dto.setPrice(100.0);
        dto.setSeatNumber("1A");
        dto.setSeatStatus(SeatStatus.AVAILABLE);

        // Mock the repository to return empty
        when(flightRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            seatService.createSeat(dto);
        });

        // Verify seatRepository.save() is never called
        verify(seatRepository, never()).save(any());
    }


    // ---------- TEST findSeatById ----------
    @Test
    void testGetSeatById() {
        Flight f = new Flight();
        Seat s = new Seat();
        s.setSeatClass(SeatClass.ECONOMY); //     BUSINESS, ECONOMY
        s.setPrice(100);
        s.setSeatNumber("A1");
        s.setSeatStatus(SeatStatus.AVAILABLE); // TOBEDETERMINED, AVAILABLE
        s.setFlight(f);
        
        Long seatId = 1L;
        s.setSeatId(seatId);


        when(seatRepository.findById(seatId)).thenReturn(Optional.of(s));

        SeatResponseDTO seat = seatService.getSeatById(seatId);


        assertNotNull(seat);
        assertEquals(SeatClass.ECONOMY, seat.getSeatClass());
        assertEquals(100, seat.getPrice());
        assertEquals("A1", seat.getSeatNumber());
        assertEquals(SeatStatus.AVAILABLE, seat.getSeatStatus());
        assertEquals(f.getFlightId(), seat.getFlightId());
        assertEquals(s.getSeatId(), seat.getSeatId());


        verify(seatRepository, times(1)).findById(seatId);
        
    }

    @Test
    void testGetSeatByIdNotFound() {
        // Arrange
        when(seatRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> seatService.getSeatById(999L)
        );

        assertEquals("There is no Seat with ID 999.", ex.getMessage());
        verify(seatRepository).findById(999L);
    }


    // ---------- TEST viewAllSeats ----------
    @Test
    void testGetAllSeats() {
        Flight f = new Flight();
        Seat s = new Seat();
        s.setSeatClass(SeatClass.ECONOMY); //     BUSINESS, ECONOMY
        s.setPrice(100);
        s.setSeatNumber("A1");
        s.setSeatStatus(SeatStatus.AVAILABLE); // TOBEDETERMINED, AVAILABLE
        s.setFlight(f);

        
        when(seatRepository.findAll()).thenReturn(List.of(s));

        List<SeatResponseDTO> result = seatService.getAllSeats();

        assertEquals(1, result.size());

        assertEquals(SeatClass.ECONOMY, result.get(0).getSeatClass());
        assertEquals(100, result.get(0).getPrice());
        assertEquals("A1", result.get(0).getSeatNumber());
        assertEquals(SeatStatus.AVAILABLE, result.get(0).getSeatStatus());
        assertEquals(f.getFlightId(), result.get(0).getFlightId());


        verify(seatRepository, times(1)).findAll();
    }

    @Test
    void testUpdateSeat() {
        // Arrange
        Flight flight = new Flight();
        flight.setFlightId(1L);

        Seat existingSeat = new Seat();
        existingSeat.setSeatId(10L);
        existingSeat.setSeatClass(SeatClass.ECONOMY);
        existingSeat.setSeatNumber("12");
        existingSeat.setSeatStatus(SeatStatus.AVAILABLE);
        existingSeat.setPrice(100.0);
        existingSeat.setFlight(flight);

        SeatRequestDTO dto = new SeatRequestDTO();
        dto.setSeatClass(SeatClass.BUSINESS);
        dto.setSeatNumber("1");
        dto.setSeatStatus(SeatStatus.TOBEDETERMINED);
        dto.setPrice(250.0);
        dto.setFlightId(1L);

        // Mocking repository behavior
        when(seatRepository.findById(10L)).thenReturn(Optional.of(existingSeat));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.save(any(Seat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SeatResponseDTO result = seatService.updateSeat(10L, dto);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getSeatId());
        assertEquals(1L, result.getFlightId());
        assertEquals(SeatClass.BUSINESS, result.getSeatClass());
        assertEquals(SeatStatus.TOBEDETERMINED, result.getSeatStatus());
        assertEquals(250.0, result.getPrice());
        assertEquals("1", result.getSeatNumber());

        verify(seatRepository).findById(10L);
        verify(flightRepository).findById(1L);
        verify(seatRepository).save(existingSeat);
    }

    @Test
    void testUpdateSeat_SeatNotFound() {
        // Arrange
        SeatRequestDTO dto = new SeatRequestDTO();
        dto.setFlightId(1L);

        when(seatRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> seatService.updateSeat(999L, dto)
        );

        assertEquals("No Seat found with ID 999", ex.getMessage());
        verify(seatRepository).findById(999L);
        verify(flightRepository, never()).findById(anyLong());
        verify(seatRepository, never()).save(any());
    }


    @Test
    void testDeleteSeat() {
        // Arrange
        Seat seat = new Seat();
        seat.setSeatId(1L);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        // Act
        seatService.deleteSeat(1L);

        // Assert
        verify(seatRepository).findById(1L);
        verify(seatRepository).delete(seat);
    }

    @Test
    void testDeleteSeat_NotFound() {
        // Arrange
        when(seatRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> seatService.deleteSeat(999L)
        );

        assertEquals("No Seat found with ID 999", ex.getMessage());
        verify(seatRepository).findById(999L);
        verify(seatRepository, never()).delete(any());
    }




}
