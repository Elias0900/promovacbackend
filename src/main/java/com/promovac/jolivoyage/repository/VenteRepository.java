package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.dto.VentesParJourDto;
import com.promovac.jolivoyage.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VenteRepository extends JpaRepository<Vente, Long> {

    @Query("SELECT v FROM Vente v WHERE v.user.id = :userId")
    List<Vente> findVentesByUserId(@Param("userId") Long userId);


    @Query("select sum(v.montantAssurance) from Vente v where v.user.id = :userId")
    Double totalMontantAssuranceByUserId(@Param("userId") Long userId);

    @Query("select sum(v.venteTotal) from Vente v where v.user.id = :userId")
    Double totalMontant(@Param("userId") Long userId);

    @Query("select sum(v.totalSansAssurance) from Vente v where v.user.id = :userId and v.tourOperateur like %:tourOperateur%")
    Double totalMontantTOByUserIdForFramContaining(@Param("userId") Long userId, @Param("tourOperateur") String tourOperateur);


    @Query("select sum(v.totalSansAssurance) from Vente v where v.user.id = :userId and v.tourOperateur not like  %:tourOperateur%")
    Double totalMontantTOByUserIdForNonFram(@Param("userId") Long userId,  @Param("tourOperateur") String tourOperateur);

//    @Query("select v.dateValidation, sum(v.montantVenteTotale) from Vente v where v.user.id = :userId group by v.dateValidation order by v.dateValidation")
//    List<Object[]> findTotalVentesByDateForUser(@Param("userId") Long userId);


    @Query("SELECT new com.promovac.jolivoyage.dto.VentesParJourDto(" +
            "    v.dateValidation, " +
            "    COUNT(v), " +
            "    SUM(v.venteTotal)) " +
            "FROM Vente v " +
            "WHERE v.dateValidation BETWEEN :startDate AND :endDate " +
            "GROUP BY v.dateValidation " +
            "ORDER BY v.dateValidation")
    List<VentesParJourDto> findVentesParJour(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

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




}
