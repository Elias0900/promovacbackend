package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.entity.Bilan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BilanRepository extends JpaRepository<Bilan, Long> {


    @Query("SELECT b FROM Bilan b WHERE b.user.id = :userId")
    Optional<Bilan> findByUserId(Long userId);

}
