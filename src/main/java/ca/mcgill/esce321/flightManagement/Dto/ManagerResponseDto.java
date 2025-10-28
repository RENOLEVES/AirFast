
package ca.mcgill.esce321.flightManagement.dto;

import java.time.LocalDate;

import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.model.Flight;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


//add flights list -> ResponseDto
//update flight assign employee, all functions -> ManagerService
public class ManagerResponseDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Flight> flights;

    // private LocalDate creationDate;

    @SuppressWarnings("unused")
    private ManagerResponseDto() {
    }

    // do I need a new parameter to this Constructor, how do I get flights
    // is the parameter Manager model or Person model
    public ManagerResponseDto(Manager model) {
        this.id = model.getId();
        this.firstName = model.getFirstName();
        this.lastName = model.getLastName();
        this.email = model.getEmail();
        this.flights = model.getFlights();
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
      
    public List<Flight> getFlights() {
        return flights;
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

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}