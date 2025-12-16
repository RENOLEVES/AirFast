package ca.mcgill.esce321.flightManagement.dto.response;

import java.util.ArrayList;
import java.util.List;

public class FlightAttendantResponse extends EmployeeResponse {

    private List<Long> flightIds = new ArrayList<>();

    public FlightAttendantResponse() {}
    public FlightAttendantResponse(Long id, String email, String firstName, String lastName) {
        super(id, email,firstName,lastName,"Flight Attendant");
    }

    public FlightAttendantResponse(Long id, String email, String firstName, String lastName,
                                   List<Long> flightIds) {
        super(id, email, firstName, lastName, "Flight Attendant");
        this.setActive(true);
        this.flightIds = flightIds;
    }

    public List<Long> getFlightIds() {
        return flightIds;
    }

    public void setFlightIds(List<Long> flightIds) {
        this.flightIds = flightIds;
    }
}
