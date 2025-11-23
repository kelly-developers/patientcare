package com.example.patientcare.dto.response;

import com.example.patientcare.entity.Patient;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
        Map<String, Object> researchConsentMap = new HashMap<>();
        researchConsentMap.put("dataUse", researchConsent != null ? researchConsent : false);
        researchConsentMap.put("futureContact", futureContactConsent != null ? futureContactConsent : false);
        researchConsentMap.put("anonymizedData", anonymizedDataConsent != null ? anonymizedDataConsent : false);
        researchConsentMap.put("specificStudies", Collections.emptyList());
        researchConsentMap.put("consentDate", researchConsentDate);
        return researchConsentMap;
    }

    public Map<String, Object> getSample_storage() {
        Map<String, Object> sampleStorageMap = new HashMap<>();
        sampleStorageMap.put("storeSamples", sampleStorageConsent != null ? sampleStorageConsent : false);

        // Handle sample types conversion
        List<String> sampleTypesList = new ArrayList<>();
        if (sampleTypes != null && !sampleTypes.trim().isEmpty()) {
            sampleTypesList = Arrays.asList(sampleTypes.split(","));
        }
        sampleStorageMap.put("sampleTypes", sampleTypesList);

        sampleStorageMap.put("storageDuration", storageDuration != null ? storageDuration : "5years");
        sampleStorageMap.put("futureResearchUse", futureResearchUseConsent != null ? futureResearchUseConsent : false);
        sampleStorageMap.put("destructionConsent", destructionConsent != null ? destructionConsent : false);
        return sampleStorageMap;
    }

    // Additional getters for frontend compatibility
    public String getFirst_name() {
        return firstName;
    }

    public String getLast_name() {
        return lastName;
    }

    public String getDate_of_birth() {
        return dateOfBirth != null ? dateOfBirth.toString() : null;
    }

    public String getEmergency_contact_name() {
        return emergencyContactName;
    }

    public String getEmergency_contact_phone() {
        return emergencyContactPhone;
    }

    public String getMedical_history() {
        return medicalHistory;
    }

    public String getCurrent_medications() {
        return currentMedications;
    }

    public Boolean getResearch_consent_flag() {
        return researchConsent;
    }

    public String getResearch_consent_date() {
        return researchConsentDate != null ? researchConsentDate.toString() : null;
    }

    public String getCreated_at() {
        return createdAt != null ? createdAt.toString() : null;
    }

    public String getUpdated_at() {
        return updatedAt != null ? updatedAt.toString() : null;
    }
}