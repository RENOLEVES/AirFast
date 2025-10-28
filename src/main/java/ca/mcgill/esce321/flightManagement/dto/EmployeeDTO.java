package ca.mcgill.esce321.flightManagement.dto;

import ca.mcgill.esce321.flightManagement.model.Owner;

public class EmployeeDTO extends PersonDTO{
    private Owner owner;
    private Long e_id;
    private boolean isActive;

    public EmployeeDTO(){}
    public EmployeeDTO(Long eId,String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
        e_id = eId;
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
