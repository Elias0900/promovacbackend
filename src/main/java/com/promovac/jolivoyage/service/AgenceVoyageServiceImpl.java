package com.promovac.jolivoyage.service;

import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.repository.UserRepository;
import com.promovac.jolivoyage.service.interf.AgenceVoyageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceVoyageServiceImpl implements AgenceVoyageService {

    private final UserRepository userRepository;

    @Override
    public List<User> getUsersByAgenceId(Long agenceId) {
        return userRepository.findByAgenceId(agenceId);
    }
}
