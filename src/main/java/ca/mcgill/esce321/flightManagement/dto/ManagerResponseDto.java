package ca.mcgill.esce321.flightManagement.dto;

import java.time.LocalDate;

import ca.mcgill.ecse321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;


public class ManagerResponseDto {
    private int id;
    private String name;
    private String email;
    private LocalDate creationDate;

    // Jackson needs a default constructor, but it doesn't need to be public
    @SuppressWarnings("unused")
    private PersonResponseDto() {
    }

    public ManagerResponseDto(Person model) {
        this.id = model.getId();
        this.name = model.getName();
        this.email = model.getEmail();
        this.creationDate = model.getCreationDate().toLocalDate();
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
