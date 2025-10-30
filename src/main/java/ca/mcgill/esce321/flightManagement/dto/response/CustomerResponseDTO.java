package ca.mcgill.esce321.flightManagement.dto.response;

public class CustomerResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    private int membershipNumber;
    private int points;
    private int timeInFlight;      // total hours or minutes spent flying

    private int totalBookings;     // derived field

    public CustomerResponseDTO() {}

    public CustomerResponseDTO(Long id, String email, String firstName, String lastName,
                               int membershipNumber, int points, int timeInFlight, int totalBookings) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.membershipNumber = membershipNumber;
        this.points = points;
        this.timeInFlight = timeInFlight;
        this.totalBookings = totalBookings;
    }

    // Getters & Setters

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

    public int getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(int membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTimeInFlight() {
        return timeInFlight;
    }

    public void setTimeInFlight(int timeInFlight) {
        this.timeInFlight = timeInFlight;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }
}
