package com.smartclinic.smartclinic.service;

import com.smartclinic.smartclinic.model.mongo.Prescription;
import com.smartclinic.smartclinic.repository.mongo.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public Prescription createPrescription(Prescription prescription) {

        prescription.setIssuedAt(Instant.now());

        if (prescription.getRefillAllowed() == null) {
            prescription.setRefillAllowed(false);
        }

        return prescriptionRepository.save(prescription);
    }


    public List<Prescription> getByPatient(Long patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public List<Prescription> getByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    public List<Prescription> getByAppointment(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }
}
