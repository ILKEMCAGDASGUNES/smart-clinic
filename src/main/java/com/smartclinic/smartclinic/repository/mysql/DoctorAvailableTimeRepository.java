package com.smartclinic.smartclinic.repository.mysql;

import com.smartclinic.smartclinic.model.mysql.DoctorAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorAvailableTimeRepository extends JpaRepository<DoctorAvailableTime, Long> {
}
