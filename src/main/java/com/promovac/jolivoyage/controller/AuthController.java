package com.promovac.jolivoyage.controller;

import com.promovac.jolivoyage.configuration.JwtUtils;
import com.promovac.jolivoyage.dto.UserDTO;
import com.promovac.jolivoyage.dto.UserLoginResponseDTO;
import com.promovac.jolivoyage.entity.AgenceVoyage;
import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.repository.AgenceVoyageRepository;
import com.promovac.jolivoyage.repository.UserRepository;
import com.promovac.jolivoyage.service.BilanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final AgenceVoyageRepository agenceVoyageRepository;


    private final BilanServiceImpl bilanService;

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }

        AgenceVoyage agence = agenceVoyageRepository.findById(userDTO.getAgenceId())
                .orElseThrow(() -> new RuntimeException("Agence non trouvée avec l'ID : " + userDTO.getAgenceId()));

        User user = new User();
        user.setNom(userDTO.getNom());
        user.setPrenom(userDTO.getPrenom());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole()); // Si Role est un Enum
        user.setAgence(agence);

        return ResponseEntity.ok(userRepository.save(user));
    }


    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody UserLoginResponseDTO user) {
        try {
            // Authentifie l'utilisateur avec email et mot de passe
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                // Récupère l'utilisateur depuis la base de données
                User authenticatedUser = userRepository.findByEmail(user.getEmail());
                if (authenticatedUser == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
                }

                // Création du DTO
                UserLoginResponseDTO responseDTO = new UserLoginResponseDTO(
                        jwtUtils.generateToken(authenticatedUser.getEmail()),
                        authenticatedUser.getNom(),
                        authenticatedUser.getPrenom(),
                        authenticatedUser.getEmail(),
                        authenticatedUser.getRole(),
                        authenticatedUser.getId(),
                        authenticatedUser.getAgence().getId(),
                        authenticatedUser.getAgence().getObjectif()
                );

                return ResponseEntity.ok(responseDTO);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


}
