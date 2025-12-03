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

    // Search with date range and locations
    @Query(value = "SELECT * FROM flight f WHERE " +
            "DATE(f.depart_time) >= DATE(:start) " +
            "AND DATE(f.depart_time) <= DATE(:end) " +
            "AND (:departLocation IS NULL OR :departLocation = '' OR LOWER(f.depart_location) LIKE LOWER(CONCAT('%', :departLocation, '%'))) " +
            "AND (:arrivalLocation IS NULL OR :arrivalLocation = '' OR LOWER(f.arrival_location) LIKE LOWER(CONCAT('%', :arrivalLocation, '%')))",
            nativeQuery = true)
    List<Flight> findFlightsByDateRangeAndLocations(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("departLocation") String departureLocation,
            @Param("arrivalLocation") String arrivalLocation
    );
}
