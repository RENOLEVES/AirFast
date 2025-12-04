package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private SeatRepository seatRepository;

    Booking b1 = new Booking();
    Customer c1 = new Customer();
    Flight f1 = new Flight();

    Seat s1 = new Seat();

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
        bookingRepository.deleteAll();
        flightRepository.deleteAll();

        Customer customer = new Customer("eric.zhao@gmail.com", "123456", "Eric","Zhao", 0, 0, 0);
        customer.setPoints(500);
        c1 = personRepository.save(customer);

        LocalDateTime expectedDepartTime = LocalDateTime.of(2025, 10, 6, 11, 15, 0);
        Flight flight = new Flight(100,expectedDepartTime,"Montreal","Toronto","AC11",3,true);
        f1 = flightRepository.save(flight);

        Seat seat = new Seat(SeatClass.ECONOMY, 100, "A6",SeatStatus.AVAILABLE,f1);
        s1 = seatRepository.save(seat);

        Booking booking = new Booking(c1,s1);
        b1 = bookingRepository.save(booking);
    }

    @Test
    void testSaveBooking() {
        //create entity
        Customer customer = new Customer("ken.dubien@gmail.com", "654321", "Ken","Dubien", 0, 0, 0);
        Customer c2 = personRepository.save(customer);

        LocalDateTime expectedDepartTime = LocalDateTime.of(2024, 10, 6, 1, 15, 0);
        //create entity
        Flight flight = new Flight(100,expectedDepartTime,"Texas","Ottawa","AC11",3,true);
        Flight f2 = flightRepository.save(flight);

        Seat seat = new Seat(SeatClass.ECONOMY, 100, "A6",SeatStatus.AVAILABLE,f1);

        s1 = seatRepository.save(seat);

        Booking booking = new Booking(c2,s1);
        Booking b2 = bookingRepository.save(booking);

        assertThat(b2.getBookingId()).isNotNull();
    }

    @Test
    void testReadBooking() {
        //read
        Booking b2 = bookingRepository.findByBookingId(b1.getBookingId());

        assertThat(b2).isNotNull();
        assertThat(b2.getCustomer() == c1);
    }

    @Test
    void testUpdateBooking(){
        //read
        Booking b2 = bookingRepository.findByBookingId(b1.getBookingId());

        Customer customer = new Customer("ken.dubien@gmail.com", "654321", "Ken","Dubien",123456, 0, 0);
        Customer c2 = personRepository.save(customer);

        LocalDateTime expectedDepartTime = LocalDateTime.of(2024, 10, 6, 1, 15, 0);
        //create entity
        Flight flight = new Flight(100,expectedDepartTime,"Texas","Ottawa","AC11",3,true);
        Flight f2 = flightRepository.save(flight);

        //update
        b2.setCustomer(c2);
        bookingRepository.save(b2);

        Booking b3 = bookingRepository.findByBookingId(b2.getBookingId());

        assertThat(b3).isNotNull();
        assertThat(b3.getCustomer() == c2);
    }

    @Test
    void testOwnerAndBooking(){
        Booking b2 = bookingRepository.findByBookingId(b1.getBookingId());

        assertThat(b2).isNotNull();
    }

    @Test
    void testDeleteBooking(){
        bookingRepository.delete(b1);

        Booking b2 = bookingRepository.findByBookingId(b1.getBookingId());
        assertThat(b2).isNull();
    }

}