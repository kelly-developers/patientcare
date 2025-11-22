package com.example.patientcare.dto.request;

import com.example.patientcare.entity.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientRequest {

    @NotNull(message = "Patient ID is required")
    @Size(min = 1, max = 50, message = "Patient ID must be between 1 and 50 characters")
    private String patientId;

    @NotNull(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    private Patient.Gender gender;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    private String address;

    @Size(max = 200, message = "Emergency contact name must not exceed 200 characters")
    private String emergencyContactName;

    @Size(max = 20, message = "Emergency contact phone must not exceed 20 characters")
    private String emergencyContactPhone;

    private String medicalHistory;

    private String allergies;

    private String currentMedications;

    private Boolean researchConsent = false;

    private LocalDateTime researchConsentDate;

    // Constructors
    public PatientRequest() {}

    public PatientRequest(String patientId, String firstName, String lastName, LocalDate dateOfBirth) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Patient.Gender getGender() { return gender; }
    public void setGender(Patient.Gender gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }

    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }

    public String getCurrentMedications() { return currentMedications; }
    public void setCurrentMedications(String currentMedications) { this.currentMedications = currentMedications; }

    public Boolean getResearchConsent() { return researchConsent; }
    public void setResearchConsent(Boolean researchConsent) { this.researchConsent = researchConsent; }

    public LocalDateTime getResearchConsentDate() { return researchConsentDate; }
    public void setResearchConsentDate(LocalDateTime researchConsentDate) { this.researchConsentDate = researchConsentDate; }
}