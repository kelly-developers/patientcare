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
@Table(name = "vital_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VitalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotNull
    @Column(name = "systolic_bp")
    private Integer systolicBp;

    @NotNull
    @Column(name = "diastolic_bp")
    private Integer diastolicBp;

    @NotNull
    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "respiratory_rate")
    private Integer respiratoryRate;

    @NotNull
    private BigDecimal temperature;

    @NotNull
    @Column(name = "oxygen_saturation")
    private Integer oxygenSaturation;

    private BigDecimal height;
    private BigDecimal weight;

    @Column(name = "blood_glucose")
    private Integer bloodGlucose;

    @Column(name = "pain_level")
    private Integer painLevel;

    private BigDecimal bmi;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Lob
    private String notes;

    @NotNull
    @Size(max = 100)
    @Column(name = "recorded_by")
    private String recordedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum RiskLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}