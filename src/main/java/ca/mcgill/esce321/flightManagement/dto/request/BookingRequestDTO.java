package ca.mcgill.esce321.flightManagement.Dto.request;

import ca.mcgill.esce321.flightManagement.model.BookingStatus;
import ca.mcgill.esce321.flightManagement.model.PaymentStatus;

import java.time.LocalDateTime;

public class BookingRequestDTO {
    private Long customerId;
    private Long seatId;

    private LocalDateTime bookingDate;
    private PaymentStatus paymentStatus;
    private BookingStatus bookingStatus;

    public BookingRequestDTO() {}

    public BookingRequestDTO(Long customerId, Long seatId,
                             LocalDateTime bookingDate, PaymentStatus paymentStatus, BookingStatus bookingStatus) {
        this.customerId = customerId;
        this.seatId = seatId;
        this.bookingDate = bookingDate;
        this.paymentStatus = paymentStatus;
        this.bookingStatus = bookingStatus;
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
}
