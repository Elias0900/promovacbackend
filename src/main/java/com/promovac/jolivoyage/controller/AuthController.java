package com.promovac.jolivoyage.controller;

import com.promovac.jolivoyage.configuration.JwtUtils;
import com.promovac.jolivoyage.entity.User;
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

    private final BilanServiceImpl bilanService;

    @PostMapping(path = "/register",consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = new User();
        savedUser.setPassword(user.getPassword());
        savedUser.setNom(user.getNom());
        savedUser.setPrenom(user.getPrenom());
        savedUser.setEmail(user.getEmail());
        savedUser.setRole(user.getRole());
        return ResponseEntity.ok(userRepository.save(savedUser));
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> login(@RequestBody User user) {
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

                // Crée une réponse avec les données utilisateur
                Map<String, Object> authData = new HashMap<>();
                authData.put("token", jwtUtils.generateToken(authenticatedUser.getEmail()));
                authData.put("type", "Bearer");
                authData.put("nom", authenticatedUser.getNom());
                authData.put("prenom", authenticatedUser.getPrenom());
                authData.put("email", authenticatedUser.getEmail());
                authData.put("role", authenticatedUser.getRole());
                authData.put("id", authenticatedUser.getId());

                return ResponseEntity.ok().body(authData);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}
