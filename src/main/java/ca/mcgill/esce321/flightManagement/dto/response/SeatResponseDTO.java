package ca.mcgill.esce321.flightManagement.dto.response;

import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.SeatClass;
import ca.mcgill.esce321.flightManagement.model.SeatStatus;

public class SeatResponseDTO {

    private Long seatId;
    private Long flightId;

    private SeatClass seatClass;
    private double price;
    private String seatNumber;
    private SeatStatus seatStatus;

    public SeatResponseDTO() {}

    public SeatResponseDTO(Long seatId, Long flightId, SeatClass seatClass, double price, String seatNumber, SeatStatus seatStatus) {
        this.seatId = seatId;
        this.flightId = flightId;
        this.seatClass = seatClass;
        this.price = price;
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus;
    }

    // Getters and Setters
    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
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


}
