package com.smartclinic.smartclinic.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    private String id;

    @NotNull(message = "appointmentId is required")
    private Long appointmentId;

    @NotNull(message = "doctorId is required")
    private Long doctorId;

    @NotNull(message = "patientId is required")
    private Long patientId;

    @NotBlank(message = "patientName is required")
    private String patientName;

    @NotNull(message = "medications are required")
    private List<MedicationItem> medications;

    private String notes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant issuedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean refillAllowed;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicationItem {

        @NotBlank(message = "medication name is required")
        private String name;

        @NotBlank(message = "dosage is required")
        private String dosage;

        @NotBlank(message = "frequency is required")
        private String frequency;

        @NotBlank(message = "duration is required")
        private String duration;
    }
}
