package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;
import ca.mcgill.esce321.flightManagement.service.SeatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SeatServiceImplTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatServiceImpl seatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- TEST findSeatById ----------
    @Test
    void testFindSeatById() {
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

        SeatResponseDTO seat = seatService.findSeatById(seatId);


        assertNotNull(seat);
        assertEquals(SeatClass.ECONOMY, seat.getSeatClass());
        assertEquals(100, seat.getPrice());
        assertEquals("A1", seat.getSeatNumber());
        assertEquals(SeatStatus.AVAILABLE, seat.getSeatStatus());
        assertEquals(f.getFlightId(), seat.getFlightId());
        assertEquals(s.getSeatId(), seat.getSeatId());


        verify(seatRepository, times(1)).findAll();
        
    }


    // ---------- TEST viewAllSeats ----------
    @Test
    void testViewAllSeats() {
        Flight f = new Flight();
        Seat s = new Seat();
        s.setSeatClass(SeatClass.ECONOMY); //     BUSINESS, ECONOMY
        s.setPrice(100);
        s.setSeatNumber("A1");
        s.setSeatStatus(SeatStatus.AVAILABLE); // TOBEDETERMINED, AVAILABLE
        s.setFlight(f);

        
        when(seatRepository.findAll()).thenReturn(List.of(s));

        List<SeatResponseDTO> result = seatService.viewAllSeats();

        assertEquals(1, result.size());

        assertEquals(SeatClass.ECONOMY, result.get(0).getSeatClass());
        assertEquals(100, result.get(0).getPrice());
        assertEquals("A1", result.get(0).getSeatNumber());
        assertEquals(SeatStatus.AVAILABLE, result.get(0).getSeatStatus());
        assertEquals(f.getFlightId(), result.get(0).getFlightId());


        verify(seatRepository, times(1)).findAll();
    }









}
