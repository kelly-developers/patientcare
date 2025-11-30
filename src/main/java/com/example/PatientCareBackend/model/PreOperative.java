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
@Table(name = "pre_operative_checklist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreOperative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotBlank
    @Size(max = 255)
    @Column(name = "procedure_name")
    private String procedureName;

    // Patient Identity
    @NotNull
    @Column(name = "patient_identity_confirmed")
    private Boolean patientIdentityConfirmed;

    @NotNull
    @Column(name = "consent_signed")
    private Boolean consentSigned;

    @NotNull
    @Column(name = "site_marked")
    private Boolean siteMarked;

    // Anesthesia Safety
    @NotNull
    @Column(name = "anesthesia_machine_checked")
    private Boolean anesthesiaMachineChecked;

    @NotNull
    @Column(name = "oxygen_available")
    private Boolean oxygenAvailable;

    @NotNull
    @Column(name = "suction_available")
    private Boolean suctionAvailable;

    // Patient Assessment
    @Size(max = 50)
    @Column(name = "known_allergy")
    private String knownAllergy;

    @Size(max = 50)
    @Column(name = "difficult_airway")
    private String difficultAirway;

    @Size(max = 50)
    @Column(name = "aspiration_risk")
    private String aspirationRisk;

    @Size(max = 50)
    @Column(name = "blood_loss_risk")
    private String bloodLossRisk;

    // Equipment
    @NotNull
    @Column(name = "sterile_indicators_confirmed")
    private Boolean sterileIndicatorsConfirmed;

    @Lob
    @Column(name = "equipment_issues")
    private String equipmentIssues;

    @Size(max = 50)
    @Column(name = "implant_available")
    private String implantAvailable;

    // Team Confirmation
    @NotNull
    @Column(name = "nurse_confirmed")
    private Boolean nurseConfirmed;

    @NotNull
    @Column(name = "anesthetist_confirmed")
    private Boolean anesthetistConfirmed;

    @NotNull
    @Column(name = "surgeon_confirmed")
    private Boolean surgeonConfirmed;

    // Research Consent
    @Column(name = "research_consent_given")
    private Boolean researchConsentGiven = false;

    @Column(name = "data_usage_consent")
    private Boolean dataUsageConsent = false;

    @Column(name = "sample_storage_consent")
    private Boolean sampleStorageConsent = false;

    @Column(name = "research_consent_date")
    private LocalDateTime researchConsentDate;

    @Size(max = 100)
    @Column(name = "research_consent_witness")
    private String researchConsentWitness;

    // Additional Info
    @Lob
    @Column(name = "additional_concerns")
    private String additionalConcerns;

    @NotBlank
    @Size(max = 100)
    @Column(name = "completed_by")
    private String completedBy;

    @CreationTimestamp
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}