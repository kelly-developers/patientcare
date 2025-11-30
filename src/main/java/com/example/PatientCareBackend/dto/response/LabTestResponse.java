package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.LabTest.Priority;
import com.example.PatientCareBackend.model.LabTest.TestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestResponse {
    private Long id;
    private PatientResponse patient;
    private String testType;
    private String testName;
    private String orderedBy;
    private LocalDateTime orderedDate;
    private TestStatus status;
    private Priority priority;
    private String results;
    private String clinicalNotes;
    private String notes;
    private LocalDateTime reportDate;
    private LocalDateTime completedDate;
    private LocalDateTime createdAt;
}