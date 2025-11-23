package com.example.patientcare.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "surgical_procedures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurgicalProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "procedure_name", nullable = false, length = 255)
    private String procedureName;

    @Column(name = "procedure_type", length = 100)
    private String procedureType;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "actual_date")
    private LocalDateTime actualDate;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "surgeon_name", length = 200)
    private String surgeonName;

    @Column(name = "assistant_surgeon", length = 200)
    private String assistantSurgeon;

    @Column(name = "anesthesia_type", length = 100)
    private String anesthesiaType;

    @Column(name = "pre_operative_notes", columnDefinition = "TEXT")
    private String preOperativeNotes;

    @Column(name = "operative_notes", columnDefinition = "TEXT")
    private String operativeNotes;

    @Column(name = "post_operative_notes", columnDefinition = "TEXT")
    private String postOperativeNotes;

    @Column(columnDefinition = "TEXT")
    private String complications;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcedureStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ProcedureStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}