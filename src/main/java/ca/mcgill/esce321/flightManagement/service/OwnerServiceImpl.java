package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.request.OwnerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class OwnerServiceImpl{

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

    public OwnerResponseDTO createOwner(OwnerRequestDTO dto) {
        // Date today = Date.valueOf(LocalDate.now());

        Owner ownerToCreate = new Owner(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        Owner saved = personRepository.save(ownerToCreate);
        return new OwnerResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getFirstName(),
                saved.getLastName()
        );
    }

    public OwnerResponseDTO findOwnerById(long id) {
        Optional<Person> p = personRepository.findById(id);
        if (p.isPresent() && p.get() instanceof Owner owner) {

            return new OwnerResponseDTO(
                    owner.getId(),
                    owner.getEmail(),
                    owner.getPassword(),
                    owner.getFirstName(),
                    owner.getLastName()
            );
        } else {
            throw new IllegalArgumentException("There is no Owner with ID " + id + ".");
        }
    }

    public List<CustomerResponseDTO> viewAllCustomers() {
        return personRepository.findAll().stream()
                .filter(p -> p instanceof Customer)
                .map(p -> (Customer) p)
                .map(c -> new CustomerResponseDTO(
                        c.getId(),
                        c.getEmail(),
                        c.getPassword(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getMembershipNumber(),
                        c.getPoints(),
                        c.getTimeInFlight()
                )).collect(Collectors.toList());
    }

    public List<EmployeeResponseDTO> viewAllEmployees() {
        return personRepository.findAll().stream()
                .filter(p -> p instanceof Employee)
                .map(p -> (Employee) p)
                .map(e -> {
                    if (e instanceof Pilot pilot) {
                        return new PilotResponseDTO(
                                pilot.getId(),
                                pilot.getEmail(),
                                pilot.getPassword(),
                                pilot.getFirstName(),
                                pilot.getLastName(),
                                pilot.getFlights().stream().map(Flight::getFlightId).toList()
                        );
                    } else if (e instanceof Manager manager) {
                        return new ManagerResponseDTO(
                                manager.getId(),
                                manager.getEmail(),
                                manager.getPassword(),
                                manager.getFirstName(),
                                manager.getLastName(),
                                manager.getFlights().stream().map(Flight::getFlightId).toList(),
                                manager.getBookings().stream().map(Booking::getBookingId).toList()
                        );
                    } else if (e instanceof FlightAttendant attendant) {
                        return new FlightAttendantResponseDTO(
                                attendant.getId(),
                                attendant.getEmail(),
                                attendant.getPassword(),
                                attendant.getFirstName(),
                                attendant.getLastName(),
                                attendant.getFlights().stream().map(Flight::getFlightId).toList()
                        );
                    } else {
                        return new EmployeeResponseDTO(
                                e.getId(),
                                e.getEmail(),
                                e.getPassword(),
                                e.getFirstName(),
                                e.getLastName()
                        );
                    }
                }).collect(Collectors.toList());
    }

    public List<FlightResponseDTO> viewAllFlights() {
        return flightRepository.findAll().stream()
                .map(f -> new FlightResponseDTO(
                        f.getFlightId(),
                        f.getCapacity(),
                        f.getSeatsRemaining(),
                        f.getDepartTime(),
                        f.getArrivalTime(),
                        f.getExpectedDepartTime(),
                        f.getDepartLocation(),
                        f.getArrivalLocation(),
                        f.getFlightNumber(),
                        f.getFlightTime(),
                        f.isRecurring(),
                        f.isActive()
                )).collect(Collectors.toList());
    }

    public List<BookingResponseDTO> viewAllBookings() {
        return bookingRepository.findAll().stream()
                .map(b -> new BookingResponseDTO(
                        b.getBookingId(),
                        b.getCustomer() != null ? b.getCustomer().getId() : null,
                        b.getSeat() != null ? b.getSeat().getSeatId() : null,
                        b.getBookingDate(),
                        b.getPaymentStatus(),
                        b.getBookingStatus()
                )).collect(Collectors.toList());
    }

    public List<SeatResponseDTO> viewAllSeats() {
        return seatRepository.findAll().stream()
                .map(s -> new SeatResponseDTO(
                        s.getSeatId(),
                        s.getFlight() != null ? s.getFlight().getFlightId() : null,
                        s.getSeatClass(),
                        s.getPrice(),
                        s.getSeatNumber(),
                        s.getSeatStatus()
                )).collect(Collectors.toList());
    }
    public double viewSalary(Employee employee) {
        return employee != null ? employee.getSalary() : 0.0;
    }

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