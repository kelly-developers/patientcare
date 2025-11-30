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
@Table(name = "lab_tests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotBlank
    @Size(max = 100)
    @Column(name = "test_type")
    private String testType;

    @NotBlank
    @Size(max = 255)
    @Column(name = "test_name")
    private String testName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "ordered_by")
    private String orderedBy;

    @NotNull
    @Column(name = "ordered_date")
    private LocalDateTime orderedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Lob
    private String results;

    @Lob
    @Column(name = "clinical_notes")
    private String clinicalNotes;

    @Lob
    private String notes;

    @Column(name = "report_date")
    private LocalDateTime reportDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum TestStatus {
        ORDERED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public enum Priority {
        ROUTINE, URGENT, STAT
    }
}