package com.example.patientcare.controller;

import com.example.patientcare.dto.request.ExportRequest;
import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<?> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1]) ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Page<PatientResponse> patients = patientService.getAllPatients(pageable);
        return ResponseEntity.ok(new ApiResponse(true, "Patients retrieved successfully", patients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable String id) {
        PatientResponse patient = patientService.getPatientById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Patient retrieved successfully", patient));
    }

    @GetMapping("/patient-id/{patientId}")
    public ResponseEntity<?> getPatientByPatientId(@PathVariable String patientId) {
        PatientResponse patient = patientService.getPatientByPatientId(patientId);
        return ResponseEntity.ok(new ApiResponse(true, "Patient retrieved successfully", patient));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patient = patientService.createPatient(patientRequest);
        return ResponseEntity.status(201).body(new ApiResponse(true, "Patient created successfully", patient));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<?> updatePatient(@PathVariable String id, @Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patient = patientService.updatePatient(id, patientRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Patient updated successfully", patient));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<?> searchPatients(@RequestParam String q) {
        List<PatientResponse> patients = patientService.searchPatients(q);
        return ResponseEntity.ok(new ApiResponse(true, "Patients search completed", patients));
    }

    @PatchMapping("/{id}/consent")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<?> updateResearchConsent(@PathVariable String id, @RequestBody Boolean researchConsent) {
        PatientResponse patient = patientService.updateResearchConsent(id, researchConsent);
        return ResponseEntity.ok(new ApiResponse(true, "Research consent updated successfully", patient));
    }

    @PostMapping("/export/excel")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<?> exportPatientsToExcel(@RequestBody ExportRequest exportRequest) {
        // This would be implemented with Excel export logic
        // For now, return the patients data
        List<PatientResponse> patients;
        if (exportRequest.getPatientIds() != null && !exportRequest.getPatientIds().isEmpty()) {
            patients = patientService.getPatientsByIds(exportRequest.getPatientIds());
        } else {
            patients = patientService.getPatientsWithConsent();
        }

        return ResponseEntity.ok(new ApiResponse(true, "Export data prepared", patients));
    }

    @PostMapping("/export/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<?> exportPatientsToPdf(@RequestBody ExportRequest exportRequest) {
        // This would be implemented with PDF export logic
        // For now, return the patients data
        List<PatientResponse> patients;
        if (exportRequest.getPatientIds() != null && !exportRequest.getPatientIds().isEmpty()) {
            patients = patientService.getPatientsByIds(exportRequest.getPatientIds());
        } else {
            patients = patientService.getPatientsWithConsent();
        }

        return ResponseEntity.ok(new ApiResponse(true, "Export data prepared", patients));
    }
}