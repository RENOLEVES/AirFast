package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
public class Owner extends Person{

    private double totalRevenue;

    public Owner(){}
    public Owner(String email, String password, String firstName, String lastName) {
        super(email,password,firstName,lastName);
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}