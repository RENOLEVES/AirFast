package ca.mcgill.esce321.flightManagement.dto.response;

public class OwnerResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    private double totalRevenue;
    private int totalCustomers;
    private int totalEmployees;
    private int totalFlights;
    private int totalBookings;
    private int totalSeats;

    public OwnerResponseDTO() {}

    public OwnerResponseDTO(Long id, String email, String firstName, String lastName, double totalRevenue,
                            int totalCustomers, int totalEmployees, int totalFlights, int totalBookings, int totalSeats) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalRevenue = totalRevenue;
        this.totalCustomers = totalCustomers;
        this.totalEmployees = totalEmployees;
        this.totalFlights = totalFlights;
        this.totalBookings = totalBookings;
        this.totalSeats = totalSeats;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

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
