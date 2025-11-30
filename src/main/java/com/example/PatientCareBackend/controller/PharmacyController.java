package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.PharmacyRequest;
import com.example.PatientCareBackend.dto.response.PharmacyResponse;
import com.example.PatientCareBackend.model.Pharmacy;
import com.example.PatientCareBackend.service.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping
    public ResponseEntity<PharmacyResponse> createPrescription(@Valid @RequestBody PharmacyRequest pharmacyRequest) {
        PharmacyResponse prescription = pharmacyService.createPrescription(pharmacyRequest);
        return new ResponseEntity<>(prescription, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PharmacyResponse>> getAllPrescriptions() {
        List<PharmacyResponse> prescriptions = pharmacyService.getAllPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PharmacyResponse>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        List<PharmacyResponse> prescriptions = pharmacyService.getPrescriptionsByPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PharmacyResponse> updatePrescriptionStatus(
            @PathVariable Long id,
            @RequestParam Pharmacy.PrescriptionStatus status) {
        PharmacyResponse prescription = pharmacyService.updatePrescriptionStatus(id, status);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyResponse> getPrescriptionById(@PathVariable Long id) {
        PharmacyResponse prescription = pharmacyService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PharmacyResponse>> getPendingPrescriptions() {
        List<PharmacyResponse> prescriptions = pharmacyService.getPendingPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PharmacyResponse>> getPrescriptionsByDoctor(@PathVariable Long doctorId) {
        List<PharmacyResponse> prescriptions = pharmacyService.getPrescriptionsByDoctor(doctorId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/patient/{patientId}/active")
    public ResponseEntity<List<PharmacyResponse>> getActivePrescriptionsByPatient(@PathVariable Long patientId) {
        List<PharmacyResponse> prescriptions = pharmacyService.getActivePrescriptionsByPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PharmacyResponse>> searchByMedicationName(@RequestParam String medicationName) {
        List<PharmacyResponse> prescriptions = pharmacyService.searchByMedicationName(medicationName);
        return ResponseEntity.ok(prescriptions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        pharmacyService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}