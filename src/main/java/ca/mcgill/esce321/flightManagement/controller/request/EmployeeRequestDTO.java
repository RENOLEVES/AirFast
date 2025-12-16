package ca.mcgill.esce321.flightManagement.controller.request;

public class EmployeeRequestDTO extends PersonRequestDTO {

    private boolean isActive;
    private String title;

    public EmployeeRequestDTO(){}
    public EmployeeRequestDTO(String email, String password, String firstName, String lastName, String title) {
        super(email,password,firstName,lastName);
        this.isActive = true;
        this.title = title;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
