package ca.mcgill.esce321.flightManagement.integrationTest;

import ca.mcgill.esce321.flightManagement.dto.request.BookingRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponseDTO;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingIntegrationTests {

    @Autowired private TestRestTemplate client;

    @Autowired private PersonRepository personRepo;
    @Autowired private FlightRepository flightRepo;
    @Autowired private SeatRepository seatRepo;
    @Autowired private BookingRepository bookingRepo;

    private Long customerId;
    private Long seatId;
    private Long bookingId;

    @BeforeAll
    void seed() {
        // Clean slate
        bookingRepo.deleteAll();
        seatRepo.deleteAll();
        flightRepo.deleteAll();
        personRepo.deleteAll();

        // Customer
        Customer c = new Customer("alice@example.com", "pw", "Alice", "Smith", 123456,100,0);
        c.setPoints(0);
        customerId = personRepo.save(c).getId();

        // Flight + Seat
        Flight f = new Flight(
                120,
                LocalDateTime.now().plusDays(3),
                "YUL",
                "YYZ",
                "AC200",
                1.3,
                false
        );
        f = flightRepo.save(f);

        Seat seat = new Seat(SeatClass.ECONOMY, 199.0, "15C", SeatStatus.AVAILABLE, f);
        seatId = seatRepo.save(seat).getSeatId();
    }

    @Test
    @Order(1)
    void createBooking_success() {
        BookingRequestDTO req = new BookingRequestDTO(customerId, seatId, null, null, null);

        ResponseEntity<BookingResponseDTO> resp =
                client.postForEntity("/api/bookings", req, BookingResponseDTO.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resp.getBody()).isNotNull();
        bookingId = resp.getBody().getBookingId();
        assertThat(resp.getBody().getCustomerId()).isEqualTo(customerId);
        assertThat(resp.getBody().getSeatId()).isEqualTo(seatId);
        assertThat(resp.getBody().getPaymentStatus()).isEqualTo(PaymentStatus.NOTPAID);
    }

    @Test
    @Order(2)
    void getBooking_success() {
        ResponseEntity<BookingResponseDTO> resp =
                client.getForEntity("/api/bookings/" + bookingId, BookingResponseDTO.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getBookingId()).isEqualTo(bookingId);
    }

    @Test
    @Order(3)
    void listBookings_success() {
        ResponseEntity<BookingResponseDTO[]> resp =
                client.getForEntity("/api/bookings", BookingResponseDTO[].class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotEmpty();
    }

    @Test
    @Order(4)
    void updateBooking_setPaid() {
        BookingRequestDTO update = new BookingRequestDTO(
                null, null, LocalDateTime.now(), PaymentStatus.PAID, BookingStatus.CONFIRMED
        );

        HttpEntity<BookingRequestDTO> entity = new HttpEntity<>(update);
        ResponseEntity<BookingResponseDTO> resp =
                client.exchange("/api/bookings/" + bookingId, HttpMethod.PUT, entity, BookingResponseDTO.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
    }

    @Test
    @Order(5)
    void cancelBookingByCustomer_success() {
        ResponseEntity<Void> resp =
                client.postForEntity("/api/bookings/" + bookingId + "/cancel", null, Void.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify status changed
        ResponseEntity<BookingResponseDTO> get =
                client.getForEntity("/api/bookings/" + bookingId, BookingResponseDTO.class);

        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody()).isNotNull();
        assertThat(get.getBody().getBookingStatus()).isEqualTo(BookingStatus.CANCELLED_BY_CUSTOMER);
    }

    @Test
    @Order(6)
    void deleteBooking_success() {
        ResponseEntity<Void> resp =
                client.exchange("/api/bookings/" + bookingId, HttpMethod.DELETE, null, Void.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify gone
        ResponseEntity<String> get =
                client.getForEntity("/api/bookings/" + bookingId, String.class);
        assertThat(get.getStatusCode()).isIn(HttpStatus.NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
