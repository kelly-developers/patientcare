package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.PostOperative.FollowupType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostOperativeRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private Long surgeryId;

    @NotNull
    private FollowupType followupType;

    private String symptoms;
    private String improvements;
    private String concerns;
    private LocalDateTime nextVisitDate;
    private Boolean medicationAdherence;
    private String notes;
}