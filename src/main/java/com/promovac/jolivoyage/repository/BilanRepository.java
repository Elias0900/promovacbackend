package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.entity.Bilan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BilanRepository extends JpaRepository<Bilan, Long> {

    /**
     * Recherche un bilan par ID utilisateur.
     * @param userId L'ID de l'utilisateur.
     * @return Un bilan, s'il existe.
     */
    @Query("SELECT b FROM Bilan b WHERE b.user.id = :userId")
    Optional<Bilan> findByUserId(Long userId);

    /**
     * Recherche tous les bilans associés à un ID d'agence.
     * Utilisation d'une jointure implicite via le lien entre l'utilisateur et l'agence.
     * @param agenceId L'ID de l'agence.
     * @return Une liste de bilans associés à l'agence.
     */
    @Query("SELECT b FROM Bilan b WHERE b.user.agence.id = :agenceId")
    List<Bilan> findByAgenceId(@Param("agenceId") Long agenceId);

    // Si tu veux ajouter d'autres méthodes comme par date ou par statut, voici un exemple :
    // @Query("SELECT b FROM Bilan b WHERE b.date >= :startDate AND b.date <= :endDate")
    // List<Bilan> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
