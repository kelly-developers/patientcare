package com.example.PatientCareBackend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ICURequest {
    @NotNull
    private Long patientId;

    // Hemodynamics
    @NotNull
    private Integer heartRate;

    @NotNull
    private Integer bloodPressureSystolic;

    @NotNull
    private Integer bloodPressureDiastolic;

    @NotNull
    private Integer meanArterialPressure;

    private Integer centralVenousPressure;

    // Respiratory
    @NotNull
    private Integer respiratoryRate;

    @NotNull
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
    @NotNull
    private BigDecimal temperature;

    private Integer bloodGlucose;
    private Integer urineOutput;

    // Lab Results
    private BigDecimal abgPh;
    private Integer abgPao2;
    private Integer abgPaco2;
    private Integer abgHco3;

    // Metadata
    @NotNull
    private String recordedBy;
}