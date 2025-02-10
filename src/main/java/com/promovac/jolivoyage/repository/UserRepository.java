package com.promovac.jolivoyage.repository;

import com.promovac.jolivoyage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);
    List<User> findByAgenceId(Long agenceId);

}
