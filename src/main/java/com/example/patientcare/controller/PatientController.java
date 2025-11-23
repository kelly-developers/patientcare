package com.example.patientcare.controller;

import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPatients() {
        List<PatientResponse> patients = patientService.getAllPatients();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patients retrieved successfully")
                .data(patients)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPatientById(@PathVariable String id) {
        PatientResponse patient = patientService.getPatientById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient retrieved successfully")
                .data(patient)
                .build());
    }

    @GetMapping("/patient-id/{patientId}")
    public ResponseEntity<ApiResponse> getPatientByPatientId(@PathVariable String patientId) {
        PatientResponse patient = patientService.getPatientByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient retrieved successfully")
                .data(patient)
                .build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> createPatient(@Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.createPatient(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient created successfully")
                .data(patient)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> updatePatient(@PathVariable String id, @Valid @RequestBody PatientRequest request) {
        PatientResponse patient = patientService.updatePatient(id, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient updated successfully")
                .data(patient)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient deleted successfully")
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchPatients(@RequestParam String q) {
        List<PatientResponse> patients = patientService.searchPatients(q);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patients search completed")
                .data(patients)
                .build());
    }

    @PatchMapping("/{id}/consent")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> updateResearchConsent(@PathVariable String id, @RequestParam Boolean consent) {
        PatientResponse patient = patientService.updateResearchConsent(id, consent);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Research consent updated successfully")
                .data(patient)
                .build());
    }

    @GetMapping("/consented")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getPatientsWithResearchConsent() {
        List<PatientResponse> patients = patientService.getPatientsWithResearchConsent();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patients with research consent retrieved successfully")
                .data(patients)
                .build());
    }
}