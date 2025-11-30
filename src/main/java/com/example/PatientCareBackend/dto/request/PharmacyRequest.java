package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.Pharmacy.PrescriptionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyRequest {
    @NotNull
    private Long patientId;

    @NotBlank
    private String medicationName;

    @NotBlank
    private String dosage;

    @NotBlank
    private String frequency;

    @NotBlank
    private String duration;

    private String instructions;

    @NotNull
    private Long doctorId;

    @NotNull
    private PrescriptionStatus status;
}