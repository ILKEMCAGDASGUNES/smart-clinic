package com.smartclinic.smartclinic.model.mysql;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "patients")
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  id;

    private String name;
    private String email;
    private String password;
}