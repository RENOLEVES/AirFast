package ca.mcgill.esce321.flightManagement.dto;

import java.util.List;

public class PilotDTO extends EmployeeDTO {

    private List<Long> flightIds;  // store only IDs to avoid circular references

    public PilotDTO() {}

    public PilotDTO(Long eId, String email,String password, String firstName, String lastName, List<Long> flightIds) {
        super(eId, email,password, firstName, lastName);
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
