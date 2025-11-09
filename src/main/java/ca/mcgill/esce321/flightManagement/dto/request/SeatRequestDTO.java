package ca.mcgill.esce321.flightManagement.dto.request;

import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.SeatClass;
import ca.mcgill.esce321.flightManagement.model.SeatStatus;


public class SeatRequestDTO {

    private Long flightId;   // reference to Flight
    private SeatClass seatClass;
    private double price;
    private String seatNumber;
    private SeatStatus seatStatus;


    public SeatRequestDTO() {}

    public SeatRequestDTO(Long flightId,
                          SeatClass seatClass, double price, String seatNumber, SeatStatus seatStatus) {
        this.flightId = flightId;
        this.seatClass = seatClass;
        this.price = price;
        this.seatNumber = seatNumber;
        this.seatStatus = seatStatus;
    }

    // Getters and Setters

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