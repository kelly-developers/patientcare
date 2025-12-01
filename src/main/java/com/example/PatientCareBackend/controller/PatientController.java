package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.PatientRequest;
import com.example.PatientCareBackend.dto.response.PatientResponse;
import com.example.PatientCareBackend.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        List<PatientResponse> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        PatientResponse patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/patient-id/{patientId}")
    public ResponseEntity<PatientResponse> getPatientByPatientId(@PathVariable String patientId) {
        PatientResponse patient = patientService.getPatientByPatientId(patientId);
        return ResponseEntity.ok(patient);
    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse createdPatient = patientService.createPatient(patientRequest);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse updatedPatient = patientService.updatePatient(id, patientRequest);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientResponse>> searchPatients(@RequestParam String query) {
        List<PatientResponse> patients = patientService.searchPatients(query);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/research-consent")
    public ResponseEntity<List<PatientResponse>> getPatientsWithResearchConsent() {
        // Change from getPatientsWithResearchConsent() to getPatientsWithConsent()
        List<PatientResponse> patients = patientService.getPatientsWithConsent();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalPatients() {
        long count = patientService.getTotalPatients();
        return ResponseEntity.ok(count);
    }
}