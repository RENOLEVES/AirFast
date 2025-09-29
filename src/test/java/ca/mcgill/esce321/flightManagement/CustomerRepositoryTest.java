package ca.mcgill.esce321.flightManagement;

import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FlightRepository flightRepository;

    @Test
    void testSaveCustomer() {

        //create entity
        Customer customer = new Customer("eric.zhao@gmail.com", "123456", "Eric","Zhao",true);
        customer.setPoints(500);

        //save entity
        Customer s1 = personRepository.save(customer);

        assertThat(s1.getId()).isNotNull();

        //read
        Customer c1 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com");

        assertThat(c1).isNotNull();
        assertThat(c1.getFirstName()).isEqualTo("Eric");
        assertThat(c1.getLastName()).isEqualTo("Zhao");
        assertThat(c1.getPoints()).isEqualTo(500);
        assertThat(c1.isMember());

        //update
        c1.setEmail("joe.lee@gmail.com");
        c1.setFirstName("Joe");
        c1.setLastName("Lee");
        c1.setPoints(100);
        Customer s2 = personRepository.save(c1);

        Customer c2 = (Customer) personRepository.findByEmail("joe.lee@gmail.com");

        assertThat(c2).isNotNull();
        assertThat(c2.getFirstName()).isEqualTo("Joe");
        assertThat(c2.getLastName()).isEqualTo("Lee");
        assertThat(c2.getPoints()).isEqualTo(100);

        //reference
        LocalDateTime departTime1 = LocalDateTime.of(2025, 9, 29, 13, 25);
        LocalDateTime ArrivalTime1 = LocalDateTime.of(2025, 9, 29, 15, 0);

        LocalDateTime departTime2 = LocalDateTime.of(2025, 9, 30, 13, 25);
        LocalDateTime ArrivalTime2 = LocalDateTime.of(2025, 9, 30, 15, 0);

        //1 manager
        Manager m1 = new Manager("ken.dubien@gmail.com","000000","Ken","Dubien");

        personRepository.save(m1);


        //2 flight attendants
        FlightAttendant fa1 = new FlightAttendant("fa1@gmail.com","000000","faf1","fal1");
        FlightAttendant fa2 = new FlightAttendant("fa2@gmail.com","000000","faf2","fal2");

        personRepository.save(fa1);
        personRepository.save(fa2);

        //1 pilot
        Pilot p1 = new Pilot("p1@gmail.com","000000","pf1","pl1");

        personRepository.save(p1);

        //2 flights
        Flight f1 = new Flight(100,departTime1, ArrivalTime1,"Montreal", "Toronto");
        Flight f2 = new Flight(100,departTime2, ArrivalTime2,"Toronto", "Montreal");

        //assign employees to the f1
        f1.setAttendants(Arrays.asList(fa1,fa2));
        f1.setPilots(List.of(p1));
        f1.setManager(m1);
        //f2
        f2.setAttendants(Arrays.asList(fa1,fa2));
        f1.setPilots(List.of(p1));
        f2.setManager(m1);

        flightRepository.save(f1);
        flightRepository.save(f2);

        //1 customer
        Customer r1 = new Customer("r1@gmail.com", "123456", "r1","l1",true);

        Customer s3 = personRepository.save(r1);

        //2 bookings
        Booking b1 = new Booking(r1,f1,200.0);
        Booking b2 = new Booking(r1,f2,195.0);

        bookingRepository.save(b1);
        bookingRepository.save(b2);

        r1.setBookings(Arrays.asList(b1,b2));

        Customer c3 = (Customer) personRepository.findByEmail("r1@gmail.com");
        assertThat(c3.getBookings()).hasSize(2);
        assertThat(c3.getBookings()).extracting("price")
                .containsExactlyInAnyOrder(200.0,195.0);

        //deletion
        personRepository.delete(s2);
        personRepository.delete(s3);

        Customer c4 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com");
        Customer c5 = (Customer) personRepository.findByEmail("joe.lee@gmail.com");
        Customer c6 = (Customer) personRepository.findByEmail("r1@gmail.com");
        assertThat(c4).isNull();
        assertThat(c5).isNull();
        assertThat(c6).isNull();
    }

}
