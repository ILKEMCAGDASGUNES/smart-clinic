package com.smartclinic.smartclinic.repository.mysql;

import com.smartclinic.smartclinic.model.mysql.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialization(String specialization);
}
