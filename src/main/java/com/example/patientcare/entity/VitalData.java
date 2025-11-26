package com.example.patientcare.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vital_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VitalData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by", nullable = false)
    private User recordedBy;

    // Vital signs measurements
    @Column(name = "systolic_bp")
    private Integer systolicBP; // mmHg

    @Column(name = "diastolic_bp")
    private Integer diastolicBP; // mmHg

    @Column(name = "heart_rate")
    private Integer heartRate; // BPM

    @Column(name = "respiratory_rate")
    private Integer respiratoryRate; // breaths per minute

    @Column(name = "temperature")
    private Double temperature; // Celsius

    @Column(name = "oxygen_saturation")
    private Double oxygenSaturation; // Percentage

    @Column(name = "height")
    private Double height; // cm

    @Column(name = "weight")
    private Double weight; // kg

    @Column(name = "blood_glucose")
    private Double bloodGlucose; // mg/dL

    @Column(name = "pain_level")
    private Integer painLevel; // 0-10 scale

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods for BMI calculation
    public Double calculateBMI() {
        if (height == null || weight == null || height <= 0 || weight <= 0) {
            return null;
        }
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    // Helper method to get blood pressure as string
    public String getBloodPressure() {
        if (systolicBP != null && diastolicBP != null) {
            return systolicBP + "/" + diastolicBP + " mmHg";
        }
        return null;
    }

    // Validation method for vital signs ranges
    public boolean isValidVitalData() {
        // Validate basic ranges
        if (systolicBP != null && (systolicBP < 70 || systolicBP > 250)) return false;
        if (diastolicBP != null && (diastolicBP < 40 || diastolicBP > 150)) return false;
        if (heartRate != null && (heartRate < 30 || heartRate > 250)) return false;
        if (respiratoryRate != null && (respiratoryRate < 8 || respiratoryRate > 60)) return false;
        if (temperature != null && (temperature < 32 || temperature > 43)) return false;
        if (oxygenSaturation != null && (oxygenSaturation < 70 || oxygenSaturation > 100)) return false;
        if (painLevel != null && (painLevel < 0 || painLevel > 10)) return false;

        return true;
    }
}