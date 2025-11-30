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
@Table(name = "consent_management")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_id")
    private Surgery surgery;

    @NotBlank
    @Size(max = 100)
    @Column(name = "patient_name")
    private String patientName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "next_of_kin")
    private String nextOfKin;

    @NotBlank
    @Size(max = 20)
    @Column(name = "next_of_kin_phone")
    private String nextOfKinPhone;

    @NotNull
    @Column(name = "understood_risks")
    private Boolean understoodRisks;

    @NotNull
    @Column(name = "understood_benefits")
    private Boolean understoodBenefits;

    @NotNull
    @Column(name = "understood_alternatives")
    private Boolean understoodAlternatives;

    @NotNull
    @Column(name = "consent_to_surgery")
    private Boolean consentToSurgery;

    @NotBlank
    @Size(max = 100)
    private String signature;

    @Enumerated(EnumType.STRING)
    @Column(name = "consent_decision", nullable = false)
    private ConsentDecision consentDecision;

    @Size(max = 255)
    @Column(name = "consent_file_path")
    private String consentFilePath;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum ConsentDecision {
        ACCEPTED, DECLINED
    }
}