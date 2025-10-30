package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.Employee;

import java.util.List;

public interface OwnerService {

    List<CustomerResponseDTO> viewAllCustomers();

    List<EmployeeResponseDTO> viewAllEmployees(); // returns base or subclass DTOs

    List<FlightResponseDTO> viewAllFlights();

    List<BookingResponseDTO> viewAllBookings();

    List<SeatResponseDTO> viewAllSeats();

    double viewSalary(Employee employee);

    double calculateTotalRevenue();
}
