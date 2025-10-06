package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Owner extends Person{

    @OneToMany(mappedBy = "owner")
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Flight> flights = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Seat> seats = new ArrayList<>();

    private double totalRevenue;

    public Owner(){}
    public Owner(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}