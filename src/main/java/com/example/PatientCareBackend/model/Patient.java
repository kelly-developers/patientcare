package com.example.PatientCareBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "patient_id", unique = true)
    private String patientId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Size(max = 20)
    private String phone;

    @Email
    @Size(max = 100)
    private String email;

    @Lob
    private String address;

    @Size(max = 100)
    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Size(max = 20)
    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Lob
    @Column(name = "medical_history")
    private String medicalHistory;

    @Lob
    private String allergies;

    @Lob
    @Column(name = "current_medications")
    private String currentMedications;

    @Column(name = "research_consent")
    private Boolean researchConsent = false;

    @Column(name = "sample_storage_consent")
    private Boolean sampleStorageConsent = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}