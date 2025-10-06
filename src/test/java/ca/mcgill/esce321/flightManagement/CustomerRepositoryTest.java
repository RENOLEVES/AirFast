package ca.mcgill.esce321.flightManagement;

import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
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

    Customer c1 = new Customer();
    Owner o1 = new Owner();

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();

        Owner owner = new Owner("owner@gmail.com", "123456", "o1","o2");
        o1 = personRepository.save(owner);

        Customer customer = new Customer("eric.zhao@gmail.com", "123456", "Eric","Zhao",123456);
        customer.setPoints(500);
        customer.setOwner(o1);
        c1 = personRepository.save(customer);
    }

    @Test
    void testSaveCustomer() {
        //create entity
        Customer customer = new Customer("ken.dubien@gmail.com", "654321", "Ken","Dubien",123456);
        customer.setPoints(200);

        //save entity
        Customer s1 = personRepository.save(customer);

        assertThat(s1.getId()).isNotNull();
    }

    @Test
    void testReadCustomer() {
        //read
        Customer c1 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com");

        assertThat(c1).isNotNull();
        assertThat(c1.getFirstName()).isEqualTo("Eric");
        assertThat(c1.getLastName()).isEqualTo("Zhao");
        assertThat(c1.getPoints()).isEqualTo(500);
        assertThat((c1.getMembershipNumber()) == 123456);
    }

    @Test
    void testUpdateCustomer(){
        //read
        Customer c2 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com");

        //update
        c2.setEmail("joe.lee@gmail.com");
        c2.setFirstName("Joe");
        c2.setLastName("Lee");
        c2.setPoints(100);
        personRepository.save(c1);

        Customer c3 = (Customer) personRepository.findByEmail("joe.lee@gmail.com");

        assertThat(c3).isNotNull();
        assertThat(c3.getFirstName()).isEqualTo("Joe");
        assertThat(c3.getLastName()).isEqualTo("Lee");
        assertThat(c3.getPoints()).isEqualTo(100);
    }

    @Test
    void testOwnerAndCustomer(){
        Customer c2 = (Customer) personRepository.findById(c1.getId()).orElseThrow();
        Owner o2 = c1.getOwner();

        assertThat(c2).isNotNull();
        assertThat(o2).isNotNull();
        assertThat(o2.getId()).isEqualTo(o1.getId());
        assertThat(o2.getEmail()).isEqualTo("owner@gmail.com");
    }

    @Test
    void testDeleteCustomer(){
        personRepository.delete(c1);

        Customer c2 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com");
        assertThat(c2).isNull();
    }

}
