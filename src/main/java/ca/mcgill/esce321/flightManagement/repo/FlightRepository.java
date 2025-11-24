package ca.mcgill.esce321.flightManagement.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.mcgill.esce321.flightManagement.model.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    // Explicit search
    Flight findByFlightId(Long flightId);

       // Search flights between two dates (departure time)
    List<Flight> findByDepartTimeBetween(LocalDateTime start, LocalDateTime end);

    // Alternatively, using @Query
    @Query("SELECT f FROM Flight f WHERE f.departTime BETWEEN :start AND :end")
    List<Flight> searchFlightsBetweenDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


   
    // Prevent duplicates (used in create)
    // boolean existsByFlightNumberAndDepartTime(String flightNumber, LocalDateTime departTime);
}
