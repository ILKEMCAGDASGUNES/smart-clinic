package com.smartclinic.smartclinic.controller;

import com.smartclinic.smartclinic.model.mysql.Doctor;
import com.smartclinic.smartclinic.service.DoctorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // 1️⃣ Doktor oluştur (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    // 2️⃣ Tüm doktorları getir
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // 3️⃣ ID ile doktor getir
    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    // 4️⃣ Uzmanlığa göre doktor arama
    @GetMapping("/search")
    public List<Doctor> getDoctorsBySpecialization(
            @RequestParam String specialization) {
        return doctorService.getDoctorsBySpecialization(specialization);
    }

    // 5️⃣ Doktor sil (Admin)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}
