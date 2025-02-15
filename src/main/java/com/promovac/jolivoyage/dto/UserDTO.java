package com.promovac.jolivoyage.dto;

import com.promovac.jolivoyage.entity.AgenceVoyage;
import com.promovac.jolivoyage.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String password;
    private String role;
    private Long agenceId;

    /**
     * Convertit une entité `User` en un DTO `UserDTO`.
     *
     * @param user L'entité source.
     * @return Un objet `UserDTO`.
     */
    public static UserDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .prenom(user.getPrenom())
                .nom(user.getNom())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .agenceId(user.getAgence() != null ? user.getAgence().getId() : null)
                .build();
    }

    /**
     * Convertit un DTO `UserDTO` en une entité `User`.
     *
     * @param userDTO Le DTO source.
     * @return Un objet `User`.
     */
    public static User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setPrenom(userDTO.getPrenom());
        user.setNom(userDTO.getNom());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());

        if (userDTO.getAgenceId() != null) {
            user.setAgence(new AgenceVoyage());  // Créer une instance d'Agence avec uniquement l'ID
            user.getAgence().setId(userDTO.getAgenceId());
        }

        return user;
    }
}
