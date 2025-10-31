package ca.mcgill.esce321.flightManagement.dto;


// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;

public class ManagerRequestDto {

    // @NotBlank(message = "Name cannot be blank")
    private String firstName;
    private String lastName; 
    private String password;

    // @Email(message = "Invalid email format")
    private String email;

    //maybe flights list

    // Default constructor (needed by Jackson)
    @SuppressWarnings("unused")
    private ManagerRequestDto() {
    }

    public ManagerRequestDto(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        // this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
