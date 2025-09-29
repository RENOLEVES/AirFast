package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    private int capacity;
    private Date departTime;
    private Date arrivalTime;
    private String departLocation;
    private String arrivalLocation;
    @ManyToOne
    @JoinColumn(name = "flight_attendant_id", referencedColumnName = "id", nullable = false)
    private FlightAttendant attendant;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id", nullable = false)
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "pilot_id", referencedColumnName = "id", nullable = false)
    private Pilot pilot;

    public Flight(int capacity, Date departTime, Date arrivalTime, String departLocation, String arrivalLocation) {
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

    public Date getDepartTime() {
        return departTime;
    }

    public void setDepartTime(Date departTime) {
        this.departTime = departTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
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
}
