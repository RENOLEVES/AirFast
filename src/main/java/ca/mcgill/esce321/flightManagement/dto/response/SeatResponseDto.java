package ca.mcgill.esce321.flightManagement.dto.response;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.model.SeatClass;
import ca.mcgill.esce321.flightManagement.model.SeatStatus;
import ca.mcgill.esce321.flightManagement.model.Flight;


public class SeatResponseDto {

    private SeatClass seatClass;
    private double price;
    private String seatNumber;
    private Flight flight;
    private SeatStatus seatStatus;


    @SuppressWarnings("unused")
    private SeatResponseDto() {

    }

    public SeatResponseDto(SeatClass seatClass, double price, String seatNumber, SeatStatus seatStatus, Flight flight) {
            
        this.seatClass = seatClass;
        this.price = price;
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus;
        this.flight = flight;
        
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }

    public double getPrice() {
        return price;

    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatStatus getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(SeatStatus seatStatus) {
        this.seatStatus = seatStatus;
        
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }




    
}
