package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
