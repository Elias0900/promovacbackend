package com.promovac.jolivoyage.controller;

import com.promovac.jolivoyage.dto.BilanDto;
import com.promovac.jolivoyage.dto.UserDTO;
import com.promovac.jolivoyage.entity.User;
import com.promovac.jolivoyage.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping(value = "bilan/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> getBilanById(@PathVariable Long id) {
        UserDTO userDTO= customUserDetailsService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok().body(customUserDetailsService.getUserById(id));
    }
}
