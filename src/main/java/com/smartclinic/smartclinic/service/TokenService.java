package com.smartclinic.smartclinic.service;

import com.smartclinic.smartclinic.model.Role;

public interface TokenService {
    String generateToken(String email, Role role);
    boolean validateToken(String token);
    String extractEmail(String token);
}
