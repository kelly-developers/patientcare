package com.example.PatientCareBackend.dto.request;

import com.example.PatientCareBackend.model.DoctorAnalysis.AnalysisStatus;
import com.example.PatientCareBackend.model.DoctorAnalysis.SurgeryUrgency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAnalysisRequest {
    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotBlank
    private String symptoms;

    @NotBlank
    private String diagnosis;

    private String clinicalNotes;

    private Boolean recommendSurgery = false;

    private String surgeryType;

    private SurgeryUrgency surgeryUrgency;

    private Boolean requireLabTests = false;

    private String labTestsNeeded;

    @NotNull
    private AnalysisStatus status;
}