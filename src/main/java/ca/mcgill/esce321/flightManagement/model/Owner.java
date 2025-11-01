package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;
// import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Owner extends Person{
    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<Flight> flights = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<Employee> employees = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<Customer> customers = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "owner_id")
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

    public List<Booking> getBookings() {return bookings;}

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}