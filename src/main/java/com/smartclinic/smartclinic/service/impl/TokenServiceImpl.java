package com.smartclinic.smartclinic.service.impl;

import com.smartclinic.smartclinic.model.Role;
import com.smartclinic.smartclinic.security.JwtUtil;
import com.smartclinic.smartclinic.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;

    public TokenServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String generateToken(String email, Role role) {
        return jwtUtil.generateToken(email, role.name());
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    @Override
    public String extractEmail(String token) {
        return jwtUtil.extractUsername(token);
    }
}
