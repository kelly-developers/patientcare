package com.example.patientcare.dto.response;

import com.example.patientcare.entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private UUID id;
    private String appointmentId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String createdByName;
    private LocalDateTime appointmentDate;
    private LocalDateTime endTime;
    private Appointment.AppointmentStatus status;
    private Appointment.AppointmentType appointmentType;
    private String reason;
    private String symptoms;
    private String notes;
    private String cancellationReason;
    private Boolean isUrgent;
    private Boolean reminderSent;
    private Long durationInMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}