package com.example.patientcare.dto.response;

import com.example.patientcare.entity.Patient;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class PatientResponse {
    private UUID id;
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

    // Consent information
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

    // Helper methods for frontend compatibility
    public Map<String, Object> getResearch_consent() {
        return Map.of(
                "dataUse", researchConsent != null ? researchConsent : false,
                "futureContact", futureContactConsent != null ? futureContactConsent : false,
                "anonymizedData", anonymizedDataConsent != null ? anonymizedDataConsent : false,
                "specificStudies", java.util.Collections.emptyList(),
                "consentDate", researchConsentDate
        );
    }

    public Map<String, Object> getSample_storage() {
        return Map.of(
                "storeSamples", sampleStorageConsent != null ? sampleStorageConsent : false,
                "sampleTypes", sampleTypes != null ? java.util.Arrays.asList(sampleTypes.split(",")) : java.util.Collections.emptyList(),
                "storageDuration", storageDuration != null ? storageDuration : "5years",
                "futureResearchUse", futureResearchUseConsent != null ? futureResearchUseConsent : false,
                "destructionConsent", destructionConsent != null ? destructionConsent : false
        );
    }
}