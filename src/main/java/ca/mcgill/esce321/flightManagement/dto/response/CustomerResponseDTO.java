package ca.mcgill.esce321.flightManagement.Dto.response;

public class CustomerResponseDTO extends PersonResponseDTO{
    private int membershipNumber;
    private int points;
    private int timeInFlight;      // total hours or minutes spent flying

    private int totalBookings;     // derived field

    public CustomerResponseDTO() {}

    public CustomerResponseDTO(Long id, String email,String password, String firstName, String lastName,
                               int membershipNumber, int points, int timeInFlight) {

        super(id, email,password,firstName,lastName);
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
