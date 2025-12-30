package com.smartclinic.smartclinic.service;

import com.smartclinic.smartclinic.exception.BusinessException;
import com.smartclinic.smartclinic.exception.ResourceNotFoundException;
import com.smartclinic.smartclinic.model.mysql.Appointment;
import com.smartclinic.smartclinic.model.mysql.Doctor;
import com.smartclinic.smartclinic.model.mysql.Patient;
import com.smartclinic.smartclinic.repository.mysql.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    // =======================
    // SECURITY HELPER METHODS
    // =======================

    private String getCurrentUserEmail() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // JWT subject (email)
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    // =======================
    // BUSINESS METHODS
    // =======================

    // Yeni randevu oluştur
    public Appointment createAppointment(Appointment appointment) {

        boolean exists = !appointmentRepository
                .findByDoctorAndDateAndTime(
                        appointment.getDoctor(),
                        appointment.getDate(),
                        appointment.getTime()
                ).isEmpty();

        if (exists) {
            throw new BusinessException(
                    "Doctor is not available at the selected time."
            );
        }

        appointment.setStatus("BOOKED");
        return appointmentRepository.save(appointment);
    }

    // Doktorun tüm randevuları
    public List<Appointment> getAppointmentsForDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }

    // Hastanın tüm randevuları
    public List<Appointment> getAppointmentsForPatient(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }

    // 🔐 Randevu id ile getir (ROLE + OWNERSHIP KONTROLLÜ)
    public Appointment getById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Appointment not found")
                );

        String currentEmail = getCurrentUserEmail();

        // ADMIN → her şeye erişir
        if (hasRole("ADMIN")) {
            return appointment;
        }

        // DOCTOR → sadece kendi randevuları
        if (hasRole("DOCTOR")) {
            if (!appointment.getDoctor().getEmail().equals(currentEmail)) {
                throw new AccessDeniedException(
                        "Doctors can only access their own appointments"
                );
            }
        }

        // PATIENT → sadece kendi randevuları
        if (hasRole("PATIENT")) {
            if (!appointment.getPatient().getEmail().equals(currentEmail)) {
                throw new AccessDeniedException(
                        "Patients can only access their own appointments"
                );
            }
        }

        return appointment;
    }

    // 🔐 Randevu sil (aynı ownership kuralı geçerli)
    public void delete(Long id) {

        Appointment appointment = getById(id); // ownership burada zaten kontrol ediliyor
        appointmentRepository.delete(appointment);
    }
}
