package com.example.patientcare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProcedureRequest {
    @NotNull(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Procedure name is required")
    private String procedureName;

    private String procedureType;
    private LocalDateTime scheduledDate;
    private LocalDateTime actualDate;
    private Integer durationMinutes;
    private String surgeonName;
    private String assistantSurgeon;
    private String anesthesiaType;
    private String preOperativeNotes;
    private String operativeNotes;
    private String postOperativeNotes;
    private String complications;

    @NotBlank(message = "Status is required")
    private String status;
}
