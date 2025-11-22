package com.example.patientcare.controller;

import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.service.PatientOnboardingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
public class PatientOnboardingController {

    @Autowired
    private PatientOnboardingService onboardingService;

    @PostMapping("/patients")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<?> onboardPatient(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patient = onboardingService.onboardNewPatient(patientRequest);
        return ResponseEntity.ok(new ApiResponse(true, "Patient onboarded successfully", patient));
    }

    @GetMapping("/patients/{patientId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<?> getOnboardingStatus(@PathVariable String patientId) {
        PatientResponse patient = onboardingService.getOnboardingStatus(patientId);
        return ResponseEntity.ok(new ApiResponse(true, "Onboarding status retrieved", patient));
    }
}