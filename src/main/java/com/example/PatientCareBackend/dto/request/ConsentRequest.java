package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.Consent.ConsentDecision;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsentRequest {
    @NotNull
    private Long surgeryId;

    @NotBlank
    private String patientName;

    @NotBlank
    private String nextOfKin;

    @NotBlank
    private String nextOfKinPhone;

    @NotNull
    private Boolean understoodRisks;

    @NotNull
    private Boolean understoodBenefits;

    @NotNull
    private Boolean understoodAlternatives;

    @NotNull
    private Boolean consentToSurgery;

    @NotBlank
    private String signature;

    @NotNull
    private ConsentDecision consentDecision;

    private String consentFilePath;
}