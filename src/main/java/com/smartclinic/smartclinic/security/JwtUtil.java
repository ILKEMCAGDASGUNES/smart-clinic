package com.smartclinic.smartclinic.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "smartclinicsecretkeysmartclinicsecretkey";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 10; // 10 saat

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // --------------------------------------------------
    // ✅ TOKEN GENERATION (LOGIN)
    // --------------------------------------------------
    public String generateToken(String email, String role) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // ADMIN / DOCTOR / PATIENT

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --------------------------------------------------
    // ✅ TOKEN VALIDATION
    // --------------------------------------------------
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --------------------------------------------------
    // ✅ USERNAME / EMAIL
    // --------------------------------------------------
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // --------------------------------------------------
    // ✅ ROLE
    // --------------------------------------------------
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // --------------------------------------------------
    // ✅ PUBLIC WRAPPER (extractClaims HATASINI ÇÖZER)
    // --------------------------------------------------
    public Claims extractClaims(String token) {
        return extractAllClaims(token);
    }

    // --------------------------------------------------
    // 🔒 INTERNAL
    // --------------------------------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
