package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.entity.AgenceVoyage;
import com.promovac.jolivoyage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgenceVoyageRepository extends JpaRepository<AgenceVoyage, Long> {

}
