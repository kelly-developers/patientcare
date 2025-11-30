package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.Pharmacy.PrescriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyResponse {
    private Long id;
    private PatientResponse patient;
    private UserResponse doctor;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
    private PrescriptionStatus status;
    private LocalDateTime dispensedAt;
    private LocalDateTime collectedAt;
    private LocalDateTime createdAt;
}