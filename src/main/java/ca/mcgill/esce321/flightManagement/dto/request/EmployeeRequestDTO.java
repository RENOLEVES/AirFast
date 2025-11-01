package ca.mcgill.esce321.flightManagement.dto.request;

import ca.mcgill.esce321.flightManagement.dto.response.PersonResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Owner;

public class EmployeeRequestDTO extends PersonResponseDTO {
    private Owner owner;
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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
