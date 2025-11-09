package ca.mcgill.esce321.flightManagement.dto.request;

import ca.mcgill.esce321.flightManagement.dto.request.EmployeeRequestDTO;

import java.util.List;

import ca.mcgill.esce321.flightManagement.dto.request.EmployeeRequestDTO;

public class PilotRequestDTO extends EmployeeRequestDTO {

    private List<Long> flightIds;

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
