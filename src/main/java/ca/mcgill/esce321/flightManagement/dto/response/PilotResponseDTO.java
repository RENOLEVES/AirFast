package ca.mcgill.esce321.flightManagement.dto.response;

import java.util.ArrayList;
import java.util.List;

public class PilotResponseDTO extends EmployeeResponseDTO {

    private List<Long> flightIds = new ArrayList<>();

    public PilotResponseDTO() {}

    public PilotResponseDTO(Long id, String email, String password, String firstName, String lastName, List<Long> flightIds) {
        super(id, email,password, firstName, lastName, "Pilot");
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
