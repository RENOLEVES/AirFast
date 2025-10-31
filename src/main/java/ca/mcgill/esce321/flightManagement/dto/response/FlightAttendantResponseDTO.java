package ca.mcgill.esce321.flightManagement.dto.response;

import java.util.List;

public class FlightAttendantResponseDTO extends EmployeeResponseDTO {

    private List<Long> flightIds;

    public FlightAttendantResponseDTO() {}

    public FlightAttendantResponseDTO(String email, String password, String firstName, String lastName,
                                      List<Long> flightIds) {
        super(email,password, firstName, lastName);
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
