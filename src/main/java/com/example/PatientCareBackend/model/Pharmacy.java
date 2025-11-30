package com.example.PatientCareBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotBlank
    @Size(max = 255)
    @Column(name = "medication_name")
    private String medicationName;

    @NotBlank
    @Size(max = 100)
    private String dosage;

    @NotBlank
    @Size(max = 100)
    private String frequency;

    @NotBlank
    @Size(max = 100)
    private String duration;

    @Lob
    private String instructions;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrescriptionStatus status;

    @Column(name = "dispensed_at")
    private LocalDateTime dispensedAt;

    @Column(name = "collected_at")
    private LocalDateTime collectedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum PrescriptionStatus {
        PENDING, DISPENSED, COLLECTED
    }
}