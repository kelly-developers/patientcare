package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.Notification;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRequest {
    private Notification.NotificationType type;
    private String title;
    private String message;
    private Long doctorId;
    private Long patientId;
    private Long appointmentId;
    private Notification.Priority priority;
    private LocalDateTime scheduledFor;
}