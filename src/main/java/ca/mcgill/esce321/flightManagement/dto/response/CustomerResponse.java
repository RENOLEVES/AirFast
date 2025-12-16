package ca.mcgill.esce321.flightManagement.dto.response;

public class CustomerResponse extends PersonResponse {
    private int membershipNumber;
    private int points;
    private int timeInFlight;      // total hours or minutes spent flying

    private int totalBookings;     // derived field

    public CustomerResponse() {}

    public CustomerResponse(Long id, String email, String firstName, String lastName,
                            int membershipNumber, int points, int timeInFlight) {

        super(id, email,firstName,lastName, "Customer");
        this.membershipNumber = membershipNumber;
        this.points = points;
        this.timeInFlight = timeInFlight;
    }

    // Getters & Setters

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
