package com.smartclinic.smartclinic.service;

import com.smartclinic.smartclinic.model.mysql.Doctor;
import com.smartclinic.smartclinic.repository.mysql.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
