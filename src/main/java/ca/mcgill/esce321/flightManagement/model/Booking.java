package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "flight_id", referencedColumnName = "flightId")
    private Flight flight;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;

    private LocalDateTime bookingDate;
    private PaymentStatus paymentStatus;
    private BookingStatus bookingStatus;

    public Booking(){}
    public Booking(Customer customer, Flight flight){
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = LocalDateTime.now();
        this.paymentStatus = PaymentStatus.NOTPAID;
        this.bookingStatus = BookingStatus.WAITLISTED;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}



