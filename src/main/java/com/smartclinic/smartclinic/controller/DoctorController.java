package com.smartclinic.smartclinic.controller;

import com.smartclinic.smartclinic.security.JwtUtil;
import com.smartclinic.smartclinic.service.DoctorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final JwtUtil jwtUtil;

    public DoctorController(DoctorService doctorService, JwtUtil jwtUtil) {
        this.doctorService = doctorService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * ✅ REQUIRED ENDPOINT (availability)
     * Example: GET /api/doctors/5/availability?date=2026-01-05
     * Header: Authorization: Bearer <token>
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader
    ) {
        // 1) Token kontrolü
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authorizationHeader.substring("Bearer ".length()).trim();

        // 2) Token doğrulama
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        // 3) Token’dan role & username/email oku
        String role = jwtUtil.extractRole(token);
        String email = jwtUtil.extractUsername(token);

        // Role normalize (örn: ROLE_ADMIN -> ADMIN)
        if (role != null && role.startsWith("ROLE_")) {
            role = role.substring("ROLE_".length());
        }

        // 4) Role bazlı erişim kontrolü
        if ("ADMIN".equalsIgnoreCase(role)) {
            // admin her doktoru görebilir
        } else if ("DOCTOR".equalsIgnoreCase(role)) {
            // doktor sadece kendi availability’sini görebilir
            try {
                Long loggedInDoctorId = doctorService.getDoctorIdByEmail(email);
                if (!doctorId.equals(loggedInDoctorId)) {
                    return ResponseEntity.status(403).body("Forbidden: doctor can only access own availability");
                }
            } catch (RuntimeException ex) {
                return ResponseEntity.status(403).body("Forbidden: doctor not found for token user");
            }
        } else if ("PATIENT".equalsIgnoreCase(role)) {
            // patient görüntüleme yapabilir (genelde allowed)
        } else {
            return ResponseEntity.status(403).body("Forbidden: unknown role");
        }

        // 5) Asıl iş: availability
        var availability = doctorService.getAvailability(doctorId, date);

        return ResponseEntity.ok(availability);
    }
}
