package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Employee extends Person{

    public Employee(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
    }

}
