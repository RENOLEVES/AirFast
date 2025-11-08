package ca.mcgill.esce321.flightManagement.dto.request;

// import ca.mcgill.esce321.flightManagement.Dto.response.PersonResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.request.PersonRequestDTO;
import ca.mcgill.esce321.flightManagement.model.Owner;

public class EmployeeRequestDTO extends PersonRequestDTO {

    private boolean isActive;

    public EmployeeRequestDTO(){}
    public EmployeeRequestDTO(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
        this.isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
