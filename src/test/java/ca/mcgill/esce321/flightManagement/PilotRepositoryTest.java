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

public class PilotRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    /*
     * A test suite of the persistence layer needs to be developed to try one read and one write
operation for each class of your domain model. The test suite should demonstrate that
your application can read and write (1) objects, (2) attribute values of objects, and (3)
references between objects. Test cases are required to demonstrate the persistence of
objects of all domain model types, and at least one attribute and at least one reference of
each object. Test data must be removed from the database after each test method.
     */

    @Test
    void testPilot() {
        //create entity
        Pilot pilot = new Pilot("eric.zhao@gmail.com", "123456", "Eric","Zhao");

        //save entity
        Pilot p1 = personRepository.save(pilot);

        assertThat(p1.getId()).isNotNull();

        Pilot c1 = (Pilot) personRepository.findByEmail("eric.zhao@gmail.com");

        //read
        assertThat(c1).isNotNull();
        assertThat(c1.getFirstName()).isEqualTo("Eric");
        assertThat(c1.getLastName()).isEqualTo("Zhao");
        assertThat(c1.getEmail()).isEqualTo("eric.zhao@gmail.com");
        assertThat(c1.getPassword()).isEqualTo("123456");

        //update
        c1.setEmail("joe.lee@gmail.com");
        c1.setFirstName("Joe");
        c1.setLastName("Lee");
        c1.setPassword("654321");
        Pilot s2 = personRepository.save(c1);

        Pilot c2 = (Pilot) personRepository.findByEmail("joe.lee@gmail.com");

        assertThat(c2).isNotNull();
        assertThat(c2.getFirstName()).isEqualTo("Joe");
        assertThat(c2.getLastName()).isEqualTo("Lee");
        assertThat(c2.getEmail()).isEqualTo("joe.lee@gmail.com");
        assertThat(c2.getPassword()).isEqualTo("654321");

        //deletion
        personRepository.delete(p1);
        personRepository.delete(c1);
        personRepository.delete(s2);
        personRepository.delete(c2);

        Pilot c4 = (Pilot) personRepository.findByEmail("eric.zhao@gmail.com");
        Pilot c5 = (Pilot) personRepository.findByEmail("joe.lee@gmail.com");
        assertThat(c4).isNull();
        assertThat(c5).isNull();
       
    }
        





}
