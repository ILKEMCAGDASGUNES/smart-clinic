package com.smartclinic.smartclinic.controller;

import com.smartclinic.smartclinic.model.mongo.Prescription;
import com.smartclinic.smartclinic.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    @PostMapping
    public Prescription createPrescription(@Valid @RequestBody Prescription prescription) {
        return prescriptionService.createPrescription(prescription);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    @GetMapping("/patient/{patientId}")
    public List<Prescription> getPrescriptionsByPatient(@PathVariable Long patientId) {
        return prescriptionService.getByPatient(patientId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @GetMapping("/doctor/{doctorId}")
    public List<Prescription> getPrescriptionsByDoctor(@PathVariable Long doctorId) {
        return prescriptionService.getByDoctor(doctorId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT')")
    @GetMapping("/appointment/{appointmentId}")
    public List<Prescription> getPrescriptionsByAppointment(@PathVariable Long appointmentId) {
        return prescriptionService.getByAppointment(appointmentId);
    }
}
