package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Notification.NotificationType type;
    private String title;
    private String message;
    private UserResponse doctor;
    private PatientResponse patient;
    private AppointmentResponse appointment;
    private Notification.Priority priority;
    private Boolean read;
    private LocalDateTime scheduledFor;
    private LocalDateTime createdAt;
}