package ca.mcgill.esce321.flightManagement.Dto.request;

// import ca.mcgill.esce321.flightManagement.Dto.response.EmployeeResponseDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.EmployeeRequestDTO;


import java.util.List;

public class FlightAttendantRequestDTO extends EmployeeRequestDTO {

    private List<Long> flightIds;

    public FlightAttendantRequestDTO() {}

    public FlightAttendantRequestDTO(String email, String password, String firstName, String lastName,
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
