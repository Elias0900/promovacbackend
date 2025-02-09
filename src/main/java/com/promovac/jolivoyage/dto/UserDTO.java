package com.promovac.jolivoyage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String prenom;
    private String nom;
    private String email;
    private String password;
    private String role;
    private Long agenceId;
}
