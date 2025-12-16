package ca.mcgill.esce321.flightManagement.controller.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import ca.mcgill.esce321.flightManagement.model.FlightStatus;

public class FlightRequestDTO {

    private int capacity;
    private LocalDateTime expectedDepartTime;
    private LocalDateTime departTime;     
    private LocalDateTime arrivalTime;    
    private String departLocation;
    private String arrivalLocation;
    private String flightNumber;             
    private double flightTime;               
    private boolean isRecurring;
    private Boolean isActive;             
    private Integer delayHours;
    private int seatsRemaining; 
    private FlightStatus status;      private String departTimeStr;  // For search with date string
    private String arrivalTimeStr; // For search with date string


    public FlightRequestDTO() {}

    public FlightRequestDTO(
                            LocalDateTime departTime,
                            LocalDateTime arrivalTime,
                            String departLocation,
                            String arrivalLocation){
        this.departLocation = departLocation;
        this.arrivalLocation = arrivalLocation;
        this.departTime = departTime;
        this.arrivalTime = arrivalTime;
    }

    public FlightRequestDTO(int capacity,
                            LocalDateTime expectedDepartTime,
                            String departLocation,
                            String arrivalLocation,
                            String flightNumber,
                            double flightTime,
                            boolean isRecurring,
                            Boolean isActive,
                            FlightStatus status) {
        this.capacity = capacity;
        this.expectedDepartTime = expectedDepartTime;
        this.departLocation = departLocation;
        this.arrivalLocation = arrivalLocation;
        this.flightNumber = flightNumber;
        this.flightTime = flightTime;
        this.isRecurring = isRecurring;
        this.status = status;
        this.isActive = true;
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

    public Integer getDelayHours() {
        return delayHours;
    }

    public void setDelayHours(Integer delayHours) {
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

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setStatus(FlightStatus status){
        this.status = status;
    }

    public FlightStatus getStatus() {
        return this.status;
    }

    // Add these getters and setters at the bottom
    public String getDepartTimeStr() {
        return departTimeStr;
    }

    public void setDepartTimeStr(String departTimeStr) {
        this.departTimeStr = departTimeStr;
    }

    public String getArrivalTimeStr() {
        return arrivalTimeStr;
    }

    public void setArrivalTimeStr(String arrivalTimeStr) {
        this.arrivalTimeStr = arrivalTimeStr;
    }

    // Add helper methods to convert strings to LocalDateTime
    public LocalDateTime getDepartDateTimeFromStr() {
        if (departTimeStr != null && !departTimeStr.isEmpty()) {
            return LocalDate.parse(departTimeStr).atStartOfDay();
        }
        return departTime; // Fall back to existing field
    }

    public LocalDateTime getArrivalDateTimeFromStr() {
        if (arrivalTimeStr != null && !arrivalTimeStr.isEmpty()) {
            return LocalDate.parse(arrivalTimeStr).atTime(23, 59, 59);
        }
        return arrivalTime; // Fall back to existing field
    }

}

