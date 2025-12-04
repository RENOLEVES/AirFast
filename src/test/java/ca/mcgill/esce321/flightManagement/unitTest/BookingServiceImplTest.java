package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.dto.request.BookingRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import ca.mcgill.esce321.flightManagement.service.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock BookingRepository bookingRepo;
    @Mock PersonRepository personRepo;
    @Mock SeatRepository seatRepo;

    @InjectMocks BookingServiceImpl service;

    private Customer customer;
    private Flight flight;
    private Seat seat;

    @BeforeEach
    void setup() {
        customer = new Customer("a@b.com","pw","Alice","Smith", 123456, 0, 0);
        customer.setId(1L);
        flight = new Flight(
            100,
            LocalDateTime.now().plusDays(3),
            "YUL","YYZ","AC11",3.0, true
        );

        flight.setFlightId(10L);

        seat = new Seat(SeatClass.ECONOMY, 199.0, "12A", SeatStatus.AVAILABLE, flight);
        seat.setSeatId(20L);
    }

    @Test
    void createBooking_success() {
        when(personRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(seatRepo.findById(20L)).thenReturn(Optional.of(seat));
        when(bookingRepo.findAll()).thenReturn(List.of()); // no conflicts

        Booking persisted = new Booking();
        persisted.setBookingId(99L);
        persisted.setCustomer(customer);
        persisted.setSeat(seat);
        persisted.setPaymentStatus(PaymentStatus.NOTPAID);
        persisted.setBookingStatus(BookingStatus.CONFIRMED);
        when(bookingRepo.save(any(Booking.class))).thenReturn(persisted);

        BookingRequestDTO req = new BookingRequestDTO(1L, 20L, null, null, null);

        BookingResponseDTO out = service.createBooking(req);

        assertThat(out.getBookingId()).isEqualTo(99L);
        assertThat(out.getCustomerId()).isEqualTo(1L);
        assertThat(out.getSeatId()).isEqualTo(20L);
        assertThat(out.getPaymentStatus()).isEqualTo(PaymentStatus.NOTPAID);
        assertThat(out.getBookingStatus()).isEqualTo(BookingStatus.CONFIRMED);
        verify(bookingRepo).save(any(Booking.class));
    }

    @Test
    void createBooking_failsWhenCustomerAlreadyBookedSameFlight() {
        when(personRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(seatRepo.findById(20L)).thenReturn(Optional.of(seat));

        // Existing booking by same customer on a different seat of the same flight
        Booking existing = new Booking();
        existing.setBookingId(77L);
        existing.setCustomer(customer);
        Seat otherSeat = new Seat(SeatClass.ECONOMY, 199.0, "12B", SeatStatus.AVAILABLE, flight);
        otherSeat.setSeatId(21L);
        existing.setSeat(otherSeat);
        existing.setBookingStatus(BookingStatus.CONFIRMED);
        when(bookingRepo.findAll()).thenReturn(List.of(existing));

        BookingRequestDTO req = new BookingRequestDTO(1L, 20L, null, null, null);

        assertThatThrownBy(() -> service.createBooking(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already has a booking");
    }

    @Test
    void createBooking_failsWhenSeatTaken() {
        when(personRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(seatRepo.findById(20L)).thenReturn(Optional.of(seat));

        Booking existing = new Booking();
        existing.setBookingId(77L);
        existing.setCustomer(new Customer("x@y.com","pw","X","Y", 111, 0, 0));
        existing.setSeat(seat);
        existing.setBookingStatus(BookingStatus.CONFIRMED);
        when(bookingRepo.findAll()).thenReturn(List.of(existing));

        BookingRequestDTO req = new BookingRequestDTO(1L, 20L, null, null, null);
        assertThatThrownBy(() -> service.createBooking(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Seat is already booked");
    }

    @Test
    void getBookingById_found() {
        Booking b = new Booking();
        b.setBookingId(5L);
        b.setCustomer(customer);
        b.setSeat(seat);
        when(bookingRepo.findById(5L)).thenReturn(Optional.of(b));

        BookingResponseDTO out = service.getBookingById(5L);

        assertThat(out.getBookingId()).isEqualTo(5L);
        assertThat(out.getCustomerId()).isEqualTo(1L);
        assertThat(out.getSeatId()).isEqualTo(20L);
    }

    @Test
    void getBookingById_notFound() {
        when(bookingRepo.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getBookingById(5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No booking");
    }

    @Test
    void updateBooking_updatesStatusesAndDate() {
        Booking b = new Booking();
        b.setBookingId(5L);
        b.setCustomer(customer);
        b.setSeat(seat);
        b.setPaymentStatus(PaymentStatus.NOTPAID);
        b.setBookingStatus(BookingStatus.CONFIRMED);
        when(bookingRepo.findById(5L)).thenReturn(Optional.of(b));

        LocalDateTime t = LocalDateTime.now().plusDays(1);
        BookingRequestDTO req = new BookingRequestDTO(null, null, t, PaymentStatus.PAID, BookingStatus.CONFIRMED);

        BookingResponseDTO out = service.updateBooking(5L, req);

        assertThat(out.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(out.getBookingDate()).isEqualTo(t);
        verify(bookingRepo, never()).save(any()); // JPA flush via @Transactional
    }

    @Test
    void updateBooking_changeSeat_okWhenFree() {
        Booking b = new Booking();
        b.setBookingId(5L);
        b.setCustomer(customer);
        b.setSeat(seat);

        Seat newSeat = new Seat(SeatClass.ECONOMY, 250.0, "14C", SeatStatus.AVAILABLE, flight);
        newSeat.setSeatId(30L);

        when(bookingRepo.findById(5L)).thenReturn(Optional.of(b));
        when(seatRepo.findById(30L)).thenReturn(Optional.of(newSeat));
        when(bookingRepo.findAll()).thenReturn(List.of(b)); // no other bookings taking newSeat

        BookingRequestDTO req = new BookingRequestDTO(null, 30L, null, null, null);
        BookingResponseDTO out = service.updateBooking(5L, req);

        assertThat(out.getSeatId()).isEqualTo(30L);
    }

    @Test
    void updateBooking_changeSeat_failsWhenTaken() {
        Booking b = new Booking();
        b.setBookingId(5L);
        b.setCustomer(customer);
        b.setSeat(seat);

        Seat newSeat = new Seat(SeatClass.ECONOMY, 250.0, "14C", SeatStatus.AVAILABLE, flight);
        newSeat.setSeatId(30L);

        Booking other = new Booking();
        other.setBookingId(9L);
        other.setSeat(newSeat);
        other.setBookingStatus(BookingStatus.CONFIRMED);

        when(bookingRepo.findById(5L)).thenReturn(Optional.of(b));
        when(seatRepo.findById(30L)).thenReturn(Optional.of(newSeat));
        when(bookingRepo.findAll()).thenReturn(List.of(b, other));

        BookingRequestDTO req = new BookingRequestDTO(null, 30L, null, null, null);

        assertThatThrownBy(() -> service.updateBooking(5L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already booked");
    }

    @Test
    void cancelByCustomer_setsStatus() {
        Booking b = new Booking();
        b.setBookingId(5L);
        b.setCustomer(customer);
        b.setSeat(seat);
        b.setBookingStatus(BookingStatus.CONFIRMED);
        when(bookingRepo.findById(5L)).thenReturn(Optional.of(b));

        service.cancelByCustomer(5L);

        assertThat(b.getBookingStatus()).isEqualTo(BookingStatus.CANCELLED_BY_CUSTOMER);
    }

    @Test
    void deleteBooking_notFoundThrows() {
        when(bookingRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.deleteBooking(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No booking");
    }
}
