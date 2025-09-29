package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
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

    private int capacity;
    private LocalDateTime departTime;
    private LocalDateTime arrivalTime;
    private String departLocation;
    private String arrivalLocation;

    public Flight(){}
    public Flight(int capacity, LocalDateTime departTime, LocalDateTime arrivalTime, String departLocation, String arrivalLocation) {
        this.capacity = capacity;
        this.departTime = departTime;
        this.arrivalTime = arrivalTime;
        this.departLocation = departLocation;
        this.arrivalLocation = arrivalLocation;
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
}
