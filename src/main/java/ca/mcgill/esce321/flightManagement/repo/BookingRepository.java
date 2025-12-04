package ca.mcgill.esce321.flightManagement.repo;

import ca.mcgill.esce321.flightManagement.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findByBookingId(Long bookingId);

    @Query("SELECT DISTINCT b FROM Booking b JOIN FETCH b.customer JOIN FETCH b.seat")
    List<Booking> findAllWithCustomerAndSeat();

    List<Booking> findAllByCustomer_Id(Long customerId);
}