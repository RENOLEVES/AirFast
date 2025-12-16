package ca.mcgill.esce321.flightManagement.controller.request;

public class OwnerRequestDTO extends PersonRequestDTO{

    public OwnerRequestDTO() {}

    public OwnerRequestDTO(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
    }
}
