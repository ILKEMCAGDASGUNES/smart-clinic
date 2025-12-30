package com.smartclinic.smartclinic.model.mysql;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_available_times")
public class DoctorAvailableTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    protected DoctorAvailableTime() { }

    public DoctorAvailableTime(Doctor doctor, LocalTime startTime, LocalTime endTime) {
        this.doctor = doctor;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() { return id; }
    public Doctor getDoctor() { return doctor; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
}
