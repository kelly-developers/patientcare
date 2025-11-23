package com.example.patientcare.dto.request;

import com.example.patientcare.entity.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class PatientRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Patient.Gender gender;

    private String phone;

    @Email(message = "Invalid email format")
    private String email;

    private String address;

    private String emergencyContactName;
    private String emergencyContactPhone;
    private String medicalHistory;
    private String allergies;
    private String currentMedications;

    // Consent fields
    private Boolean researchConsent = false;
    private LocalDateTime researchConsentDate;
    private Boolean futureContactConsent = false;
    private Boolean anonymizedDataConsent = false;
    private Boolean sampleStorageConsent = false;
    private String sampleTypes;
    private String storageDuration;
    private Boolean futureResearchUseConsent = false;
    private Boolean destructionConsent = false;
    private Map<String, Object> consentData;
}