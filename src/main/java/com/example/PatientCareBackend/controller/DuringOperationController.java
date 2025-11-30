package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.DuringOperationRequest;
import com.example.PatientCareBackend.dto.response.DuringOperationResponse;
import com.example.PatientCareBackend.model.DuringOperation;
import com.example.PatientCareBackend.service.DuringOperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/during-operation")
@RequiredArgsConstructor
public class DuringOperationController {

    private final DuringOperationService duringOperationService;

    @PostMapping
    public ResponseEntity<DuringOperationResponse> startOperation(@Valid @RequestBody DuringOperationRequest operationRequest) {
        DuringOperationResponse operation = duringOperationService.startOperation(operationRequest);
        return new ResponseEntity<>(operation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DuringOperationResponse> updateOperation(
            @PathVariable Long id,
            @Valid @RequestBody DuringOperationRequest operationRequest) {
        DuringOperationResponse operation = duringOperationService.updateOperation(id, operationRequest);
        return ResponseEntity.ok(operation);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<DuringOperationResponse> completeOperation(@PathVariable Long id) {
        DuringOperationResponse operation = duringOperationService.completeOperation(id);
        return ResponseEntity.ok(operation);
    }

    @PostMapping("/{id}/vitals")
    public ResponseEntity<DuringOperationResponse> updateVitals(
            @PathVariable Long id,
            @RequestBody Map<String, Object> vitals) {
        DuringOperationResponse operation = duringOperationService.updateVitals(id, vitals);
        return ResponseEntity.ok(operation);
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<DuringOperationResponse> addSurgicalNote(
            @PathVariable Long id,
            @RequestBody String note) {
        DuringOperationResponse operation = duringOperationService.addSurgicalNote(id, note);
        return ResponseEntity.ok(operation);
    }

    @PostMapping("/{id}/complications")
    public ResponseEntity<DuringOperationResponse> addComplication(
            @PathVariable Long id,
            @RequestBody Map<String, Object> complication) {
        DuringOperationResponse operation = duringOperationService.addComplication(id, complication);
        return ResponseEntity.ok(operation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuringOperationResponse> getOperationById(@PathVariable Long id) {
        DuringOperationResponse operation = duringOperationService.getOperationById(id);
        return ResponseEntity.ok(operation);
    }

    @GetMapping("/surgery/{surgeryId}")
    public ResponseEntity<DuringOperationResponse> getOperationBySurgery(@PathVariable Long surgeryId) {
        DuringOperationResponse operation = duringOperationService.getOperationBySurgery(surgeryId);
        return ResponseEntity.ok(operation);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DuringOperationResponse>> getOperationsByStatus(@PathVariable DuringOperation.OperationStatus status) {
        List<DuringOperationResponse> operations = duringOperationService.getOperationsByStatus(status);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DuringOperationResponse>> getActiveOperations() {
        List<DuringOperationResponse> operations = duringOperationService.getActiveOperations();
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/patient/{patientId}/recent")
    public ResponseEntity<List<DuringOperationResponse>> getRecentOperationsByPatient(@PathVariable Long patientId) {
        List<DuringOperationResponse> operations = duringOperationService.getRecentOperationsByPatient(patientId);
        return ResponseEntity.ok(operations);
    }
}