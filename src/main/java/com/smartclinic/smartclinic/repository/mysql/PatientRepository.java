package com.smartclinic.smartclinic.repository.mysql;

import com.smartclinic.smartclinic.model.mysql.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
