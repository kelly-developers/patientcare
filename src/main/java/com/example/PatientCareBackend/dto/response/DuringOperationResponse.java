package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.DuringOperation.OperationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuringOperationResponse {
    private Long id;
    private SurgeryResponse surgery;
    private PatientResponse patient;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
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

    private LocalDateTime createdAt;
}