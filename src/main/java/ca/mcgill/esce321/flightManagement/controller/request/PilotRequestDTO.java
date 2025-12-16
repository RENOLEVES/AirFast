package ca.mcgill.esce321.flightManagement.controller.request;

import java.util.List;

public class PilotRequestDTO extends EmployeeRequestDTO {

    private List<Long> flightIds;

    public PilotRequestDTO() {}

    public PilotRequestDTO(String email, String password, String firstName, String lastName, List<Long> flightIds) {
        super(email,password, firstName, lastName, "pilot");
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
