package ca.mcgill.esce321.flightManagement.dto.request;

import ca.mcgill.esce321.flightManagement.dto.request.EmployeeRequestDTO;


import java.util.List;

import ca.mcgill.esce321.flightManagement.dto.request.EmployeeRequestDTO;

public class FlightAttendantRequestDTO extends EmployeeRequestDTO {

    private List<Long> flightIds;

    public FlightAttendantRequestDTO() {}

    public FlightAttendantRequestDTO(String email, String password, String firstName, String lastName,
                                     List<Long> flightIds) {
        super(email,password, firstName, lastName, "flight attendant");
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
