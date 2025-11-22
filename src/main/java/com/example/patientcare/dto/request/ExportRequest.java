package com.example.patientcare.dto.request;

import java.util.List;

public class ExportRequest {
    private List<Long> patientIds;

    // Constructors
    public ExportRequest() {}

    public ExportRequest(List<Long> patientIds) {
        this.patientIds = patientIds;
    }

    // Getters and Setters
    public List<Long> getPatientIds() { return patientIds; }
    public void setPatientIds(List<Long> patientIds) { this.patientIds = patientIds; }
}