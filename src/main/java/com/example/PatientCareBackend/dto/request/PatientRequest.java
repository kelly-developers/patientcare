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
    @NotBlank
    @Size(max = 20)
    private String patientId;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
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

    private Boolean researchConsent = false;

    private Boolean sampleStorageConsent = false;
}