package ca.mcgill.esce321.flightManagement.dto.request;

import java.time.LocalDateTime;

public class FlightRequestDTO {

    private int capacity;
    private LocalDateTime expectedDepartTime;
    private LocalDateTime departTime;     
    private LocalDateTime arrivalTime;    
    private String departLocation;
    private String arrivalLocation;
    private int flightNumber;             
    private int flightTime;               
    private boolean isRecurring;
    private Boolean isActive;             
    private Integer delayInHours;        

    public FlightRequestDTO() {}

    public FlightRequestDTO(int capacity,
                            LocalDateTime expectedDepartTime,
                            String departLocation,
                            String arrivalLocation,
                            int flightNumber,
                            int flightTime,
                            boolean isRecurring) {
        this.capacity = capacity;
        this.expectedDepartTime = expectedDepartTime;
        this.departLocation = departLocation;
        this.arrivalLocation = arrivalLocation;
        this.flightNumber = flightNumber;
        this.flightTime = flightTime;
        this.isRecurring = isRecurring;
    }

    // Getters and Setters
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

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public double getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(double flightTime) {
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
}
