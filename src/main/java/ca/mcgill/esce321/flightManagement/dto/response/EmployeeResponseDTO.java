package ca.mcgill.esce321.flightManagement.dto.response;

import ca.mcgill.esce321.flightManagement.model.Owner;

public class EmployeeResponseDTO extends PersonResponseDTO {
    private Long e_id;
    private boolean isActive;
    private String title;

    public EmployeeResponseDTO(){}
    public EmployeeResponseDTO(Long id, String email, String password, String firstName, String lastName, String title) {
        super(id, email,password,firstName,lastName);
        this.isActive = true;
        this.title = title;
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
