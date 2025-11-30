package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.VitalDataRequest;
import com.example.PatientCareBackend.dto.response.VitalDataResponse;
import com.example.PatientCareBackend.service.VitalDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vital-data")
@RequiredArgsConstructor
public class VitalDataController {

    private final VitalDataService vitalDataService;

    @PostMapping
    public ResponseEntity<VitalDataResponse> recordVitalData(@Valid @RequestBody VitalDataRequest vitalDataRequest) {
        VitalDataResponse vitalData = vitalDataService.recordVitalData(vitalDataRequest);
        return new ResponseEntity<>(vitalData, HttpStatus.CREATED);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VitalDataResponse>> getPatientVitals(@PathVariable Long patientId) {
        List<VitalDataResponse> vitalData = vitalDataService.getPatientVitals(patientId);
        return ResponseEntity.ok(vitalData);
    }

    @GetMapping("/recorded-by-me")
    public ResponseEntity<List<VitalDataResponse>> getVitalsRecordedByCurrentUser() {
        List<VitalDataResponse> vitalData = vitalDataService.getVitalsRecordedByCurrentUser();
        return ResponseEntity.ok(vitalData);
    }

    @GetMapping("/critical")
    public ResponseEntity<List<VitalDataResponse>> getCriticalVitals() {
        List<VitalDataResponse> vitalData = vitalDataService.getCriticalVitals();
        return ResponseEntity.ok(vitalData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VitalDataResponse> getVitalDataById(@PathVariable Long id) {
        VitalDataResponse vitalData = vitalDataService.getVitalDataById(id);
        return ResponseEntity.ok(vitalData);
    }

    @GetMapping("/patient/{patientId}/recent")
    public ResponseEntity<List<VitalDataResponse>> getRecentVitals(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "24") int hours) {
        List<VitalDataResponse> vitalData = vitalDataService.getRecentVitals(patientId, hours);
        return ResponseEntity.ok(vitalData);
    }

    @GetMapping("/patient/{patientId}/latest")
    public ResponseEntity<List<VitalDataResponse>> getLatestVitals(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "10") int limit) {
        List<VitalDataResponse> vitalData = vitalDataService.getLatestVitals(patientId, limit);
        return ResponseEntity.ok(vitalData);
    }
}