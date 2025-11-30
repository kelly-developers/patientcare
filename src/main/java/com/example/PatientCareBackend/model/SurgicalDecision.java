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
@Table(name = "surgical_decisions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgicalDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_id")
    private Surgery surgery;

    @NotBlank
    @Size(max = 100)
    @Column(name = "surgeon_name")
    private String surgeonName;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_status", nullable = false)
    private DecisionStatus decisionStatus;

    @Lob
    private String comments;

    @Column(name = "factors_considered", columnDefinition = "TEXT")
    private String factorsConsidered;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum DecisionStatus {
        ACCEPTED, DECLINED
    }
}