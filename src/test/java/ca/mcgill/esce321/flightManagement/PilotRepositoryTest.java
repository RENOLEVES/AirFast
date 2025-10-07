package ca.mcgill.esce321.flightManagement;

import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    Pilot p1 = new Pilot();
    Owner o1 = new Owner();

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();

        Owner owner = new Owner("owner@gmail.com", "123456", "o1","o2");
        o1 = personRepository.save(owner);

        Pilot pilot = new Pilot("toufic.jrab@gmail.com", "123456", "Toufic","Jrab");
        pilot.setOwner(o1);
        p1 = personRepository.save(pilot);
    }

    @Test
    void testSavePilot() {
        //create entity
        Pilot pilot = new Pilot("ken.dubien@gmail.com", "654321", "Ken","Dubien");

        //save entity
        Pilot p1 = personRepository.save(pilot);

        assertThat(p1.getEmail()).isNotNull();
    }

    @Test
    void testReadPilot() {
        //read
        Pilot p1 = (Pilot) personRepository.findByEmail("toufic.jrab@gmail.com");

        assertThat(p1).isNotNull();
        assertThat(p1.getFirstName()).isEqualTo("Toufic");
        assertThat(p1.getLastName()).isEqualTo("Jrab");
    }

    @Test
    void testUpdatePilot(){
        //read
        Pilot p2 = (Pilot) personRepository.findByEmail("toufic.jrab@gmail.com");

        //update
        p2.setEmail("joe.lee@gmail.com");
        p2.setFirstName("Joe");
        p2.setLastName("Lee");
        personRepository.save(p1);

        Pilot p3 = (Pilot) personRepository.findByEmail("joe.lee@gmail.com");

        assertThat(p3).isNotNull();
        assertThat(p3.getFirstName()).isEqualTo("Joe");
        assertThat(p3.getLastName()).isEqualTo("Lee");
    }

    @Test
    void testOwnerAndPilot(){
        Pilot m2 = (Pilot) personRepository.findByEmail(p1.getEmail());
        Owner o2 = p1.getOwner();

        assertThat(m2).isNotNull();
        assertThat(o2).isNotNull();
        assertThat(o2.getId()).isEqualTo(o1.getId());
        assertThat(o2.getEmail()).isEqualTo("owner@gmail.com");
    }

    @Test
    void testDeletePilot(){
        personRepository.delete(p1);

        Pilot p2 = (Pilot) personRepository.findByEmail("toufic.jrab@gmail.com");
        assertThat(p2).isNull();
    }

}
