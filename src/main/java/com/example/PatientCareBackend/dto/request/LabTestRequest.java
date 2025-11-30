package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.LabTest.Priority;
import com.example.PatientCareBackend.model.LabTest.TestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestRequest {
    @NotNull
    private Long patientId;

    @NotBlank
    private String testType;

    @NotBlank
    private String testName;

    @NotBlank
    private String orderedBy;

    @NotNull
    private LocalDateTime orderedDate;

    @NotNull
    private TestStatus status;

    @NotNull
    private Priority priority;

    private String results;
    private String clinicalNotes;
    private String notes;
    private LocalDateTime reportDate;
    private LocalDateTime completedDate;
}