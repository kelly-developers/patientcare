package com.example.PatientCareBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ICUResponse {
    private Long id;
    private PatientResponse patient;

    // Hemodynamics
    private Integer heartRate;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer meanArterialPressure;
    private Integer centralVenousPressure;

    // Respiratory
    private Integer respiratoryRate;
    private Integer oxygenSaturation;
    private Integer fio2;
    private Integer peep;
    private Integer tidalVolume;
    private String ventilationMode;

    // Neurological
    private Integer gcsTotal;
    private Integer gcsEyes;
    private Integer gcsVerbal;
    private Integer gcsMotor;
    private String pupilSizeLeft;
    private String pupilSizeRight;
    private Boolean pupilReactionLeft;
    private Boolean pupilReactionRight;

    // Metabolic
    private BigDecimal temperature;
    private Integer bloodGlucose;
    private Integer urineOutput;

    // Lab Results
    private BigDecimal abgPh;
    private Integer abgPao2;
    private Integer abgPaco2;
    private Integer abgHco3;

    // Metadata
    private String recordedBy;
    private LocalDateTime createdAt;
}