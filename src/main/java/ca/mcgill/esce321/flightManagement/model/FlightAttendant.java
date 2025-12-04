package ca.mcgill.esce321.flightManagement.model;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FlightAttendant extends Employee{

    @ManyToMany(mappedBy = "attendants",cascade = CascadeType.ALL)
    private List<Flight> flights = new ArrayList<>();

    public FlightAttendant(){}
    public FlightAttendant(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName, "Flight Attendant");
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }


}
