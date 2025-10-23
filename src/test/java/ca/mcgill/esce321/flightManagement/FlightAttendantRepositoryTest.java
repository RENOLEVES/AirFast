package ca.mcgill.esce321.flightManagement;

import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FlightAttendantRepositoryTest {
    
    @Autowired
    private PersonRepository personRepository;

    FlightAttendant fa1 = new FlightAttendant();
    Owner o1 = new Owner();
    
    @BeforeEach
    void setUp() {
      personRepository.deleteAll();

      Owner owner = new Owner("owner@gmail.com", "123456", "o1","o2");
      o1 = personRepository.save(owner);

      FlightAttendant flightAttendant = new FlightAttendant("eric.zhao@gmail.com", "123456", "Eric","Zhao");
      flightAttendant.setOwner(o1);
      fa1 = personRepository.save(flightAttendant);
    }

    @Test
    void testSaveFlightAttendant() {
        //create entity
        FlightAttendant flightAttendant = new FlightAttendant("toufic.jrab@gmail.com", "654321", "Toufic","Jrab");

        //save entity
        FlightAttendant s1 = personRepository.save(flightAttendant);

        assertThat(s1.getEmail()).isNotBlank();
    }

    @Test
    void testReadFlightAttendant() {
        //read
        FlightAttendant c1 = (FlightAttendant) personRepository.findByEmail("eric.zhao@gmail.com");

        assertThat(c1).isNotNull();
        assertThat(c1.getFirstName()).isEqualTo("Eric");
        assertThat(c1.getLastName()).isEqualTo("Zhao");
    }

    @Test
    void testUpdateFlightAttendant(){
        //read
        FlightAttendant fa2 = (FlightAttendant) personRepository.findByEmail("eric.zhao@gmail.com");

        //update
        fa2.setEmail("joe.lee@gmail.com");
        fa2.setFirstName("Joe");
        fa2.setLastName("Lee");
        
        personRepository.save(fa1);

        FlightAttendant c3 = (FlightAttendant) personRepository.findByEmail("joe.lee@gmail.com");

        assertThat(c3).isNotNull();
        assertThat(c3.getFirstName()).isEqualTo("Joe");
        assertThat(c3.getLastName()).isEqualTo("Lee");
    }

    @Test
    void testOwnerAndFlightAttendant(){
        FlightAttendant fa2 = (FlightAttendant) personRepository.findByEmail(fa1.getEmail());
        Owner o2 = fa1.getOwner();

        assertThat(fa2).isNotNull();
        assertThat(o2).isNotNull();
        assertThat(o2.getId()).isEqualTo(o1.getId());
        assertThat(o2.getEmail()).isEqualTo("owner@gmail.com");
    }

    @Test
    void testDeleteFlightAttendant(){
        personRepository.delete(fa1);

        FlightAttendant fa2 = (FlightAttendant) personRepository.findByEmail("eric.zhao@gmail.com");
        assertThat(fa2).isNull();
    }
        
}
