package ca.mcgill.esce321.flightManagement.dto.response;

import java.util.ArrayList;
import java.util.List;

public class OwnerResponseDTO extends PersonResponseDTO{
    private double totalRevenue = -1d;
    private List<Long> customerIds = new ArrayList<>();
    private List<Long> employeeIds= new ArrayList<>();;
    private List<Long> flightIds= new ArrayList<>();;
    private List<Long> bookingIds= new ArrayList<>();;
    private List<Long> seatIds= new ArrayList<>();;

    public OwnerResponseDTO() {}

    public OwnerResponseDTO(Long id, String email,String password, String firstName, String lastName) {
        super(id, email,password,firstName,lastName);
    }

    public OwnerResponseDTO(Long id, String email, String password, String firstName, String lastName,
                            double totalRevenue, List<Long> customerIds, List<Long> employeeIds,
                            List<Long> flightIds, List<Long> bookingIds, List<Long> seatIds) {
        super(id, email, password, firstName, lastName);
        this.totalRevenue = totalRevenue;
        this.customerIds = customerIds;
        this.employeeIds = employeeIds;
        this.flightIds = flightIds;
        this.bookingIds = bookingIds;
        this.seatIds = seatIds;
    }

    // Getters and Setters
    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public List<Long> getFlightIds() {
        return flightIds;
    }

    public void setFlightIds(List<Long> flightIds) {
        this.flightIds = flightIds;
    }

    public List<Long> getBookingIds() {
        return bookingIds;
    }

    public void setBookingIds(List<Long> bookingIds) {
        this.bookingIds = bookingIds;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }
}
