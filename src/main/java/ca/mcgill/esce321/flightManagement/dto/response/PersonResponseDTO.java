package ca.mcgill.esce321.flightManagement.Dto.response;

public class PersonResponseDTO {

    private Long id;
    private String email;
    // private  String password;
    private String firstName;
    private String lastName;

    public PersonResponseDTO() {}

    public PersonResponseDTO(String email, String firstName, String lastName) {
        this.email = email;
        // this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // public String getPassword() {
    //     return password;
    // }

    // public void setPassword(String password) {
    //     this.password = password;
    // }
}

