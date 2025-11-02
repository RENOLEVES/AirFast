package ca.mcgill.esce321.flightManagement.dto.response;

import java.util.ArrayList;
import java.util.List;

public class ManagerResponseDTO extends EmployeeResponseDTO {

    private List<Long> flightIds = new ArrayList<>();

    private List<Long> bookingIds = new ArrayList<>();

    public ManagerResponseDTO() {}

    public ManagerResponseDTO(Long id, String email, String password, String firstName, String lastName) {
        super(id, email, password, firstName, lastName);
        this.setActive(true);
    }

    public ManagerResponseDTO(Long id, String email, String password, String firstName, String lastName,
                              List<Long> flightIds, List<Long> bookingIds) {
        super(id, email, password, firstName, lastName);
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
