package ca.mcgill.esce321.flightManagement;

import ca.mcgill.esce321.flightManagement.model.Customer;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
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

    @Test
    void testSaveAndFindCustomer() {
        Customer customer = new Customer("eric.zhao@gmail.com", "123456", "Eric","Zhao",true);
        customer.setPoints(500);

        Customer savedCustomer = personRepository.save(customer);

        assertThat(savedCustomer.getId()).isNotNull();

        Customer c1 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com");

        assertThat(c1).isNotNull();
        assertThat(c1.getFirstName()).isEqualTo("Eric");
        assertThat(c1.getLastName()).isEqualTo("Zhao");
        assertThat(c1.getPoints()).isEqualTo(500);
        assertThat(c1.isMember());


        personRepository.delete(savedCustomer);

        Customer c2 = (Customer) personRepository.findByEmail("eric.zhao@gmail.com");
        assertThat(c2).isNull();
    }
}
