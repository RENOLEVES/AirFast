package ca.mcgill.esce321.flightManagement.service.implementation;

import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;
import ca.mcgill.esce321.flightManagement.service.OwnerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final PersonRepository personRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    public OwnerServiceImpl(PersonRepository personRepository,
                            FlightRepository flightRepository,
                            BookingRepository bookingRepository,
                            SeatRepository seatRepository) {
        this.personRepository = personRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public List<CustomerResponseDTO> viewAllCustomers() {
        return personRepository.findAll().stream()
                .filter(p -> p instanceof Customer)
                .map(p -> (Customer) p)
                .map(c -> new CustomerResponseDTO(
                        c.getId(),
                        c.getEmail(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getMembershipNumber(),
                        c.getPoints(),
                        c.getTimeInFlight(),
                        c.getBookings().size()
                )).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeResponseDTO> viewAllEmployees() {
        return personRepository.findAll().stream()
                .filter(p -> p instanceof Employee)
                .map(p -> (Employee) p)
                .map(e -> {
                    if (e instanceof Pilot pilot) {
                        return new PilotResponseDTO(
                                pilot.getE_id(),
                                pilot.getEmail(),
                                pilot.getPassword(),
                                pilot.getFirstName(),
                                pilot.getLastName(),
                                pilot.getFlights().stream().map(Flight::getFlightId).toList()
                        );
                    } else if (e instanceof Manager manager) {
                        return new ManagerResponseDTO(
                                manager.getE_id(),
                                manager.getEmail(),
                                manager.getPassword(),
                                manager.getFirstName(),
                                manager.getLastName(),
                                manager.getFlights().stream().map(Flight::getFlightId).toList()
                        );
                    } else if (e instanceof FlightAttendant attendant) {
                        return new FlightAttendantResponseDTO(
                                attendant.getE_id(),
                                attendant.getEmail(),
                                attendant.getPassword(),
                                attendant.getFirstName(),
                                attendant.getLastName(),
                                attendant.getFlights().stream().map(Flight::getFlightId).toList()
                        );
                    } else {
                        return new EmployeeResponseDTO(
                                e.getE_id(),
                                e.getEmail(),
                                e.getPassword(),
                                e.getFirstName(),
                                e.getLastName()
                        );
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public List<FlightResponseDTO> viewAllFlights() {
        return flightRepository.findAll().stream()
                .map(f -> new FlightResponseDTO(
                        f.getFlightId(),
                        f.getCapacity(),
                        f.getSeatsRemaining(),
                        f.getDelayHours(),
                        f.getDepartTime(),
                        f.getArrivalTime(),
                        f.getExpectedDepartTime(),
                        f.getDepartLocation(),
                        f.getArrivalLocation(),
                        f.getFlightNumber(),
                        f.getFlightTime(),
                        f.isRecurring(),
                        f.isActive(),
                        f.getManager() != null ? f.getManager().getE_id() : null,
                        f.getOwner() != null ? f.getOwner().getId() : null,
                        f.getPilots().size(),
                        f.getAttendants().size(),
                        f.getBookings().size(),
                        f.getSeats().size()
                )).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDTO> viewAllBookings() {
        return bookingRepository.findAll().stream()
                .map(b -> new BookingResponseDTO(
                        b.getBookingId(),
                        b.getCustomer() != null ? b.getCustomer().getId() : null,
                        b.getSeat() != null ? b.getSeat().getSeatId() : null,
                        b.getOwner() != null ? b.getOwner().getId() : null,
                        b.getBookingDate(),
                        b.getPaymentStatus(),
                        b.getBookingStatus()
                )).collect(Collectors.toList());
    }

    @Override
    public List<SeatResponseDTO> viewAllSeats() {
        return seatRepository.findAll().stream()
                .map(s -> new SeatResponseDTO(
                        s.getSeatId(),
                        s.getFlight() != null ? s.getFlight().getFlightId() : null,
                        s.getOwner() != null ? s.getOwner().getId() : null,
                        s.getSeatClass(),
                        s.getPrice(),
                        s.getSeatNumber(),
                        s.getSeatStatus()
                )).collect(Collectors.toList());
    }

    @Override
    public double viewSalary(Employee employee) {
        return employee != null ? employee.getSalary() : 0.0;
    }

    @Override
    public double calculateTotalRevenue() {
        // Get all paid bookings
        List<BookingResponseDTO> paidBookings = viewAllBookings().stream()
                .filter(b -> b.getPaymentStatus() == PaymentStatus.PAID)
                .toList();

        // Sum the prices of the seats associated with each paid booking
        return paidBookings.stream()
                .mapToDouble(b -> {
                    if (b.getSeatId() != null) {
                        return seatRepository.findById(b.getSeatId())
                                .map(Seat::getPrice)
                                .orElse(0.0);
                    }
                    return 0.0;
                })
                .sum();
    }
}
