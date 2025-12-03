package com.example.PatientCareBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "surgeries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Surgery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotBlank
    @Size(max = 255)
    @Column(name = "procedure_name")
    private String procedureName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SurgeryUrgency urgency;

    @NotBlank
    @Size(max = 100)
    @Column(name = "recommended_by")
    private String recommendedBy;

    @NotBlank
    @Lob
    private String diagnosis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SurgeryStatus status;

    @Column(name = "consent_date")
    private LocalDateTime consentDate;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Size(max = 100)
    @Column(name = "surgeon_name")
    private String surgeonName;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum SurgeryUrgency {
        EMERGENCY,
        URGENT,
        SCHEDULED,  // Added SCHEDULED
        ROUTINE,
        ELECTIVE
    }

    public enum SurgeryStatus {
        PENDING_CONSENT,
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}