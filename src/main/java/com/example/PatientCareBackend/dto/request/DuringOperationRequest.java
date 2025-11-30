package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.DuringOperation.OperationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuringOperationRequest {
    @NotNull
    private Long surgeryId;

    @NotNull
    private Long patientId;

    @NotNull
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotNull
    private OperationStatus status;

    // Vitals
    private Integer heartRate;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer oxygenSaturation;
    private Double temperature;
    private Integer bloodLoss;
    private Integer urineOutput;

    // Surgical Details
    private Map<String, Object> surgicalNotes;
    private Map<String, Object> complications;
    private Map<String, Object> medications;
    private Map<String, Object> outcomes;
    private Map<String, Object> surgicalGoals;
    private Map<String, Object> equipmentCheck;
    private Map<String, Object> closureChecklist;
}