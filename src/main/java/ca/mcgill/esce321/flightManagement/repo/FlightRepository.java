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

    // Custom search query (used for browsing flights)
    // @Query("""
    //     SELECT f FROM Flight f
    //     WHERE f.departLocation = :from
    //       AND f.arrivalLocation = :to
    //       AND DATE(f.departTime) = :date
    //       AND f.status <> 'CANCELLED'
    // """)
    // List<Flight> search(
    //     @Param("from") String from,
    //     @Param("to") String to,
    //     @Param("date") LocalDate date
    // );

    // Prevent duplicates (used in create)
    // boolean existsByFlightNumberAndDepartTime(String flightNumber, LocalDateTime departTime);
}
