package ca.mcgill.esce321.flightManagement.dto.response;

public class OwnerResponseDTO extends PersonResponseDTO{
    private double totalRevenue;
    private int totalCustomers;
    private int totalEmployees;
    private int totalFlights;
    private int totalBookings;
    private int totalSeats;

    public OwnerResponseDTO() {}

    public OwnerResponseDTO(String email,String password, String firstName, String lastName, double totalRevenue,
                            int totalCustomers, int totalEmployees, int totalFlights, int totalBookings, int totalSeats) {

        super(email,password,firstName,lastName);
        this.totalRevenue = totalRevenue;
        this.totalCustomers = totalCustomers;
        this.totalEmployees = totalEmployees;
        this.totalFlights = totalFlights;
        this.totalBookings = totalBookings;
        this.totalSeats = totalSeats;
    }

    // Getters and Setters
    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public int getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(int totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public int getTotalFlights() {
        return totalFlights;
    }

    public void setTotalFlights(int totalFlights) {
        this.totalFlights = totalFlights;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}
