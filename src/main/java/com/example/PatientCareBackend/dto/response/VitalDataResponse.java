package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.VitalData.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VitalDataResponse {
    private Long id;
    private PatientResponse patient;
    private Integer systolicBp;
    private Integer diastolicBp;
    private Integer heartRate;
    private Integer respiratoryRate;
    private BigDecimal temperature;
    private Integer oxygenSaturation;
    private BigDecimal height;
    private BigDecimal weight;
    private Integer bloodGlucose;
    private Integer painLevel;
    private BigDecimal bmi;
    private RiskLevel riskLevel;
    private String notes;
    private String recordedBy;
    private LocalDateTime createdAt;
}