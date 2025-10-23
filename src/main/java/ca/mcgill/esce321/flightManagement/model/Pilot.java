package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Pilot extends Employee{
    @ManyToMany(mappedBy = "pilots",cascade = CascadeType.ALL)
    private List<Flight> flights;

    public Pilot(){}
    public Pilot(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
