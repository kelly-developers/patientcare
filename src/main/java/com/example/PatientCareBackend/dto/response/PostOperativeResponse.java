package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.PostOperative.FollowupType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostOperativeResponse {
    private Long id;
    private PatientResponse patient;
    private SurgeryResponse surgery;
    private FollowupType followupType;
    private String symptoms;
    private String improvements;
    private String concerns;
    private LocalDateTime nextVisitDate;
    private Boolean medicationAdherence;
    private String notes;
    private LocalDateTime createdAt;
}