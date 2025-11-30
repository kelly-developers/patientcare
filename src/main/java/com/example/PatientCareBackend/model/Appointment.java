package com.example.PatientCareBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
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

    @NotNull
    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @NotNull
    @Column(name = "appointment_time")
    private LocalTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @NotBlank
    @Lob
    private String reason;

    @Lob
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "arrival_status")
    private ArrivalStatus arrivalStatus = ArrivalStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum AppointmentType {
        CONSULTATION, FOLLOW_UP, EMERGENCY, SURGERY
    }

    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, COMPLETED, CANCELLED
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum ArrivalStatus {
        PENDING, ARRIVED, LATE, NO_SHOW
    }
}