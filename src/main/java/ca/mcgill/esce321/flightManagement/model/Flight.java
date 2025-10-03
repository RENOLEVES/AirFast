package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @ManyToMany
    @JoinTable(
            name = "flight_attendant",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "attendant_id")
    )
    private List<FlightAttendant> attendants;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id", nullable = false)
    private Manager manager;

    @ManyToMany
    @JoinTable(
            name = "pilot",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "pilot_id")
    )
    private List<Pilot> pilots;

    @OneToMany(mappedBy = "flight",cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "flight",cascade = CascadeType.ALL)
    private List<Seat> seats;

    private int capacity;
    private int delayHours;
    private LocalDateTime departTime;
    private LocalDateTime arrivalTime;
    private LocalDateTime expectedDepartTime;
    private String departLocation;
    private String arrivalLocation;
    private int flightNumber;
    private int flightTime;
    private int seatsRemaining;
    private boolean isRecurring;
    private boolean isActive;
    private HashMap<String, Integer> bookingFrequencyPerCity;
    private String[] bookedCities;

    public Flight(){}
    public Flight(List<FlightAttendant> attendants, Manager manager, List<Pilot> pilots, int capacity,
                  LocalDateTime expectedDepartTime, String departLocation, String arrivalLocation,
                  int flightNumber, int flightTime, boolean isRecurring) {
        this.attendants = attendants;
        this.manager = manager;
        this.pilots = pilots;
        this.capacity = capacity;
        this.expectedDepartTime = expectedDepartTime;
        this.departLocation = departLocation;
        this.arrivalLocation = arrivalLocation;
        this.flightNumber = flightNumber;
        this.flightTime = flightTime;
        this.isRecurring = isRecurring;
        this.isActive = true;
    }

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

    public List<FlightAttendant> getAttendants() {
        return attendants;
    }

    public void setAttendants(List<FlightAttendant> attendants) {
        this.attendants = attendants;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<Pilot> getPilots() {
        return pilots;
    }

    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public int getDelayHours() {
        return delayHours;
    }

    public void setDelayHours(int delayHours) {
        this.delayHours = delayHours;
    }

    public LocalDateTime getExpectedDepartTime() {
        return expectedDepartTime;
    }

    public void setExpectedDepartTime(LocalDateTime expectedDepartTime) {
        this.expectedDepartTime = expectedDepartTime;
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

    public int getSeatsRemaining() {
        return seatsRemaining;
    }

    public void setSeatsRemaining(int seatsRemaining) {
        this.seatsRemaining = seatsRemaining;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public HashMap<String, Integer> getBookingFrequencyPerCity() {
        return bookingFrequencyPerCity;
    }

    public void setBookingFrequencyPerCity(HashMap<String, Integer> bookingFrequencyPerCity) {
        this.bookingFrequencyPerCity = bookingFrequencyPerCity;
    }

    public String[] getBookedCities() {
        return bookedCities;
    }

    public void setBookedCities(String[] bookedCities) {
        this.bookedCities = bookedCities;
    }
}
