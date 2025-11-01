package ca.mcgill.esce321.flightManagement.Dto.response;

import ca.mcgill.esce321.flightManagement.model.Owner;

public class EmployeeResponseDTO extends PersonResponseDTO {
    private Owner owner;
    private Long e_id;
    private boolean isActive;

    public EmployeeResponseDTO(){}
    public EmployeeResponseDTO(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
