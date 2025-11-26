package com.example.patientcare.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VitalDataRequest {

    @NotNull(message = "Patient ID is required")
    private String patientId;

    // Vital signs with validation ranges
    @Positive(message = "Systolic BP must be positive")
    private Integer systolicBP;

    @Positive(message = "Diastolic BP must be positive")
    private Integer diastolicBP;

    @Positive(message = "Heart rate must be positive")
    private Integer heartRate;

    @Positive(message = "Respiratory rate must be positive")
    private Integer respiratoryRate;

    @Positive(message = "Temperature must be positive")
    private Double temperature;

    @Positive(message = "Oxygen saturation must be positive")
    private Double oxygenSaturation;

    @Positive(message = "Height must be positive")
    private Double height;

    @Positive(message = "Weight must be positive")
    private Double weight;

    @Positive(message = "Blood glucose must be positive")
    private Double bloodGlucose;

    private Integer painLevel; // 0-10 scale

    private String notes;
}