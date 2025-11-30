package com.example.PatientCareBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "during_operation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuringOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_id")
    private Surgery surgery;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotNull
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationStatus status;

    // Vitals
    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "blood_pressure_systolic")
    private Integer bloodPressureSystolic;

    @Column(name = "blood_pressure_diastolic")
    private Integer bloodPressureDiastolic;

    @Column(name = "oxygen_saturation")
    private Integer oxygenSaturation;

    private Double temperature;

    @Column(name = "blood_loss")
    private Integer bloodLoss;

    @Column(name = "urine_output")
    private Integer urineOutput;

    // Surgical Details - Store as JSON strings in TEXT columns
    @Column(name = "surgical_notes", columnDefinition = "TEXT")
    private String surgicalNotes;

    @Column(name = "complications", columnDefinition = "TEXT")
    private String complications;

    @Column(name = "medications", columnDefinition = "TEXT")
    private String medications;

    @Column(name = "outcomes", columnDefinition = "TEXT")
    private String outcomes;

    @Column(name = "surgical_goals", columnDefinition = "TEXT")
    private String surgicalGoals;

    @Column(name = "equipment_check", columnDefinition = "TEXT")
    private String equipmentCheck;

    @Column(name = "closure_checklist", columnDefinition = "TEXT")
    private String closureChecklist;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum OperationStatus {
        IN_PROGRESS, COMPLETED, EMERGENCY
    }
}