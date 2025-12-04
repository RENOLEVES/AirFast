package ca.mcgill.esce321.flightManagement.dto.response;

import java.util.ArrayList;
import java.util.List;

public class FlightAttendantResponseDTO extends EmployeeResponseDTO {

    private List<Long> flightIds = new ArrayList<>();

    public FlightAttendantResponseDTO() {}
    public FlightAttendantResponseDTO(Long id, String email,String password, String firstName, String lastName) {
        super(id, email,password,firstName,lastName,"Flight Attendant");
    }

    public FlightAttendantResponseDTO(Long id, String email, String password, String firstName, String lastName,
                                      List<Long> flightIds) {
        super(id, email,password, firstName, lastName, "Flight Attendant");
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
