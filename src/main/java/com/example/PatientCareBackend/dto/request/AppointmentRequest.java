package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.Appointment;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {
    private Long patientId;
    private Long doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Appointment.AppointmentType type;
    private String reason;
    private String notes;
    private Appointment.Priority priority;
}