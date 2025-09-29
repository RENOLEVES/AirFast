package ca.mcgill.esce321.flightManagement.model;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class Manager extends Employee{

    @OneToMany(mappedBy = "manager")
    private List<Flight> flights;
    public Manager(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
    }


    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
