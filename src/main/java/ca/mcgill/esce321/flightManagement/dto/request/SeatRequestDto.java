package ca.mcgill.esce321.flightManagement.dto.request;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.model.SeatClass;
import ca.mcgill.esce321.flightManagement.model.Flight;
import java.time.LocalDate;

public class SeatRequestDTO {

    private Long seatId;
    private Flight flight;
    
    @SuppressWarnings("unused")
    private SeatRequestDTO() {

    }

    public SeatRequestDTO(Long seatId, Flight flight) {
            
        this.seatId = seatId;
        this.flight = flight;
        
    }

    public Long getSeatId () {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

  

    public Flight getFlight() {
        return flight;

    }

    public void setFlight(Flight flight) {
        this.flight = flight;

    }
}
