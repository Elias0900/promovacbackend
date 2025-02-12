package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.dto.VentesParJourDto;
import com.promovac.jolivoyage.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VenteRepository extends JpaRepository<Vente, Long>, JpaSpecificationExecutor<Vente> {

    /**
     * Récupère toutes les ventes effectuées par un utilisateur spécifique.
     *
     * @param userId L'ID de l'utilisateur
     * @return Une liste d'objets Vente associés à l'utilisateur
     */
    @Query("SELECT v FROM Vente v WHERE v.user.id = :userId")
    List<Vente> findVentesByUserId(@Param("userId") Long userId);

    /**
     * Récupère toutes les ventes effectuées par un utilisateur spécifique.
     *
     * @param AgenceId L'ID de l'utilisateur
     * @return Une liste d'objets Vente associés à l'utilisateur
     */
    @Query("SELECT v FROM Vente v WHERE v.user.agence.id = :agenceId")
    List<Vente> findVentesByAgenceId(@Param("agenceId") Long AgenceId);


    /**
     * Calcule le montant total des assurances pour un utilisateur spécifique.
     *
     * @param userId L'ID de l'utilisateur
     * @return Le montant total des assurances souscrites par l'utilisateur
     */
    @Query("select sum(v.montantAssurance) from Vente v where v.user.id = :userId")
    Double totalMontantAssuranceByUserId(@Param("userId") Long userId);

    /**
     * Compte le nombre d'assurances souscrites par un utilisateur spécifique.
     *
     * @param userId L'ID de l'utilisateur
     * @return Le nombre d'assurances souscrites par l'utilisateur
     */
    @Query("select count(v) from Vente v where v.user.id = :userId and v.assurance = true")
    Double countAssuranceSouscriteByUserId(@Param("userId") Long userId);

    /**
     * Calcule le montant total des ventes réalisées par un utilisateur spécifique.
     *
     * @param userId L'ID de l'utilisateur
     * @return Le montant total des ventes réalisées par l'utilisateur
     */
    @Query("select sum(v.venteTotal) from Vente v where v.user.id = :userId")
    Double totalMontant(@Param("userId") Long userId);

    /**
     * Calcule le montant total des ventes sans assurance pour un utilisateur spécifique,
     * filtré par un tour opérateur spécifique (contenant le nom du tour opérateur).
     *
     * @param userId L'ID de l'utilisateur
     * @param tourOperateur Le nom du tour opérateur
     * @return Le montant total des ventes sans assurance filtrées par le tour opérateur
     */
    @Query("select sum(v.totalSansAssurance) from Vente v where v.user.id = :userId and v.tourOperateur like %:tourOperateur%")
    Double totalMontantTOByUserIdForFramContaining(@Param("userId") Long userId, @Param("tourOperateur") String tourOperateur);

    /**
     * Calcule le montant total des ventes sans assurance pour un utilisateur spécifique,
     * excluant un tour opérateur spécifique.
     *
     * @param userId L'ID de l'utilisateur
     * @param tourOperateur Le nom du tour opérateur
     * @return Le montant total des ventes sans assurance excluant le tour opérateur spécifié
     */
    @Query("select sum(v.totalSansAssurance) from Vente v where v.user.id = :userId and v.tourOperateur not like %:tourOperateur%")
    Double totalMontantTOByUserIdForNonFram(@Param("userId") Long userId,  @Param("tourOperateur") String tourOperateur);

    /**
     * Récupère les ventes par jour dans une période spécifique.
     * Retourne une liste de ventes agrégées par date avec le nombre de ventes et le total des montants.
     *
     * @param startDate La date de début de la période
     * @param endDate La date de fin de la période
     * @return Une liste de VentesParJourDto avec les données agrégées par date
     */
    @Query("SELECT new com.promovac.jolivoyage.dto.VentesParJourDto(" +
            "    v.dateValidation, " +
            "    COUNT(v), " +
            "    SUM(v.venteTotal)) " +
            "FROM Vente v " +
            "WHERE v.dateValidation >= :startDate " +
            "AND v.dateValidation < :endDate " + // Pour couvrir jusqu'à la fin du dernier jour
            "GROUP BY v.dateValidation " +
            "ORDER BY v.dateValidation")
    List<VentesParJourDto> findVentesParJour(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    /**
     * Récupère les ventes par jour dans une période spécifique pour un utilisateur donné.
     * Retourne une liste de ventes agrégées par date pour un utilisateur spécifique.
     *
     * @param startDate La date de début de la période
     * @param endDate La date de fin de la période
     * @param userId L'ID de l'utilisateur
     * @return Une liste de VentesParJourDto avec les données agrégées par date pour l'utilisateur
     */
    @Query("SELECT new com.promovac.jolivoyage.dto.VentesParJourDto(" +
            "    v.dateValidation, " +
            "    COUNT(v), " +
            "    SUM(v.venteTotal)) " +
            "FROM Vente v " +
            "WHERE v.dateValidation BETWEEN :startDate AND :endDate " +
            "AND v.user.id = :userId " +
            "GROUP BY v.dateValidation " +
            "ORDER BY v.dateValidation")
    List<VentesParJourDto> findVentesParJourByUser(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate,
                                                   @Param("userId") Long userId);

    /**
     * Compte le nombre total de ventes réalisées par un utilisateur spécifique.
     *
     * @param userId L'ID de l'utilisateur
     * @return Le nombre total de ventes réalisées par l'utilisateur
     */
    @Query("select count(v) from Vente v where v.user.id = :userId")
    Double countVentesByUserId(@Param("userId") Long userId);

    /**
     * Récupère les ventes agrégées par jour pour une année donnée.
     * Retourne le nombre de ventes et le montant total des ventes pour chaque jour de l'année.
     *
     * @param year L'année pour laquelle les données sont récupérées
     * @return Une liste de VentesParJourDto avec les données agrégées par jour pour l'année spécifiée
     */
    @Query("SELECT new com.promovac.jolivoyage.dto.VentesParJourDto( " +
            "    v.dateValidation, " +
            "    COUNT(v), " +
            "    SUM(v.venteTotal)) " +
            "FROM Vente v " +
            "WHERE YEAR(v.dateValidation) = :year " +
            "GROUP BY v.dateValidation " +
            "ORDER BY v.dateValidation")
    List<VentesParJourDto> findVentesParJourGraph(@Param("year") int year);

    /**
     * Récupère les ventes agrégées par mois pour une année donnée.
     * Retourne le nombre de ventes et le montant total des ventes pour chaque mois de l'année.
     *
     * @param year L'année pour laquelle les données sont récupérées
     * @return Une liste de VentesParJourDto avec les données agrégées par mois pour l'année spécifiée
     */
    @Query("SELECT new com.promovac.jolivoyage.dto.VentesParJourDto( " +
            "    v.dateValidation, " +
            "    COUNT(v), " +
            "    SUM(v.venteTotal)) " +
            "FROM Vente v " +
            "WHERE YEAR(v.dateValidation) = :year " +
            "GROUP BY FUNCTION('DATE_FORMAT', v.dateValidation, '%Y-%m') " +
            "ORDER BY FUNCTION('DATE_FORMAT', v.dateValidation, '%Y-%m')")
    List<VentesParJourDto> findVentesParMoisGraph(@Param("year") int year);

    /**
     * Récupère les ventes agrégées par année.
     * Retourne le nombre de ventes et le montant total des ventes pour chaque année.
     *
     * @return Une liste de VentesParJourDto avec les données agrégées par année
     */
    @Query("SELECT new com.promovac.jolivoyage.dto.VentesParJourDto( " +
            "    v.dateValidation, " +
            "    COUNT(v), " +
            "    SUM(v.venteTotal)) " +
            "FROM Vente v " +
            "GROUP BY FUNCTION('YEAR', v.dateValidation) " +
            "ORDER BY FUNCTION('YEAR', v.dateValidation)")
    List<VentesParJourDto> findVentesParAn();

    /**
     * Calcule le montant total des assurances souscrites par tous les utilisateurs.
     *
     * @return Le montant total des assurances
     */
    @Query("select sum(v.montantAssurance) from Vente v")
    Double totalMontantAssurance();

    /**
     * Compte le nombre total d'assurances souscrites par tous les utilisateurs.
     *
     * @return Le nombre total d'assurances souscrites
     */
    @Query("select count(v) from Vente v where v.assurance = true")
    Double countAssuranceSouscrite();

    /**
     * Calcule le montant total des ventes pour tous les utilisateurs.
     *
     * @return Le montant total des ventes
     */
    @Query("select sum(v.venteTotal) from Vente v")
    Double totalMontant();

    /**
     * Calcule le montant total des ventes sans assurance, filtré par un tour opérateur spécifique.
     *
     * @param tourOperateur Le nom du tour opérateur
     * @return Le montant total des ventes sans assurance filtrées par le tour opérateur
     */
    @Query("select sum(v.totalSansAssurance) from Vente v where v.tourOperateur like %:tourOperateur%")
    Double totalMontantTOForFramContaining(@Param("tourOperateur") String tourOperateur);

    /**
     * Calcule le montant total des ventes sans assurance, excluant un tour opérateur spécifique.
     *
     * @param tourOperateur Le nom du tour opérateur
     * @return Le montant total des ventes sans assurance excluant le tour opérateur spécifié
     */
    @Query("select sum(v.totalSansAssurance) from Vente v where v.tourOperateur not like  %:tourOperateur%")
    Double totalMontantTOForNonFram(@Param("tourOperateur") String tourOperateur);

    @Query("SELECT v FROM Vente v WHERE YEAR(v.dateValidation) = YEAR(:lastMonth) " +
            "AND MONTH(v.dateValidation) = MONTH(:lastMonth) " +
            "AND v.user.id = :userId")
    List<Vente> findVentesDuMoisPrecedentByUser(@Param("lastMonth") LocalDate lastMonth,
                                                @Param("userId") Long userId);

//    @Query("SELECT v FROM Vente v WHERE YEAR(v.dateValidation) = YEAR(:lastMonth) " +
//            "AND MONTH(v.dateValidation) = MONTH(:lastMonth) " +
//            "AND v.user.agenceId = :agenceId")
//    List<Vente> findVentesDuMoisPrecedentPourAgence(@Param("lastMonth") LocalDate lastMonth,
//                                                  @Param("agenceId") Long agenceId);

}
