package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.Patient.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private Long id;
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String phone;
    private String email;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String medicalHistory;
    private String allergies;
    private String currentMedications;
    private Boolean consentAccepted;
    private String consentFormPath;
    private Boolean researchConsent;
    private LocalDateTime researchConsentDate;
    private Boolean futureContactConsent;
    private Boolean anonymizedDataConsent;
    private Boolean sampleStorageConsent;
    private String sampleTypes;
    private String storageDuration;
    private Boolean futureResearchUseConsent;
    private Boolean destructionConsent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PatientResponse(Long id, String patientId, String firstName, String lastName,
                           LocalDate dateOfBirth, Gender gender, String phone, String email,
                           String address, String emergencyContactName, String emergencyContactPhone,
                           String medicalHistory, String allergies, String currentMedications,
                           Boolean consentAccepted, String consentFormPath,
                           Boolean researchConsent, Boolean sampleStorageConsent,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.medicalHistory = medicalHistory;
        this.allergies = allergies;
        this.currentMedications = currentMedications;
        this.consentAccepted = consentAccepted;
        this.consentFormPath = consentFormPath;
        this.researchConsent = researchConsent;
        this.sampleStorageConsent = sampleStorageConsent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        // Set default values for other fields
        this.researchConsentDate = null;
        this.futureContactConsent = false;
        this.anonymizedDataConsent = false;
        this.sampleTypes = "";
        this.storageDuration = "";
        this.futureResearchUseConsent = false;
        this.destructionConsent = false;
    }
}