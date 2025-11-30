package com.example.PatientCareBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreOperativeResponse {
    private Long id;
    private PatientResponse patient;
    private String procedureName;

    // Patient Identity
    private Boolean patientIdentityConfirmed;
    private Boolean consentSigned;
    private Boolean siteMarked;

    // Anesthesia Safety
    private Boolean anesthesiaMachineChecked;
    private Boolean oxygenAvailable;
    private Boolean suctionAvailable;

    // Patient Assessment
    private String knownAllergy;
    private String difficultAirway;
    private String aspirationRisk;
    private String bloodLossRisk;

    // Equipment
    private Boolean sterileIndicatorsConfirmed;
    private String equipmentIssues;
    private String implantAvailable;

    // Team Confirmation
    private Boolean nurseConfirmed;
    private Boolean anesthetistConfirmed;
    private Boolean surgeonConfirmed;

    // Research Consent
    private Boolean researchConsentGiven;
    private Boolean dataUsageConsent;
    private Boolean sampleStorageConsent;
    private LocalDateTime researchConsentDate;
    private String researchConsentWitness;

    // Additional Info
    private String additionalConcerns;
    private String completedBy;
    private LocalDateTime completedAt;
}