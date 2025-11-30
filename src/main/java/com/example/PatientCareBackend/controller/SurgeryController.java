package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.SurgeryRequest;
import com.example.PatientCareBackend.dto.response.SurgeryResponse;
import com.example.PatientCareBackend.model.Surgery;
import com.example.PatientCareBackend.service.SurgeryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/surgeries")
@RequiredArgsConstructor
public class SurgeryController {

    private final SurgeryService surgeryService;

    @GetMapping
    public ResponseEntity<List<SurgeryResponse>> getAllSurgeries() {
        List<SurgeryResponse> surgeries = surgeryService.getAllSurgeries();
        return ResponseEntity.ok(surgeries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurgeryResponse> getSurgeryById(@PathVariable Long id) {
        SurgeryResponse surgery = surgeryService.getSurgeryById(id);
        return ResponseEntity.ok(surgery);
    }

    @PostMapping
    public ResponseEntity<SurgeryResponse> createSurgery(@Valid @RequestBody SurgeryRequest surgeryRequest) {
        SurgeryResponse createdSurgery = surgeryService.createSurgery(surgeryRequest);
        return new ResponseEntity<>(createdSurgery, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SurgeryResponse> updateSurgery(
            @PathVariable Long id,
            @Valid @RequestBody SurgeryRequest surgeryRequest) {
        SurgeryResponse updatedSurgery = surgeryService.updateSurgery(id, surgeryRequest);
        return ResponseEntity.ok(updatedSurgery);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurgery(@PathVariable Long id) {
        surgeryService.deleteSurgery(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SurgeryResponse> updateSurgeryStatus(
            @PathVariable Long id,
            @RequestParam Surgery.SurgeryStatus status) {
        SurgeryResponse updatedSurgery = surgeryService.updateSurgeryStatus(id, status);
        return ResponseEntity.ok(updatedSurgery);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<SurgeryResponse>> getSurgeriesByPatient(@PathVariable Long patientId) {
        List<SurgeryResponse> surgeries = surgeryService.getSurgeriesByPatient(patientId);
        return ResponseEntity.ok(surgeries);
    }

    @GetMapping("/pending-consent")
    public ResponseEntity<List<SurgeryResponse>> getPendingConsentSurgeries() {
        List<SurgeryResponse> surgeries = surgeryService.getPendingConsentSurgeries();
        return ResponseEntity.ok(surgeries);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SurgeryResponse>> getSurgeriesByStatus(@PathVariable Surgery.SurgeryStatus status) {
        List<SurgeryResponse> surgeries = surgeryService.getSurgeriesByStatus(status);
        return ResponseEntity.ok(surgeries);
    }

    @GetMapping("/surgeon/{surgeonName}")
    public ResponseEntity<List<SurgeryResponse>> getSurgeriesBySurgeon(@PathVariable String surgeonName) {
        List<SurgeryResponse> surgeries = surgeryService.getSurgeriesBySurgeon(surgeonName);
        return ResponseEntity.ok(surgeries);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<SurgeryResponse>> getSurgeriesBetweenDates(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<SurgeryResponse> surgeries = surgeryService.getSurgeriesBetweenDates(startDate, endDate);
        return ResponseEntity.ok(surgeries);
    }
}