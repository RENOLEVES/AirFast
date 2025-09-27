package ca.mcgill.esce321.flightManagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "members")
public class Member extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int points;

    private String membershipLevel;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}