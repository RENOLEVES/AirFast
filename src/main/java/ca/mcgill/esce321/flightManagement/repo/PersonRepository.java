package ca.mcgill.esce321.flightManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.mcgill.esce321.flightManagement.model.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long>{
    Optional<Person> findById(Long id);
    Person findByEmail(String email);
}