package com.smartclinic.smartclinic.model.mysql;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name ="doctors")
@Data
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String specialization;

    private String email;

    private String password;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<DoctorAvailableTime> availableTimes = new java.util.ArrayList<>();


}
