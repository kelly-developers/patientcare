package com.example.patientcare.dto.response;

import com.example.patientcare.entity.Patient;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientResponse {
    private Long id;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public PatientResponse() {}

    public PatientResponse(Patient patient) {
        this.id = patient.getId();
        this.patientId = patient.getPatientId();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.dateOfBirth = patient.getDateOfBirth();
        this.gender = patient.getGender();
        this.phone = patient.getPhone();
        this.email = patient.getEmail();
        this.address = patient.getAddress();
        this.emergencyContactName = patient.getEmergencyContactName();
        this.emergencyContactPhone = patient.getEmergencyContactPhone();
        this.medicalHistory = patient.getMedicalHistory();
        this.allergies = patient.getAllergies();
        this.currentMedications = patient.getCurrentMedications();
        this.researchConsent = patient.getResearchConsent();
        this.researchConsentDate = patient.getResearchConsentDate();
        this.createdAt = patient.getCreatedAt();
        this.updatedAt = patient.getUpdatedAt();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}