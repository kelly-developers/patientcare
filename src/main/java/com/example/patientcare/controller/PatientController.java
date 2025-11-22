package com.example.patientcare.controller;

import com.example.patientcare.dto.request.ConsentUpdateRequest;
import com.example.patientcare.dto.request.ExportRequest;
import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.service.ExportService;
import com.example.patientcare.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ExportService exportService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        PatientResponse patient = patientService.getPatientById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Patient retrieved successfully", patient));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patient = patientService.createPatient(patientRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Patient created successfully", patient));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patient = patientService.updatePatient(id, patientRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Patient updated successfully", patient));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(new ApiResponse(true, "Patient deleted successfully"));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<?> searchPatients(@RequestParam String q) {
        List<PatientResponse> patients = patientService.searchPatients(q);
        return ResponseEntity.ok(new ApiResponse(true, "Patients search completed", patients));
    }

    @PatchMapping("/{id}/consent")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<?> updateConsent(@PathVariable Long id, @Valid @RequestBody ConsentUpdateRequest consentRequest) {
        PatientResponse patient = patientService.updateConsent(id, consentRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Consent updated successfully", patient));
    }

    @PostMapping("/export/excel")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<Resource> exportPatientsToExcel(@RequestBody(required = false) ExportRequest exportRequest) throws IOException {
        List<Long> patientIds = exportRequest != null ? exportRequest.getPatientIds() : null;
        List<Patient> patients = patientService.getPatientsWithConsent(patientIds);

        if (patients.isEmpty()) {
            throw new RuntimeException("No consented patients found for export");
        }

        byte[] excelData = exportService.exportPatientsToExcel(patients);

        ByteArrayResource resource = new ByteArrayResource(excelData);

        String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "patients_export_" + timestamp + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelData.length)
                .body(resource);
    }

    @PostMapping("/export/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<?> exportPatientsToPdf(@RequestBody(required = false) ExportRequest exportRequest) {
        // PDF export implementation would go here
        // For now, return a message indicating it's not implemented
        return ResponseEntity.ok(new ApiResponse(true, "PDF export feature coming soon"));
    }
}