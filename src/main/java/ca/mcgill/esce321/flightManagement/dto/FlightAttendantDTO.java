package ca.mcgill.esce321.flightManagement.dto;

import java.util.List;

public class FlightAttendantDTO extends EmployeeDTO {

    private List<Long> flightIds;

    public FlightAttendantDTO() {}

    public FlightAttendantDTO(Long eId, String email, String password, String firstName, String lastName,
                              List<Long> flightIds) {
        super(eId, email,password, firstName, lastName);
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
