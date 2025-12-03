package com.example.PatientCareBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAnalysisRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotBlank(message = "Symptoms are required")
    private String symptoms;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 255, message = "Diagnosis must be less than 255 characters")
    private String diagnosis;

    private String clinicalNotes;

    private Boolean recommendSurgery = false;

    @Size(max = 100, message = "Surgery type must be less than 100 characters")
    private String surgeryType;

    private String surgeryUrgency;

    private Boolean requireLabTests = false;

    private String labTestsNeeded;

    private String status = "COMPLETED";
}