package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Employee extends Person{

    public Employee(){}
    public Employee(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
    }

}
