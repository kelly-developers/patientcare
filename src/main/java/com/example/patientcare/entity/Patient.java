package com.example.patientcare.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "patient_id", unique = true, nullable = false, length = 50)
    private String patientId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 20)
    private String phone;

    @Column
    private String email;

    @Column
    private String address;

    @Column(name = "emergency_contact_name", length = 200)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(name = "current_medications", columnDefinition = "TEXT")
    private String currentMedications;

    // Research consent fields
    @Column(name = "research_consent")
    private Boolean researchConsent = false;

    @Column(name = "research_consent_date")
    private LocalDateTime researchConsentDate;

    @Column(name = "future_contact_consent")
    private Boolean futureContactConsent = false;

    @Column(name = "anonymized_data_consent")
    private Boolean anonymizedDataConsent = false;

    // Sample storage consent fields
    @Column(name = "sample_storage_consent")
    private Boolean sampleStorageConsent = false;

    @Column(name = "sample_types")
    private String sampleTypes; // Store as JSON string or comma-separated

    @Column(name = "storage_duration")
    private String storageDuration;

    @Column(name = "future_research_use_consent")
    private Boolean futureResearchUseConsent = false;

    @Column(name = "destruction_consent")
    private Boolean destructionConsent = false;

    // Store additional consent data as JSON
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> consentData;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    // Helper method to generate patient ID
    public void generatePatientId() {
        if (this.patientId == null) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String initials = (firstName.substring(0, 1) + lastName.substring(0, 1)).toUpperCase();
            this.patientId = "P" + timestamp.substring(timestamp.length() - 6);
        }
    }

    // Validation method for date of birth
    public boolean isValidDateOfBirth() {
        if (dateOfBirth == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !dateOfBirth.isAfter(today) && dateOfBirth.isBefore(today.minusYears(120));
    }
}