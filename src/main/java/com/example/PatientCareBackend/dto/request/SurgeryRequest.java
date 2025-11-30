package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.Surgery.SurgeryStatus;
import com.example.PatientCareBackend.model.Surgery.SurgeryUrgency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurgeryRequest {
    @NotNull
    private Long patientId;

    @NotBlank
    private String procedureName;

    @NotNull
    private SurgeryUrgency urgency;

    @NotBlank
    private String recommendedBy;

    @NotBlank
    private String diagnosis;

    @NotNull
    private SurgeryStatus status;

    private LocalDateTime consentDate;
    private LocalDateTime scheduledDate;
    private LocalDateTime actualDate;
    private LocalDateTime completedDate;
    private String surgeonName;
    private Integer durationMinutes;
}