package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.dto.*;
import ca.mcgill.esce321.flightManagement.model.Employee;

import java.util.List;

public interface OwnerService {

    List<CustomerDTO> viewAllCustomers();

    List<EmployeeDTO> viewAllEmployees(); // returns base or subclass DTOs

    List<FlightDTO> viewAllFlights();

    List<BookingDTO> viewAllBookings();

    List<SeatDTO> viewAllSeats();

    double viewSalary(Employee employee);

    double calculateTotalRevenue();
}
