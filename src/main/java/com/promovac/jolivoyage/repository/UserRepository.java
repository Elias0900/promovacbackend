package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son adresse email.
     * @param email L'adresse email de l'utilisateur.
     * @return Un utilisateur si trouvé, sinon un Optional vide.
     */
    User findByEmail(String email);

    List<User> findByAgenceId(Long agenceId);

}
