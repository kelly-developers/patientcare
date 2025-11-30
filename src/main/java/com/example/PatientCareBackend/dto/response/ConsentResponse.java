package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.Consent.ConsentDecision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsentResponse {
    private Long id;
    private SurgeryResponse surgery;
    private String patientName;
    private String nextOfKin;
    private String nextOfKinPhone;
    private Boolean understoodRisks;
    private Boolean understoodBenefits;
    private Boolean understoodAlternatives;
    private Boolean consentToSurgery;
    private String signature;
    private ConsentDecision consentDecision;
    private String consentFilePath;
    private LocalDateTime createdAt;
}