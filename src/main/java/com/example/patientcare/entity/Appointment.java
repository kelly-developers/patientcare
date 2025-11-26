package com.example.patientcare.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "appointment_id", unique = true, nullable = false, length = 50)
    private String appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false)
    private AppointmentType appointmentType;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "symptoms", columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "is_urgent")
    private Boolean isUrgent = false;

    @Column(name = "reminder_sent")
    private Boolean reminderSent = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum AppointmentStatus {
        SCHEDULED,
        CONFIRMED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_SHOW,
        RESCHEDULED
    }

    public enum AppointmentType {
        CONSULTATION,
        FOLLOW_UP,
        CHECKUP,
        EMERGENCY,
        SURGERY,
        TEST,
        VACCINATION,
        OTHER
    }

    // Helper method to generate appointment ID
    public void generateAppointmentId() {
        if (this.appointmentId == null) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String patientInitials = patient.getFirstName().substring(0, 1) + patient.getLastName().substring(0, 1);
            this.appointmentId = "APT" + timestamp.substring(timestamp.length() - 6) + patientInitials.toUpperCase();
        }
    }

    // Validation method for appointment dates
    public boolean isValidAppointmentTime() {
        if (appointmentDate == null || endTime == null) {
            return false;
        }
        return appointmentDate.isBefore(endTime) && appointmentDate.isAfter(LocalDateTime.now().minusMinutes(1));
    }

    // Calculate duration in minutes
    public Long getDurationInMinutes() {
        if (appointmentDate != null && endTime != null) {
            return java.time.Duration.between(appointmentDate, endTime).toMinutes();
        }
        return null;
    }
}