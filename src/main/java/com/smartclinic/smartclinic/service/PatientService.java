package com.smartclinic.smartclinic.service;

import com.smartclinic.smartclinic.model.mysql.Patient;
import com.smartclinic.smartclinic.repository.mysql.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // 
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient updatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deletePatientById(Long id) {
        patientRepository.deleteById(id);
    }


}
