package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.Surgery.SurgeryStatus;
import com.example.PatientCareBackend.model.Surgery.SurgeryUrgency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryResponse {
    private Long id;
    private PatientResponse patient;
    private String procedureName;
    private SurgeryUrgency urgency;
    private String recommendedBy;
    private String diagnosis;
    private SurgeryStatus status;
    private LocalDateTime consentDate;
    private LocalDateTime scheduledDate;
    private LocalDateTime actualDate;
    private LocalDateTime completedDate;
    private String surgeonName;
    private Integer durationMinutes;
    private LocalDateTime createdAt;
}