package com.example.PatientCareBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_operative_followup")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostOperative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_id")
    private Surgery surgery;

    @Enumerated(EnumType.STRING)
    @Column(name = "followup_type", nullable = false)
    private FollowupType followupType;

    @Lob
    private String symptoms;

    @Lob
    private String improvements;

    @Lob
    private String concerns;

    @Column(name = "next_visit_date")
    private LocalDateTime nextVisitDate;

    @Column(name = "medication_adherence")
    private Boolean medicationAdherence;

    @Lob
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum FollowupType {
        POST_SURGERY, MEDICATION_REVIEW, ROUTINE_CHECKUP, COMPLICATION_ASSESSMENT
    }
}