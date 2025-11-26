package com.example.patientcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VitalDataResponse {
    private String id;
    private String patientId;
    private String patientName;
    private String recordedBy;
    private String recordedByName;
    private Integer systolicBP;
    private Integer diastolicBP;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Double temperature;
    private Double oxygenSaturation;
    private Double height;
    private Double weight;
    private Double bloodGlucose;
    private Integer painLevel;
    private Double bmi;
    private String bloodPressure;
    private String notes;
    private LocalDateTime recordedAt;
    private LocalDateTime updatedAt;
}