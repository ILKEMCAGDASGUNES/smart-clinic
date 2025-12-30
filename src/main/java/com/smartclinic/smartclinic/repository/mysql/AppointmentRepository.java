package com.smartclinic.smartclinic.repository.mysql;

import com.smartclinic.smartclinic.model.mysql.Appointment;
import com.smartclinic.smartclinic.model.mysql.Doctor;
import com.smartclinic.smartclinic.model.mysql.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Belirli doktorun randevuları
    List<Appointment> findByDoctor(Doctor doctor);

    // Belirli hastanın randevuları
    List<Appointment> findByPatient(Patient patient);

    // Aynı gün + aynı saat + aynı doktor = çakışma kontrolü
    List<Appointment> findByDoctorAndDateAndTime(Doctor doctor, LocalDate date, LocalTime time);
}
