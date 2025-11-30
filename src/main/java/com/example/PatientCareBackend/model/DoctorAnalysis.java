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
@Table(name = "doctor_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @NotBlank
    @Lob
    private String symptoms;

    @NotBlank
    @Size(max = 255)
    private String diagnosis;

    @Lob
    @Column(name = "clinical_notes")
    private String clinicalNotes;

    @Column(name = "recommend_surgery")
    private Boolean recommendSurgery = false;

    @Size(max = 100)
    @Column(name = "surgery_type")
    private String surgeryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "surgery_urgency")
    private SurgeryUrgency surgeryUrgency;

    @Column(name = "require_lab_tests")
    private Boolean requireLabTests = false;

    @Lob
    @Column(name = "lab_tests_needed")
    private String labTestsNeeded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum SurgeryUrgency {
        EMERGENCY, URGENT, ROUTINE, ELECTIVE
    }

    public enum AnalysisStatus {
        PENDING, COMPLETED
    }
}