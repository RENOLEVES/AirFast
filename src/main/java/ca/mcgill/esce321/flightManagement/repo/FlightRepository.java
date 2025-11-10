package ca.mcgill.esce321.flightManagement.repo;

import ca.mcgill.esce321.flightManagement.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    // Explicit search
    Flight findByFlightId(Long flightId);

   
    // Prevent duplicates (used in create)
    boolean existsByFlightNumberAndDepartTime(String flightNumber, LocalDateTime departTime);
}
