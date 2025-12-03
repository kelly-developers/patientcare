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
@Table(name = "doctor_analysis", schema = "patientcare")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Patient is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Doctor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @NotBlank(message = "Symptoms are required")
    @Lob
    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 255, message = "Diagnosis must be less than 255 characters")
    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Lob
    @Column(name = "clinical_notes", columnDefinition = "TEXT")
    private String clinicalNotes;

    @Column(name = "recommend_surgery")
    private Boolean recommendSurgery = false;

    @Size(max = 100, message = "Surgery type must be less than 100 characters")
    @Column(name = "surgery_type")
    private String surgeryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "surgery_urgency")
    private SurgeryUrgency surgeryUrgency;

    @Column(name = "require_lab_tests")
    private Boolean requireLabTests = false;

    @Lob
    @Column(name = "lab_tests_needed", columnDefinition = "TEXT")
    private String labTestsNeeded;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnalysisStatus status = AnalysisStatus.COMPLETED;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Surgery Urgency enum - Matching frontend options
    public enum SurgeryUrgency {
        EMERGENT("Emergent", "Emergency surgery needed within 24 hours"),
        URGENT("Urgent", "Surgery needed within 72 hours"),
        SCHEDULED("Scheduled", "Planned surgery within 1-2 weeks"),
        ELECTIVE("Elective", "Non-urgent, planned surgery");

        private final String displayName;
        private final String description;

        SurgeryUrgency(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }

        // Convert from string to enum (case-insensitive)
        public static SurgeryUrgency fromString(String value) {
            if (value == null) {
                return ELECTIVE;
            }

            String normalized = value.trim().toUpperCase();
            return switch (normalized) {
                case "EMERGENT", "EMERGENCY" -> EMERGENT;
                case "URGENT" -> URGENT;
                case "SCHEDULED", "ROUTINE" -> SCHEDULED;
                case "ELECTIVE" -> ELECTIVE;
                default -> ELECTIVE;
            };
        }
    }

    // Analysis Status enum
    public enum AnalysisStatus {
        PENDING("Pending"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        private final String displayName;

        AnalysisStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        // Convert from string to enum (case-insensitive)
        public static AnalysisStatus fromString(String value) {
            if (value == null) {
                return COMPLETED;
            }

            String normalized = value.trim().toUpperCase();
            return switch (normalized) {
                case "PENDING" -> PENDING;
                case "IN_PROGRESS" -> IN_PROGRESS;  // FIXED: Only one "IN_PROGRESS"
                case "COMPLETED" -> COMPLETED;
                case "CANCELLED" -> CANCELLED;
                default -> COMPLETED;
            };
        }
    }

    // Helper methods
    public boolean isSurgeryRecommended() {
        return Boolean.TRUE.equals(recommendSurgery);
    }

    public boolean isLabTestsRequired() {
        return Boolean.TRUE.equals(requireLabTests);
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = AnalysisStatus.COMPLETED;
        }
        if (recommendSurgery == null) {
            recommendSurgery = false;
        }
        if (requireLabTests == null) {
            requireLabTests = false;
        }
    }
}