package ca.mcgill.esce321.flightManagement.Dto;


// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;

public class ManagerRequestDto {

    // @NotBlank(message = "Name cannot be blank")
    private String firstName;
    private String lastName; 

    // @Email(message = "Invalid email format")
    private String email;

    // Default constructor (needed by Jackson)
    @SuppressWarnings("unused")
    private ManagerRequestDto() {
    }

    public ManagerRequestDto(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setLastName(String name) {
        this.lastName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
