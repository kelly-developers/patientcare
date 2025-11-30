package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.ICURequest;
import com.example.PatientCareBackend.dto.response.ICUResponse;
import com.example.PatientCareBackend.service.ICUService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/icu")
@RequiredArgsConstructor
public class ICUController {

    private final ICUService icuService;

    @PostMapping
    public ResponseEntity<ICUResponse> addICURecord(@Valid @RequestBody ICURequest icuRequest) {
        ICUResponse record = icuService.addICURecord(icuRequest);
        return new ResponseEntity<>(record, HttpStatus.CREATED);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ICUResponse>> getPatientICUData(@PathVariable Long patientId) {
        List<ICUResponse> records = icuService.getPatientICUData(patientId);
        return ResponseEntity.ok(records);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ICUResponse> updateICURecord(
            @PathVariable Long id,
            @Valid @RequestBody ICURequest icuRequest) {
        ICUResponse record = icuService.updateICURecord(id, icuRequest);
        return ResponseEntity.ok(record);
    }

    @PostMapping("/{id}/vitals")
    public ResponseEntity<ICUResponse> updateVitals(
            @PathVariable Long id,
            @RequestBody Map<String, Object> vitals) {
        ICUResponse record = icuService.updateVitals(id, vitals);
        return ResponseEntity.ok(record);
    }

    @PostMapping("/{id}/medications")
    public ResponseEntity<ICUResponse> addMedication(
            @PathVariable Long id,
            @RequestBody Map<String, Object> medication) {
        ICUResponse record = icuService.addMedication(id, medication);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/patient/{patientId}/latest")
    public ResponseEntity<ICUResponse> getLatestRecordByPatient(@PathVariable Long patientId) {
        ICUResponse record = icuService.getLatestRecordByPatient(patientId);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/critical")
    public ResponseEntity<List<ICUResponse>> getCriticalPatients() {
        List<ICUResponse> records = icuService.getCriticalPatients();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/patient/{patientId}/time-range")
    public ResponseEntity<List<ICUResponse>> getPatientRecordsByTimeRange(
            @PathVariable Long patientId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        List<ICUResponse> records = icuService.getPatientRecordsByTimeRange(patientId, startTime, endTime);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/patient/{patientId}/stats")
    public ResponseEntity<Map<String, Object>> getPatientStats(@PathVariable Long patientId) {
        Map<String, Object> stats = icuService.getPatientStats(patientId);
        return ResponseEntity.ok(stats);
    }
}