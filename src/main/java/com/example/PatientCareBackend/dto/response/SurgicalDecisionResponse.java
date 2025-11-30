package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.SurgicalDecision.DecisionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgicalDecisionResponse {
    private Long id;
    private SurgeryResponse surgery;
    private String surgeonName;
    private DecisionStatus decisionStatus;
    private String comments;
    private Map<String, Object> factorsConsidered;
    private LocalDateTime createdAt;
}