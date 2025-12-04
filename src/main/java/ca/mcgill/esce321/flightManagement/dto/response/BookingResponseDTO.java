package ca.mcgill.esce321.flightManagement.dto.response;

import java.time.LocalDateTime;
import ca.mcgill.esce321.flightManagement.model.BookingStatus;
import ca.mcgill.esce321.flightManagement.model.PaymentStatus;

public class BookingResponseDTO {

    private Long bookingId;
    private Long customerId;  // Reference to Customer
    private Long seatId;    // Reference to Flight

    private LocalDateTime bookingDate;
    private PaymentStatus paymentStatus;
    private BookingStatus bookingStatus;

    private String departLocation;
    private String arrivalLocation;
    private String flightNumber;
    private LocalDateTime departTime;
    private LocalDateTime arrivalTime;

    public BookingResponseDTO() {}

    public BookingResponseDTO(Long bookingId, Long customerId, Long seatId,
                              LocalDateTime bookingDate, PaymentStatus paymentStatus, BookingStatus bookingStatus) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.seatId = seatId;
        this.bookingDate = bookingDate;
        this.paymentStatus = paymentStatus;
        this.bookingStatus = bookingStatus;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long flightId) {
        this.seatId = flightId;
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

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
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

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public LocalDateTime getDepartTime() {
        return departTime;
    }

    public void setDepartTime(LocalDateTime departTime) {
        this.departTime = departTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
