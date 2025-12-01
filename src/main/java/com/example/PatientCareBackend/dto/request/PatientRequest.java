package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.Patient.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Size(max = 20)
    private String phone;

    @Email
    @Size(max = 100)
    private String email;

    private String address;

    @Size(max = 100)
    private String emergencyContactName;

    @Size(max = 20)
    private String emergencyContactPhone;

    private String medicalHistory;

    private String allergies;

    private String currentMedications;

    private Boolean consentAccepted = false;

    private String consentFormPath; // Path to uploaded consent form

    // Research consent fields (optional)
    private Boolean researchConsent = false;
    private Boolean futureContactConsent = false;
    private Boolean anonymizedDataConsent = false;
    private Boolean sampleStorageConsent = false;
    private String sampleTypes;
    private String storageDuration;
    private Boolean futureResearchUseConsent = false;
    private Boolean destructionConsent = false;
}