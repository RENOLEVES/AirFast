package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OwnerRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    Owner o1 = new Owner();

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();

        Owner owner = new Owner("owner@gmail.com", "123456", "Lili","Wei");
        o1 = personRepository.save(owner);
    }

    @Test
    void testSaveOwner() {
        //create entity
        Owner owner = new Owner("ken.dubien@gmail.com", "654321", "Ken","Dubien");

        //save entity
        Owner s1 = personRepository.save(owner);

        assertThat(s1.getId()).isNotNull();
    }
    @Test
    void testReadOwner() {
        //read
        Optional<Person> optPerson = personRepository.findByEmail("owner@gmail.com");
        Owner o1 = (Owner) optPerson.orElse(null);

        assertThat(o1).isNotNull();
        assertThat(o1.getFirstName()).isEqualTo("Lili");
        assertThat(o1.getLastName()).isEqualTo("Wei");
    }
    @Test
    void testUpdateOwner(){
        //read
        Optional<Person> optO2 = personRepository.findByEmail("owner@gmail.com");
        Owner o2 = (Owner) optO2.orElse(null);

        //update
        o2.setEmail("joe.lee@gmail.com");
        o2.setFirstName("Joe");
        o2.setLastName("Lee");
        personRepository.save(o2);

        Optional<Person> optO3 = personRepository.findByEmail("joe.lee@gmail.com");
        Owner o3 = (Owner) optO3.orElse(null);

        assertThat(o3).isNotNull();
        assertThat(o3.getFirstName()).isEqualTo("Joe");
        assertThat(o3.getLastName()).isEqualTo("Lee");
    }

    @Test
    void testDeleteOwner(){
        personRepository.delete(o1);

        Optional<Person> optO2 = personRepository.findByEmail("owner@gmail.com");
        Owner o2 = (Owner) optO2.orElse(null);
        assertThat(o2).isNull();
    }

}
