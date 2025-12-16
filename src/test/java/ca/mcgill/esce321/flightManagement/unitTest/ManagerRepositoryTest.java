package ca.mcgill.esce321.flightManagement.unitTest;

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
public class ManagerRepositoryTest {
     @Autowired
    private PersonRepository personRepository;

    Manager m1 = new Manager();
    Owner o1 = new Owner();
    
    @BeforeEach
    void setUp() {
        personRepository.deleteAll();

        Owner owner = new Owner("owner@gmail.com", "123456", "o1","o2");
        o1 = personRepository.save(owner);

        Manager manager = new Manager("eric.zhao@gmail.com", "123456", "Eric","Zhao");
        m1 = personRepository.save(manager);
    }

    @Test
    void testSaveManager() {
        //create entity
        Manager manager = new Manager("ken.dubien@gmail.com", "654321", "Ken","Dubien");

        //save entity
        Manager m1 = personRepository.save(manager);

        assertThat(m1.getEmail()).isNotNull();
    }

    @Test
    void testReadManager() {
        //read
        Manager m1 = (Manager) personRepository.findByEmail("eric.zhao@gmail.com");

        assertThat(m1).isNotNull();
        assertThat(m1.getFirstName()).isEqualTo("Eric");
        assertThat(m1.getLastName()).isEqualTo("Zhao");
    }

    @Test
    void testUpdateManager(){
        //read
        Manager m2 = (Manager) personRepository.findByEmail("eric.zhao@gmail.com");

        //update
        m2.setEmail("joe.lee@gmail.com");
        m2.setFirstName("Joe");
        m2.setLastName("Lee");
        personRepository.save(m1);

        Manager m3 = (Manager) personRepository.findByEmail("joe.lee@gmail.com");

        assertThat(m3).isNotNull();
        assertThat(m3.getFirstName()).isEqualTo("Joe");
        assertThat(m3.getLastName()).isEqualTo("Lee");
    }

    @Test
    void testOwnerAndManager(){
        Manager m2 = (Manager) personRepository.findByEmail(m1.getEmail());

        assertThat(m2).isNotNull();
    }

    @Test
    void testDeleteManager(){
        personRepository.delete(m1);

        Manager m2 = (Manager) personRepository.findByEmail("eric.zhao@gmail.com");
        assertThat(m2).isNull();
    }

}


