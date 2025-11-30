package com.example.PatientCareBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreOperativeRequest {
    @NotNull
    private Long patientId;

    @NotBlank
    private String procedureName;

    // Patient Identity
    @NotNull
    private Boolean patientIdentityConfirmed;

    @NotNull
    private Boolean consentSigned;

    @NotNull
    private Boolean siteMarked;

    // Anesthesia Safety
    @NotNull
    private Boolean anesthesiaMachineChecked;

    @NotNull
    private Boolean oxygenAvailable;

    @NotNull
    private Boolean suctionAvailable;

    // Patient Assessment
    private String knownAllergy;
    private String difficultAirway;
    private String aspirationRisk;
    private String bloodLossRisk;

    // Equipment
    @NotNull
    private Boolean sterileIndicatorsConfirmed;

    private String equipmentIssues;
    private String implantAvailable;

    // Team Confirmation
    @NotNull
    private Boolean nurseConfirmed;

    @NotNull
    private Boolean anesthetistConfirmed;

    @NotNull
    private Boolean surgeonConfirmed;

    // Research Consent
    private Boolean researchConsentGiven = false;
    private Boolean dataUsageConsent = false;
    private Boolean sampleStorageConsent = false;
    private LocalDateTime researchConsentDate;
    private String researchConsentWitness;

    // Additional Info
    private String additionalConcerns;

    @NotBlank
    private String completedBy;
}