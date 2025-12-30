package com.smartclinic.smartclinic.model.mysql;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bir randevu bir hastaya ait
    @ManyToOne
    @JoinColumn(name="patient_id" , nullable = false)
    private Patient patient;

    // Bir randevu bir doktora ait
    @ManyToOne
    @JoinColumn(name="doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDate date;    // Randevu tarihi
    private LocalTime time;    // Randevu saati

    private String status; // Booked , Cancelled , Completed



}
