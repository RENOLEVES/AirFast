package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Customer extends Person {

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Booking> bookings;
//    private String phoneNumber;
//    private String address;

    private int points;
    private boolean isMember;

    public Customer(){}
    public Customer(String email, String password, String firstName, String lastName, boolean isMember){
        super(email, password,firstName,lastName);
        this.isMember = isMember;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
