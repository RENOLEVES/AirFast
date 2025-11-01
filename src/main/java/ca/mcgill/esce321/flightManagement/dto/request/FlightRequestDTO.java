package ca.mcgill.esce321.flightManagement.Dto.request;

import java.time.LocalDateTime;

public class FlightRequestDTO {

    private Long flightId;
    private int capacity;
    private int seatsRemaining;
    private int delayHours;
    private LocalDateTime departTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime expectedDepartTime;
    private String departLocation;
    private String arrivalLocation;
    private int flightNumber;
    private int flightTime;
    private boolean isRecurring;
    private boolean isActive;

    // References
    private Long managerId;
    private Long ownerId;

    // Summary counts
    private int totalPilots;
    private int totalAttendants;
    private int totalBookings;
    private int totalSeats;

    public FlightRequestDTO() {}

    public FlightRequestDTO(Long flightId,int flightNumber, Long managerId) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.managerId = managerId;
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

    public int getFlightTime() {
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

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public int getTotalPilots() {
        return totalPilots;
    }

    public void setTotalPilots(int totalPilots) {
        this.totalPilots = totalPilots;
    }

    public int getTotalAttendants() {
        return totalAttendants;
    }

    public void setTotalAttendants(int totalAttendants) {
        this.totalAttendants = totalAttendants;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}
