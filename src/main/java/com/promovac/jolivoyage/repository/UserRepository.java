package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
}
