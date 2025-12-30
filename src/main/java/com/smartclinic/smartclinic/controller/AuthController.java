package com.smartclinic.smartclinic.controller;

import com.smartclinic.smartclinic.dto.AuthResponse;
import com.smartclinic.smartclinic.dto.LoginRequest;
import com.smartclinic.smartclinic.dto.RegisterRequest;
import com.smartclinic.smartclinic.model.Role;
import com.smartclinic.smartclinic.model.mysql.User;
import com.smartclinic.smartclinic.repository.mysql.UserRepository;
import com.smartclinic.smartclinic.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody RegisterRequest request) {

        // Basit uniqueness kontrolü (istersen bunu service katmanına taşırız)
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        Role role = parseRoleOrThrow(request.getRole());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = tokenService.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token);
    }

    private Role parseRoleOrThrow(String role) {
        if (role == null || role.isBlank()) {
            throw new RuntimeException("Role is required");
        }
        try {
            return Role.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }
}
