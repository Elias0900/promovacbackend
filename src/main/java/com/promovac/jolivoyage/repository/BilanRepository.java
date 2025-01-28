package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.entity.Bilan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BilanRepository extends JpaRepository<Bilan, Long> {
    Optional<Bilan> findByUserId(Long userId);

}
