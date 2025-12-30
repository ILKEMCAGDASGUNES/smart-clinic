package com.smartclinic.smartclinic.controller;

import com.smartclinic.smartclinic.model.mysql.Appointment;
import com.smartclinic.smartclinic.model.mysql.Doctor;
import com.smartclinic.smartclinic.model.mysql.Patient;
import com.smartclinic.smartclinic.service.AppointmentService;
import com.smartclinic.smartclinic.service.DoctorService;
import com.smartclinic.smartclinic.service.PatientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentController(AppointmentService appointmentService,
                                 DoctorService doctorService,
                                 PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // 1️⃣ Randevu oluştur
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.createAppointment(appointment);
    }

    // 2️⃣ Doktorun randevuları
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return appointmentService.getAppointmentsForDoctor(doctor);
    }

    // 3️⃣ Hastanın randevuları
    @GetMapping("/patient/{patientId}")
    public List<Appointment> getAppointmentsByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return appointmentService.getAppointmentsForPatient(patient);
    }

    // 4️⃣ Randevu sil
    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.delete(id);
    }
}
