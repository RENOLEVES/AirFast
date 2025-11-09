package ca.mcgill.esce321.flightManagement.dto.response;

import ca.mcgill.esce321.flightManagement.model.FlightStatus;
import ca.mcgill.esce321.flightManagement.model.Seat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightResponseDTO {
    
    private Long flightId;
    private int capacity;
    private int seatsRemaining;           // derived
    private int delayHours;
    private LocalDateTime departTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime expectedDepartTime;
    private String departLocation;
    private String arrivalLocation;
    private int flightNumber;
    private int flightTime;               // minutes
    private boolean isRecurring;
    private boolean isActive;
    private FlightStatus status;          // was missed
    private List<Seat> seats;

    public FlightResponseDTO() {}

    public FlightResponseDTO(Long flightId, int capacity, int seatsRemaining,
                             int delayInHours,
                             LocalDateTime departTime, LocalDateTime arrivalTime, LocalDateTime expectedDepartTime,
                             String departLocation, String arrivalLocation,
                             int flightNumber, int flightTime,
                             boolean isRecurring, boolean isActive,
                             FlightStatus status) {
        this.flightId = flightId;
        this.capacity = capacity;
        this.seatsRemaining = seatsRemaining;
        this.delayHours = delayInHours;
        this.departTime = departTime;
        this.arrivalTime = arrivalTime;
        this.expectedDepartTime = expectedDepartTime;
        this.departLocation = departLocation;
        this.arrivalLocation = arrivalLocation;
        this.flightNumber = flightNumber;
        this.flightTime = flightTime;
        this.isRecurring = isRecurring;
        this.isActive = isActive;
        this.status = status;
    }

    // Getters and Setters
    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getSeatsRemaining() {
        return seatsRemaining;
    }

    public void setSeatsRemaining(int seatsRemaining) {
        this.seatsRemaining = seatsRemaining;
    }

    public int getDelayHours() {
        return delayHours;
    }

    public void setDelayHours(int delayHours) {
        this.delayHours = delayHours;
    }

    public LocalDateTime getDepartTime() {
        return departTime;
    }

    public void setDepartTime(LocalDateTime departTime) {
        this.departTime = departTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getExpectedDepartTime() {
        return expectedDepartTime;
    }

    public void setExpectedDepartTime(LocalDateTime expectedDepartTime) {
        this.expectedDepartTime = expectedDepartTime;
    }

    public String getDepartLocation() {
        return departLocation;
    }

    public void setDepartLocation(String departLocation) {
        this.departLocation = departLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public double getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(int flightTime) {
        this.flightTime = flightTime;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
