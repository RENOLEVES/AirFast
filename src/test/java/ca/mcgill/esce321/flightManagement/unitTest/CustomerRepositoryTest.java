package ca.mcgill.esce321.flightManagement.unitTest;

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

        Customer customer = new Customer("eric.zhao@gmail.com", "123456", "Eric","Zhao",123456, 0, 0);
        customer.setPoints(500);
        c1 = personRepository.save(customer);
    }

    @AfterEach
    void clean(){
        personRepository.deleteAll();
    }

    @Test
    void testSaveCustomer() {
        //create entity
        Customer customer = new Customer("ken.dubien@gmail.com", "654321", "Ken","Dubien",123456, 0, 0);
        customer.setPoints(200);

        //save entity
        Customer s1 = personRepository.save(customer);

        assertThat(s1.getId()).isNotNull();
    }

    @Test
    void testReadCustomer() {
        //read
        Customer c2 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com").orElseThrow();

        assertThat(c2).isNotNull();
        assertThat(c2.getFirstName()).isEqualTo("Eric");
        assertThat(c2.getLastName()).isEqualTo("Zhao");
        assertThat(c2.getPoints()).isEqualTo(500);
        assertThat((c2.getMembershipNumber()) == 123456);
    }

    @Test
    void testUpdateCustomer(){
        //read
        Customer c2 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com").orElseThrow();

        //update
        c2.setEmail("joe.lee@gmail.com");
        c2.setFirstName("Joe");
        c2.setLastName("Lee");
        c2.setPoints(100);
        personRepository.save(c2);

        Customer c3 = (Customer) personRepository.findByEmail("joe.lee@gmail.com").orElseThrow();

        assertThat(c3).isNotNull();
        assertThat(c3.getFirstName()).isEqualTo("Joe");
        assertThat(c3.getLastName()).isEqualTo("Lee");
        assertThat(c3.getPoints()).isEqualTo(100);
    }

    @Test
    void testOwnerAndCustomer(){
        Customer c2 = (Customer) personRepository.findById(c1.getId()).orElseThrow();

        assertThat(c2).isNotNull();
    }

    @Test
    void testDeleteCustomer(){
        personRepository.delete(c1);

        Customer c2 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com").orElse(null);
        assertThat(c2).isNull();
    }

}
