package com.smartclinic.smartclinic.controller;

import com.smartclinic.smartclinic.model.mysql.Patient;
import com.smartclinic.smartclinic.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // 1️⃣ Yeni hasta oluştur
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    // 2️⃣ Tüm hastaları getir
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    // 3️⃣ ID ile hasta getir
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // 4️⃣ Hasta sil
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatientById(id);
    }
}
