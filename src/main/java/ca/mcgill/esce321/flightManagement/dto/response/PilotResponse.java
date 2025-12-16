package ca.mcgill.esce321.flightManagement.dto.response;

import java.util.ArrayList;
import java.util.List;

public class PilotResponse extends EmployeeResponse {

    private List<Long> flightIds = new ArrayList<>();

    public PilotResponse() {}
    public PilotResponse(Long id, String email, String firstName, String lastName) {
        super(id, email, firstName, lastName, "Pilot");
        this.setActive(true);
    }
    public PilotResponse(Long id, String email, String firstName, String lastName, List<Long> flightIds) {
        super(id, email, firstName, lastName, "Pilot");
        this.setActive(true);
        this.flightIds = flightIds;
    }

    // Getters and Setters
    public List<Long> getFlightIds() {
        return flightIds;
    }

    public void setFlightIds(List<Long> flightIds) {
        this.flightIds = flightIds;
    }
}
