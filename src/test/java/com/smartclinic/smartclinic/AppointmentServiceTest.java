package com.smartclinic.smartclinic;

import com.smartclinic.smartclinic.exception.BusinessException;
import com.smartclinic.smartclinic.exception.ResourceNotFoundException;
import com.smartclinic.smartclinic.model.mysql.Appointment;
import com.smartclinic.smartclinic.model.mysql.Doctor;
import com.smartclinic.smartclinic.model.mysql.Patient;
import com.smartclinic.smartclinic.repository.mysql.AppointmentRepository;
import com.smartclinic.smartclinic.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        MockitoAnnotations.openMocks(this);
    }

    private void authenticateAs(String email, String role) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(() -> "ROLE_" + role)
                )
        );
    }

    @Test
    void shouldThrowException_whenDoctorHasAppointmentConflict() {

        Appointment appointment = new Appointment();
        appointment.setDoctor(new Doctor());
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setTime(LocalTime.of(10, 0));

        when(appointmentRepository.findByDoctorAndDateAndTime(
                any(), any(), any())
        ).thenReturn(List.of(new Appointment()));

        assertThatThrownBy(() ->
                appointmentService.createAppointment(appointment)
        ).isInstanceOf(BusinessException.class);
    }

    @Test
    void patientCannotAccessOtherPatientsAppointment() {

        authenticateAs("patient1@mail.com", "PATIENT");

        Appointment appointment = new Appointment();

        Patient patient = new Patient();
        patient.setEmail("patient2@mail.com");
        appointment.setPatient(patient);

        Doctor doctor = new Doctor();
        doctor.setEmail("doctor@mail.com");
        appointment.setDoctor(doctor);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(appointment));

        assertThatThrownBy(() ->
                appointmentService.getById(1L)
        ).isInstanceOf(org.springframework.security.access.AccessDeniedException.class);
    }

    @Test
    void shouldThrowNotFound_whenAppointmentDoesNotExist() {

        authenticateAs("admin@mail.com", "ADMIN");

        when(appointmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                appointmentService.getById(99L)
        ).isInstanceOf(ResourceNotFoundException.class);
    }
}
