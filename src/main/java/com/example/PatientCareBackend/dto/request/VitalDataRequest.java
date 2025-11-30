package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.VitalData.RiskLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VitalDataRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private Integer systolicBp;

    @NotNull
    private Integer diastolicBp;

    @NotNull
    private Integer heartRate;

    private Integer respiratoryRate;

    @NotNull
    private BigDecimal temperature;

    @NotNull
    private Integer oxygenSaturation;

    private BigDecimal height;
    private BigDecimal weight;
    private Integer bloodGlucose;
    private Integer painLevel;
    private BigDecimal bmi;

    @NotNull
    private RiskLevel riskLevel;

    private String notes;

    @NotNull
    private String recordedBy;
}