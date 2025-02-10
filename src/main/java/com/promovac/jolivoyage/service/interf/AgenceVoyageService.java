package com.promovac.jolivoyage.service.interf;

import com.promovac.jolivoyage.entity.User;

import java.util.List;

public interface AgenceVoyageService {
    List<User> getUsersByAgenceId(Long agenceId);

}
