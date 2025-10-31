package ca.mcgill.esce321.flightManagement.dto.response;

import java.util.List;

public class PilotResponseDTO extends EmployeeResponseDTO {

    private List<Long> flightIds;  // store only IDs to avoid circular references

    public PilotResponseDTO() {}

    public PilotResponseDTO(String email, String password, String firstName, String lastName, List<Long> flightIds) {
        super(email,password, firstName, lastName);
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
