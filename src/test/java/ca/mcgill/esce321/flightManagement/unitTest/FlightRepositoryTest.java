package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PersonRepository personRepository;

    FlightAttendant fa1 = new FlightAttendant();
    Owner o1 = new Owner();
    Flight f1 = new Flight();

    @BeforeEach
    void setUp() {
        flightRepository.deleteAll();
        personRepository.deleteAll();

        Owner owner = new Owner("owner@gmail.com", "123456", "o1","o2");
        o1 = personRepository.save(owner);

        FlightAttendant flightAttendant = new FlightAttendant("eric.zhao@gmail.com", "123456", "Eric","Zhao");
        fa1 = personRepository.save(flightAttendant);

        LocalDateTime expectedDepartTime = LocalDateTime.of(2025, 10, 6, 11, 15, 0);
        Flight flight = new Flight(100,expectedDepartTime,"Montreal","Toronto","AC11",3,true);

        flight.setAttendants(List.of(fa1));
        f1 = flightRepository.save(flight);
    }

    @Test
    void testSaveFlight() {
        LocalDateTime expectedDepartTime = LocalDateTime.of(2024, 10, 6, 1, 15, 0);
        //create entity
        Flight flight = new Flight(100,expectedDepartTime,"Texas","Ottawa","AC11",3,true);

        //save entity
        Flight f2 = flightRepository.save(flight);

        assertThat(f2.getFlightId()).isNotNull();
    }

    @Test
    void testReadFlight() {
        //read
        Flight f2 = flightRepository.findByFlightId(f1.getFlightId());

        assertThat(f2).isNotNull();
        assertThat((f2.getFlightNumber()).equals("AC11"));
        assertThat((f2.getCapacity()) == 100);
        assertThat((f2.isRecurring()));
        assertThat((f2.getArrivalLocation())).isEqualTo("Toronto");
    }

    @Test
    void testFlightAttendantAndFlight(){
        Flight f2 = flightRepository.findByFlightId(f1.getFlightId());
        FlightAttendant fa2 = f2.getAttendants().get(0);

        assertThat(fa2).isNotNull();
        assertThat(fa2.getId()).isEqualTo(fa1.getId());
        assertThat(fa2.getEmail()).isEqualTo(fa1.getEmail());
    }

    @Test
    void testDeleteFlight(){
        flightRepository.delete(f1);

        Flight f2 = flightRepository.findByFlightId(f1.getFlightId());
        assertThat(f2).isNull();
    }

}
