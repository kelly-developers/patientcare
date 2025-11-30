package com.example.PatientCareBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "icu_monitoring")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ICU {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Hemodynamics
    @NotNull
    @Column(name = "heart_rate")
    private Integer heartRate;

    @NotNull
    @Column(name = "blood_pressure_systolic")
    private Integer bloodPressureSystolic;

    @NotNull
    @Column(name = "blood_pressure_diastolic")
    private Integer bloodPressureDiastolic;

    @NotNull
    @Column(name = "mean_arterial_pressure")
    private Integer meanArterialPressure;

    @Column(name = "central_venous_pressure")
    private Integer centralVenousPressure;

    // Respiratory
    @NotNull
    @Column(name = "respiratory_rate")
    private Integer respiratoryRate;

    @NotNull
    @Column(name = "oxygen_saturation")
    private Integer oxygenSaturation;

    private Integer fio2;
    private Integer peep;

    @Column(name = "tidal_volume")
    private Integer tidalVolume;

    @Column(name = "ventilation_mode")
    private String ventilationMode;

    // Neurological
    @Column(name = "gcs_total")
    private Integer gcsTotal;

    @Column(name = "gcs_eyes")
    private Integer gcsEyes;

    @Column(name = "gcs_verbal")
    private Integer gcsVerbal;

    @Column(name = "gcs_motor")
    private Integer gcsMotor;

    @Column(name = "pupil_size_left")
    private String pupilSizeLeft;

    @Column(name = "pupil_size_right")
    private String pupilSizeRight;

    @Column(name = "pupil_reaction_left")
    private Boolean pupilReactionLeft;

    @Column(name = "pupil_reaction_right")
    private Boolean pupilReactionRight;

    // Metabolic
    @NotNull
    private BigDecimal temperature;

    @Column(name = "blood_glucose")
    private Integer bloodGlucose;

    @Column(name = "urine_output")
    private Integer urineOutput;

    // Lab Results
    @Column(name = "abg_ph")
    private BigDecimal abgPh;

    @Column(name = "abg_pao2")
    private Integer abgPao2;

    @Column(name = "abg_paco2")
    private Integer abgPaco2;

    @Column(name = "abg_hco3")
    private Integer abgHco3;

    // Metadata
    @NotNull
    @Size(max = 100)
    @Column(name = "recorded_by")
    private String recordedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}