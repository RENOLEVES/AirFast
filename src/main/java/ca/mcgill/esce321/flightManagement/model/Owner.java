package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
public class Owner extends Person{
    public Owner(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
    }

}