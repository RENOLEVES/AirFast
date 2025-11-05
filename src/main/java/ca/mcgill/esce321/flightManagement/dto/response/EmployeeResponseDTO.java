package ca.mcgill.esce321.flightManagement.Dto.response;

import ca.mcgill.esce321.flightManagement.model.Owner;
import ca.mcgill.esce321.flightManagement.Dto.response.PersonResponseDTO;

public class EmployeeResponseDTO extends PersonResponseDTO {
    private Long e_id;
    private boolean isActive;

    public EmployeeResponseDTO(){}
    public EmployeeResponseDTO(Long id, String email, String password, String firstName, String lastName) {
        super(id, email,password,firstName,lastName);
        this.isActive = true;
    }

    public Long getE_id() {
        return e_id;
    }

    public void setE_id(Long e_id) {
        this.e_id = e_id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
