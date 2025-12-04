package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Customer extends Person {

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    private int points;
    private int membershipNumber;
    private int timeInFlight;

    public Customer(){}
    public Customer(String email, String password, String firstName, String lastName,
                              int membershipNumber, int points, int timeInFlight){
        super(email, password,firstName,lastName);
        String shortDate = new SimpleDateFormat("MMddHH").format(new Date());
        this.membershipNumber = Integer.parseInt(shortDate);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(int membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public int getTimeInFlight() {
        return timeInFlight;
    }

    public void setTimeInFlight(int timeInFlight) {
        this.timeInFlight = timeInFlight;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

}
