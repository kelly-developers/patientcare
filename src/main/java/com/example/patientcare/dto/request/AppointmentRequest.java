package com.example.patientcare.dto.request;

import com.example.patientcare.entity.Appointment;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "Patient ID is required")
    private String patientId;

    @NotNull(message = "Doctor ID is required")
    private String doctorId;

    @NotNull(message = "Appointment date is required")
    private LocalDateTime appointmentDate;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Appointment type is required")
    private Appointment.AppointmentType appointmentType;

    private Appointment.AppointmentStatus status;

    private String reason;

    private String symptoms;

    private String notes;

    private Boolean isUrgent = false;

    // Validation for appointment duration (typically 15-60 minutes)
    public boolean isValidDuration() {
        if (appointmentDate == null || endTime == null) {
            return false;
        }
        long duration = java.time.Duration.between(appointmentDate, endTime).toMinutes();
        return duration >= 15 && duration <= 240; // 15 minutes to 4 hours
    }
}