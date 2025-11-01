package ca.mcgill.esce321.flightManagement.Dto.request;

import ca.mcgill.esce321.flightManagement.Dto.response.EmployeeResponseDTO;

import java.util.List;

public class PilotRequestDTO extends EmployeeResponseDTO {

    private List<Long> flightIds;  // store only IDs to avoid circular references

    public PilotRequestDTO() {}

    public PilotRequestDTO(String email, String password, String firstName, String lastName, List<Long> flightIds) {
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
