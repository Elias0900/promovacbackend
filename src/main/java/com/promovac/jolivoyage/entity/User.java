package com.promovac.jolivoyage.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String role;
    private String token;

    @ManyToOne
    @JoinColumn(name = "agence_id", nullable = false)
    private AgenceVoyage agence;
}
