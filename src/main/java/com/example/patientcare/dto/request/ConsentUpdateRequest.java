package com.example.patientcare.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ConsentUpdateRequest {

    @NotNull(message = "Research consent status is required")
    private Boolean researchConsent;

    private LocalDateTime researchConsentDate;

    // Constructors
    public ConsentUpdateRequest() {}

    public ConsentUpdateRequest(Boolean researchConsent) {
        this.researchConsent = researchConsent;
    }

    // Getters and Setters
    public Boolean getResearchConsent() { return researchConsent; }
    public void setResearchConsent(Boolean researchConsent) { this.researchConsent = researchConsent; }

    public LocalDateTime getResearchConsentDate() { return researchConsentDate; }
    public void setResearchConsentDate(LocalDateTime researchConsentDate) { this.researchConsentDate = researchConsentDate; }
}