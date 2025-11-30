package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.DoctorAnalysis.AnalysisStatus;
import com.example.PatientCareBackend.model.DoctorAnalysis.SurgeryUrgency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAnalysisResponse {
    private Long id;
    private PatientResponse patient;
    private UserResponse doctor;
    private String symptoms;
    private String diagnosis;
    private String clinicalNotes;
    private Boolean recommendSurgery;
    private String surgeryType;
    private SurgeryUrgency surgeryUrgency;
    private Boolean requireLabTests;
    private String labTestsNeeded;
    private AnalysisStatus status;
    private LocalDateTime createdAt;
}