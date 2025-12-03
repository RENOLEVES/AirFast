package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Employee extends Person{

    private Long e_id;
    private double salary;
    private boolean isActive;
    private String title;

    public Employee(){}
    public Employee(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
        this.isActive = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getE_id() {
        return e_id;
    }

    public void setE_id(Long e_id) {
        this.e_id = e_id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
