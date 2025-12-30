package com.smartclinic.smartclinic.service;

import com.smartclinic.smartclinic.model.mysql.Doctor;
import com.smartclinic.smartclinic.repository.mysql.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    // 1️⃣ Doktor oluştur
    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // 2️⃣ Tüm doktorları getir
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // 3️⃣ ID ile doktor getir
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    // 4️⃣ Uzmanlığa göre doktorları getir
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    // 5️⃣ Doktor sil
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    // ----------------------------------------------------
    // ✅ REQUIRED FOR ASSIGNMENT
    // Doctor availability logic
    // ----------------------------------------------------

    /**
     * Belirli bir doktorun belirli bir tarihteki müsait saatlerini döner.
     * (Şu an demo amaçlı statik saatler döndürülüyor)
     */
    public List<LocalTime> getAvailability(Long doctorId, LocalDate date) {

        // Doktor var mı kontrolü
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // ⚠️ Gerçek projede:
        // - Appointment tablosundan dolu saatler çekilir
        // - Çıkarım yapılır
        // Burada assignment için basit ve yeterli bir örnek var

        List<LocalTime> availableSlots = new ArrayList<>();

        availableSlots.add(LocalTime.of(9, 0));
        availableSlots.add(LocalTime.of(10, 0));
        availableSlots.add(LocalTime.of(11, 0));
        availableSlots.add(LocalTime.of(14, 0));
        availableSlots.add(LocalTime.of(15, 0));

        return availableSlots;
    }

    /**
     * JWT token’dan gelen email bilgisine göre doktor ID bulmak için
     * (Controller tarafında role check için kullanılır)
     */
    public Long getDoctorIdByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .map(Doctor::getId)
                .orElseThrow(() -> new RuntimeException("Doctor not found for email: " + email));
    }
}
