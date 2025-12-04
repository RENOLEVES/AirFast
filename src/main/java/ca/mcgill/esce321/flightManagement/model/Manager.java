package ca.mcgill.esce321.flightManagement.model;
import jakarta.persistence.*;
// import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Manager extends Employee{
    // only getter and setters for attributes

    @OneToMany(mappedBy = "manager",cascade = CascadeType.ALL)
    private List<Flight> flights = new ArrayList<>();

    @OneToMany(mappedBy = "manager",cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    public Manager(){}
    public Manager(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName, "Manager");
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
