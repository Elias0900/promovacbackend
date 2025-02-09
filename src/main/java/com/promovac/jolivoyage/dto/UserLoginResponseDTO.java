package com.promovac.jolivoyage.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDTO {
    private String token;
    private String type = "Bearer";
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String role;
    private Long id;
    private Long agenceId;
    private Double objectif;

    // Constructeur
    public UserLoginResponseDTO(String token, String nom, String prenom, String email, String role, Long id, Long agenceId, Double objectif) {
        this.token = token;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.id = id;
        this.agenceId = agenceId;
        this.objectif = objectif;
    }
}
