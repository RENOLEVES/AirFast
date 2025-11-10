package ca.mcgill.esce321.flightManagement.dto.response;

import ca.mcgill.esce321.flightManagement.model.FlightStatus;
import ca.mcgill.esce321.flightManagement.model.Seat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightResponseDTO {
    
    private Long flightId;
    private int capacity;
<<<<<<< Updated upstream
    private int delayHours;
=======
    private int seatsRemaining;           // derived
    private Integer delayHours;
>>>>>>> Stashed changes
    private LocalDateTime departTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime expectedDepartTime;
    private String departLocation;
    private String arrivalLocation;
<<<<<<< Updated upstream
    private String flightNumber;
    private int flightTime;               // minutes
    private int seatsRemaining;           // derived
=======

    private String flightNumber;
    private double flightTime;               

>>>>>>> Stashed changes
    private boolean isRecurring;
    private Boolean isActive;
    private FlightStatus status;          
    private List<Seat> seats = new ArrayList<>();

    public FlightResponseDTO() {}

    public FlightResponseDTO(Long flightId, int capacity, int seatsRemaining,
                             int delayInHours,
                             LocalDateTime departTime, LocalDateTime arrivalTime, LocalDateTime expectedDepartTime,
                             String departLocation, String arrivalLocation,
<<<<<<< Updated upstream
                             String flightNumber, int flightTime,
                             boolean isRecurring, boolean isActive,
=======
                             String flightNumber, double flightTime,
                             boolean isRecurring, Boolean isActive,
>>>>>>> Stashed changes
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

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getFlightTime() {
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

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

<<<<<<< Updated upstream
    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }
=======
    public void setStatus(FlightStatus status){
        this.status = status;
    }

    public FlightStatus getStatus() {
        return this.status;
    }
>>>>>>> Stashed changes
}
