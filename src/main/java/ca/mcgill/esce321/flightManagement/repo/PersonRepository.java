package ca.mcgill.esce321.flightManagement.repo;

import ca.mcgill.esce321.flightManagement.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import ca.mcgill.esce321.flightManagement.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
    Optional<Person> findById(Long id);

    Optional<Person> findByEmail(String email);

}