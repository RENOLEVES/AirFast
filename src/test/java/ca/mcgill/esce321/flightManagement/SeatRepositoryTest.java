package ca.mcgill.esce321.flightManagement;

import ca.mcgill.esce321.flightManagement.model.*;
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
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SeatRepositoryTest {

    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PersonRepository personRepository;

    Seat s1 = new Seat();
    Owner o1 = new Owner();
    Flight f1 = new Flight();

    @BeforeEach
    void setUp() {
        seatRepository.deleteAll();
        personRepository.deleteAll();
        flightRepository.deleteAll();

        Owner owner = new Owner("owner@gmail.com", "123456", "o1","o2");
        o1 = personRepository.save(owner);
        personRepository.save(o1);

        LocalDateTime expectedDepartTime = LocalDateTime.of(2025, 10, 6, 11, 15, 0);
        f1 = new Flight(100,expectedDepartTime,"Montreal","Toronto",11,3,true);

        flightRepository.save(f1);

        Seat seat = new Seat(SeatClass.ECONOMY, 100, "A6",SeatStatus.AVAILABLE,f1);
        seat.setOwner(o1);
        s1 = seatRepository.save(seat);
    }

    @Test
    void testSaveSeat() {
        //create entity
        LocalDateTime expectedDepartTime = LocalDateTime.of(2024, 10, 6, 1, 15, 0);
        Flight flight = new Flight(100,expectedDepartTime,"Texas","Ottawa",100,3,true);

        Flight f2 = flightRepository.save(flight);

        Seat seat = new Seat(SeatClass.ECONOMY, 100, "A6",SeatStatus.AVAILABLE,f2);
        seat.setOwner(o1);
        Seat s2  = seatRepository.save(seat);

        assertThat(s2.getSeatId()).isNotNull();
    }

    @Test
    void testReadSeat() {
        //read
        Seat s2 = seatRepository.findBySeatId(s1.getSeatId());

        assertThat(s2).isNotNull();
        assertThat(Objects.equals(s2.getSeatId(), s1.getSeatId()));
        assertThat(s2.getOwner() == o1);
        assertThat(s2.getSeatClass() == SeatClass.ECONOMY);
    }

    @Test
    void testUpdateSeat(){
        //read
        Seat s2 = seatRepository.findBySeatId(s1.getSeatId());

        //update
        s2.setSeatClass(SeatClass.BUSINESS);
        s2.setPrice(200);

        seatRepository.save(s2);

        Seat s3 = seatRepository.findBySeatId(s2.getSeatId());

        assertThat(s3).isNotNull();
        assertThat(s3.getSeatClass() == SeatClass.BUSINESS);
        assertThat(s3.getPrice()==200);
    }

    @Test
    void testOwnerAndSeat(){
        Seat s2 = seatRepository.findBySeatId(s1.getSeatId());
        Owner o2 = s2.getOwner();

        assertThat(s2).isNotNull();
        assertThat(o2).isNotNull();
        assertThat(o2.getId()).isEqualTo(o1.getId());
        assertThat(o2.getEmail()).isEqualTo("owner@gmail.com");
    }

    @Test
    void testDeleteSeat(){
        seatRepository.delete(s1);

        Seat s2 = seatRepository.findBySeatId(s1.getSeatId());
        assertThat(s2).isNull();
    }

}
