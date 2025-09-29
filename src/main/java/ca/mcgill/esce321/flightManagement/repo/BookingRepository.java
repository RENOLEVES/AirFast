package ca.mcgill.esce321.flightManagement.repo;

import ca.mcgill.esce321.flightManagement.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findBookingByFlightId(Long bookingId);
}