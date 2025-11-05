package ca.mcgill.esce321.flightManagement.Dto.request;

import ca.mcgill.esce321.flightManagement.Dto.response.EmployeeResponseDTO;

import java.util.List;

public class ManagerRequestDTO extends EmployeeRequestDTO {

    private List<Long> flightIds;

    private List<Long> bookingIds;

    public ManagerRequestDTO() {}

    public ManagerRequestDTO(String email, String password, String firstName, String lastName,
                             List<Long> flightIds, List<Long> bookingIds) {
        super(email, password, firstName, lastName);
        this.setActive(true);
        this.flightIds = flightIds;
        this.bookingIds = bookingIds;
    }

    public List<Long> getFlightIds() {
        return flightIds;
    }

    public void setFlightIds(List<Long> flightIds) {
        this.flightIds = flightIds;
    }

    public List<Long> getBookingIds() {
        return bookingIds;
    }

    public void setBookingIds(List<Long> bookingIds) {
        this.bookingIds = bookingIds;
    }
}
