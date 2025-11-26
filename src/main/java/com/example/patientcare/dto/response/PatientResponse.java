package com.example.patientcare.dto.response;

import com.example.patientcare.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private String id;
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Patient.Gender gender;
    private String phone;
    private String email;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String medicalHistory;
    private String allergies;
    private String currentMedications;
    private Boolean researchConsent;
    private LocalDateTime researchConsentDate;
    private Boolean futureContactConsent;
    private Boolean anonymizedDataConsent;
    private Boolean sampleStorageConsent;
    private String sampleTypes;
    private String storageDuration;
    private Boolean futureResearchUseConsent;
    private Boolean destructionConsent;
    private Map<String, Object> consentData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}