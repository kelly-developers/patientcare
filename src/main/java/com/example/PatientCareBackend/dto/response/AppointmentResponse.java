package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.Appointment;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AppointmentResponse {
    private Long id;
    private PatientResponse patient;
    private UserResponse doctor;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Appointment.AppointmentType type;
    private Appointment.AppointmentStatus status;
    private String reason;
    private String notes;
    private Appointment.Priority priority; // Correct type
    private Appointment.ArrivalStatus arrivalStatus;
    private LocalDateTime createdAt;
}