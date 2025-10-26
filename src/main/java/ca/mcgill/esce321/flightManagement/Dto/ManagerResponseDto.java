
package ca.mcgill.esce321.flightManagement.Dto;

import java.time.LocalDate;

import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;


public class ManagerResponseDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    // private LocalDate creationDate;


    // Jackson needs a default constructor, but it doesn't need to be public
    @SuppressWarnings("unused")
    private ManagerResponseDto() {
    }

    public ManagerResponseDto(Person model) {
        this.id = model.getId();
        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.email = model.getEmail();
        // this.creationDate = model.getCreationDate().toLocalDate();
    }

    // public LocalDate getCreationDate() {
    //     return creationDate;
    // }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

     public String getLastName() {
        return lastName;
    }

    // public void setCreationDate(LocalDate creationDate) {
    //     this.creationDate = creationDate;
    // }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}