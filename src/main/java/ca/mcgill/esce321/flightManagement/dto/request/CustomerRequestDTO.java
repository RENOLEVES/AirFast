package ca.mcgill.esce321.flightManagement.Dto.request;

public class CustomerRequestDTO extends PersonRequestDTO{
    private int membershipNumber;
    private int points;
    private int timeInFlight;      // total hours or minutes spent flying

    public CustomerRequestDTO() {}

    public CustomerRequestDTO(String email,String password, String firstName, String lastName,
                              int membershipNumber, int points, int timeInFlight) {
        super(email,password,firstName,lastName);
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
}
