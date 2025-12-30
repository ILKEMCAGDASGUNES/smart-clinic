package com.smartclinic.smartclinic.seed;

import com.smartclinic.smartclinic.model.mongo.Prescription;
import com.smartclinic.smartclinic.model.mysql.*;
import com.smartclinic.smartclinic.repository.mongo.PrescriptionRepository;
import com.smartclinic.smartclinic.repository.mysql.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final DoctorAvailableTimeRepository doctorAvailableTimeRepository;
    private final PatientRepository patientRepository;
    private final AdminRepository adminRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public DataInitializer(
            DoctorRepository doctorRepository,
            DoctorAvailableTimeRepository doctorAvailableTimeRepository,
            PatientRepository patientRepository,
            AdminRepository adminRepository,
            AppointmentRepository appointmentRepository,
            PrescriptionRepository prescriptionRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.doctorAvailableTimeRepository = doctorAvailableTimeRepository;
        this.patientRepository = patientRepository;
        this.adminRepository = adminRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("[SEED] DataInitializer çalıştı (dev profili).");

        // MySQL
        if (doctorRepository.count() == 0) {
            System.out.println("[SEED] Doctor seed başlıyor...");
            seedDoctorsAndAvailability();
        } else {
            System.out.println("[SEED] Doctors zaten dolu, atlandı.");
        }

        if (patientRepository.count() == 0) {
            System.out.println("[SEED] Patient seed başlıyor...");
            seedPatientsAndAdmin(); // içinde patient + admin var
        } else if (adminRepository.count() == 0) {
            System.out.println("[SEED] Admin seed başlıyor...");
            seedOnlyAdmin();
        } else {
            System.out.println("[SEED] Patients/Admin zaten dolu, atlandı.");
        }

        // Mongo
        if (prescriptionRepository.count() == 0) {
            System.out.println("[SEED] Prescription (Mongo) seed başlıyor...");
            // Adım 5.6'da burayı dolduracağız
        } else {
            System.out.println("[SEED] Prescriptions zaten dolu, atlandı.");
        }

        seedAppointments();
        seedPrescriptions();


    }



    private void seedDoctorsAndAvailability() {

        Doctor doctor1 = new Doctor();
        doctor1.setName("Dr. Ayşe Yılmaz");
        doctor1.setSpecialization("Cardiology");
        doctor1.setEmail("ayse.yilmaz@smartclinic.com");
        doctor1.setPassword("password123");

        Doctor doctor2 = new Doctor();
        doctor2.setName("Dr. Mehmet Kaya");
        doctor2.setSpecialization("Dermatology");
        doctor2.setEmail("mehmet.kaya@smartclinic.com");
        doctor2.setPassword("password123");

        doctorRepository.save(doctor1);
        doctorRepository.save(doctor2);

        // Doctor 1 available times
        DoctorAvailableTime d1t1 = new DoctorAvailableTime(
                doctor1,
                java.time.LocalTime.of(9, 0),
                java.time.LocalTime.of(12, 0)
        );

        DoctorAvailableTime d1t2 = new DoctorAvailableTime(
                doctor1,
                java.time.LocalTime.of(13, 0),
                java.time.LocalTime.of(17, 0)
        );

        // Doctor 2 available times
        DoctorAvailableTime d2t1 = new DoctorAvailableTime(
                doctor2,
                java.time.LocalTime.of(10, 0),
                java.time.LocalTime.of(14, 0)
        );

        DoctorAvailableTime d2t2 = new DoctorAvailableTime(
                doctor2,
                java.time.LocalTime.of(15, 0),
                java.time.LocalTime.of(18, 0)
        );

        doctorAvailableTimeRepository.saveAll(
                java.util.List.of(d1t1, d1t2, d2t1, d2t2)
        );

        System.out.println("[SEED] Doctors ve available times eklendi.");
    }

    private void seedPatientsAndAdmin() {

        Patient patient1 = new Patient();
        patient1.setName("Ali Demir");
        patient1.setEmail("ali.demir@smartclinic.com");
        patient1.setPassword("password123");

        Patient patient2 = new Patient();
        patient2.setName("Zeynep Kaya");
        patient2.setEmail("zeynep.kaya@smartclinic.com");
        patient2.setPassword("password123");

        patientRepository.saveAll(java.util.List.of(patient1, patient2));

        Admin admin = new Admin();
        admin.setName("System Admin");
        admin.setEmail("admin@smartclinic.com");
        admin.setPassword("admin123");

        adminRepository.save(admin);

        System.out.println("[SEED] Patients ve Admin eklendi.");
    }

    private void seedOnlyAdmin() {
        Admin admin = new Admin();
        admin.setName("System Admin");
        admin.setEmail("admin@smartclinic.com");
        admin.setPassword("admin123");
        adminRepository.save(admin);

        System.out.println("[SEED] Admin eklendi.");
    }


    private void seedAppointments() {

        if (appointmentRepository.count() > 0) {
            System.out.println("[SEED] Appointments zaten dolu, atlandı.");
            return;
        }

        // 1) Doktorları ve hastaları çek
        var doctors = doctorRepository.findAll();
        var patients = patientRepository.findAll();

        if (doctors.size() < 2 || patients.size() < 2) {
            System.out.println("[SEED] Appointment seed için en az 2 doctor ve 2 patient gerekli. Atlandı.");
            return;
        }

        Doctor doctor1 = doctors.get(0);
        Doctor doctor2 = doctors.get(1);

        Patient patient1 = patients.get(0);
        Patient patient2 = patients.get(1);

        // 2) Gelecek tarihe randevu oluştur
        Appointment a1 = new Appointment();
        a1.setDoctor(doctor1);
        a1.setPatient(patient1);
        a1.setDate(java.time.LocalDate.now().plusDays(1));
        a1.setTime(java.time.LocalTime.of(10, 0));
        a1.setStatus("Booked");

        Appointment a2 = new Appointment();
        a2.setDoctor(doctor2);
        a2.setPatient(patient2);
        a2.setDate(java.time.LocalDate.now().plusDays(2));
        a2.setTime(java.time.LocalTime.of(14, 30));
        a2.setStatus("Booked");

        appointmentRepository.saveAll(java.util.List.of(a1, a2));

        System.out.println("[SEED] Appointments eklendi.");
    }

    private void seedPrescriptions() {

        if (prescriptionRepository.count() > 0) {
            System.out.println("[SEED] Prescriptions zaten dolu, atlandı.");
            return;
        }

        var appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            System.out.println("[SEED] Appointment yok, prescription seed atlandı.");
            return;
        }

        // İlk 2 appointment üzerinden 2 prescription üret
        var selected = appointments.stream().limit(2).toList();

        var prescriptions = selected.stream().map(a -> {
            var meds = java.util.List.of(
                    Prescription.MedicationItem.builder()
                            .name("Paracetamol")
                            .dosage("500mg")
                            .frequency("2 times a day")
                            .duration("5 days")
                            .build(),
                    Prescription.MedicationItem.builder()
                            .name("Vitamin D")
                            .dosage("1000 IU")
                            .frequency("once a day")
                            .duration("30 days")
                            .build()
            );

            return Prescription.builder()
                    .appointmentId(a.getId())
                    .doctorId(a.getDoctor().getId())
                    .patientId(a.getPatient().getId())
                    .patientName(a.getPatient().getName())
                    .medications(meds)
                    .notes("Seeded sample prescription for testing.")
                    .issuedAt(java.time.Instant.now())
                    .refillAllowed(Boolean.TRUE)
                    .build();
        }).toList();

        prescriptionRepository.saveAll(prescriptions);

        System.out.println("[SEED] Prescriptions (Mongo) eklendi: " + prescriptions.size());
    }



}
