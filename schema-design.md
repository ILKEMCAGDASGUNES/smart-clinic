# Database Schema Design – Smart Clinic (MySQL)

## Overview
This document describes the MySQL database schema for the Smart Clinic Management System.
The database is designed to support Patients, Doctors, Appointments, and Admin operations.

---

## Tables

### 1. users
Stores system users including patients, doctors, and admins.

| Column Name | Data Type | Constraints |
|------------|----------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| name | VARCHAR(100) | NOT NULL |
| email | VARCHAR(150) | UNIQUE, NOT NULL |
| password | VARCHAR(255) | NOT NULL |
| role | ENUM('PATIENT','DOCTOR','ADMIN') | NOT NULL |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

---

### 2. doctors
Stores doctor-specific information.

| Column Name | Data Type | Constraints |
|------------|----------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| user_id | BIGINT | FOREIGN KEY REFERENCES users(id) |
| specialty | VARCHAR(100) | NOT NULL |
| available | BOOLEAN | DEFAULT TRUE |

---

### 3. patients
Stores patient-specific information.

| Column Name | Data Type | Constraints |
|------------|----------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| user_id | BIGINT | FOREIGN KEY REFERENCES users(id) |
| phone | VARCHAR(20) | |
| date_of_birth | DATE | |

---

### 4. appointments
Stores appointment details between patients and doctors.

| Column Name | Data Type | Constraints |
|------------|----------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| patient_id | BIGINT | FOREIGN KEY REFERENCES patients(id) |
| doctor_id | BIGINT | FOREIGN KEY REFERENCES doctors(id) |
| appointment_time | DATETIME | NOT NULL |
| status | ENUM('SCHEDULED','CANCELLED','COMPLETED') | NOT NULL |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

---

## Relationships
- A **User** can be a Patient, Doctor, or Admin.
- A **Doctor** is linked to one User.
- A **Patient** is linked to one User.
- An **Appointment** links one Patient with one Doctor.

---

## Notes
- Passwords are stored as hashed values.
- Foreign key constraints ensure referential integrity.
- ENUM types are used for controlled status and role values.
