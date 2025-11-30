package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.SurgicalDecision.DecisionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgicalDecisionRequest {
    @NotNull
    private Long surgeryId;

    @NotBlank
    private String surgeonName;

    @NotNull
    private DecisionStatus decisionStatus;

    private String comments;

    private Map<String, Object> factorsConsidered;
}