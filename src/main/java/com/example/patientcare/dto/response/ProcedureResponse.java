package com.example.patientcare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureResponse {
    private String id;
    private String patientId;
    private String patientName;
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
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
