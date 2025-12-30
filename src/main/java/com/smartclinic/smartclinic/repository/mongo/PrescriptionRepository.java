package com.smartclinic.smartclinic.repository.mongo;

import com.smartclinic.smartclinic.model.mongo.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    List<Prescription> findByPatientId(Long patientId);

    List<Prescription> findByDoctorId(Long doctorId);

    List<Prescription> findByAppointmentId(Long appointmentId);
}
