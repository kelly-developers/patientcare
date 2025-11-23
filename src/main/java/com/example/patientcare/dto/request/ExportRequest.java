package com.example.patientcare.dto.request;

import java.util.List;

public class ExportRequest {
    private List<String> patientIds;

    // Constructors
    public ExportRequest() {}

    public ExportRequest(List<String> patientIds) {
        this.patientIds = patientIds;
    }

    // Getters and Setters
    public List<String> getPatientIds() { return patientIds; }
    public void setPatientIds(List<String> patientIds) { this.patientIds = patientIds; }
}