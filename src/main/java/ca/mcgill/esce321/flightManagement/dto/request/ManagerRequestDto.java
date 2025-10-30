package ca.mcgill.esce321.flightManagement.dto.request;

import ca.mcgill.esce321.flightManagement.dto.response.EmployeeResponseDTO;

import java.util.List;

public class ManagerRequestDTO extends EmployeeResponseDTO {

    private List<Long> flightIds;

    public ManagerRequestDTO() {}

    public ManagerRequestDTO(Long eId, String email, String password, String firstName, String lastName,
                             List<Long> flightIds) {
        super(eId, email, password, firstName, lastName);
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
