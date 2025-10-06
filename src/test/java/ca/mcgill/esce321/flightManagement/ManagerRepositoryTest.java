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



public class ManagerRepositoryTest {
     @Autowired
    private PersonRepository personRepository;
    @Test
    void testManager() {
        //create entity
        Manager manager = new Manager("eric.zhao@gmail.com", "123456", "Eric","Zhao");

        //save entity
        Manager p1 = personRepository.save(manager);

        assertThat(p1.getId()).isNotNull();

        Manager c1 = (Manager) personRepository.findByEmail("eric.zhao@gmail.com");

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
        Manager s2 = personRepository.save(c1);

        Manager c2 = (Manager) personRepository.findByEmail("joe.lee@gmail.com");

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

        Manager c4 = (Manager) personRepository.findByEmail("eric.zhao@gmail.com");
        Manager c5 = (Manager) personRepository.findByEmail("joe.lee@gmail.com");
        assertThat(c4).isNull();
        assertThat(c5).isNull();
    }


}
