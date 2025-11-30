package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.ConsentRequest;
import com.example.PatientCareBackend.dto.response.ConsentResponse;
import com.example.PatientCareBackend.model.Consent;
import com.example.PatientCareBackend.service.ConsentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/consent")
@RequiredArgsConstructor
public class ConsentController {

    private final ConsentService consentService;

    @PostMapping
    public ResponseEntity<ConsentResponse> submitConsent(@Valid @RequestBody ConsentRequest consentRequest) {
        ConsentResponse consent = consentService.submitConsent(consentRequest);
        return new ResponseEntity<>(consent, HttpStatus.CREATED);
    }

    @GetMapping("/surgery/{surgeryId}")
    public ResponseEntity<ConsentResponse> getConsentBySurgery(@PathVariable Long surgeryId) {
        ConsentResponse consent = consentService.getConsentBySurgery(surgeryId);
        return ResponseEntity.ok(consent);
    }

    @PostMapping("/upload/{consentId}")
    public ResponseEntity<ConsentResponse> uploadConsentFile(
            @PathVariable Long consentId,
            @RequestParam("file") MultipartFile file) {
        ConsentResponse consent = consentService.uploadConsentFile(consentId, file);
        return ResponseEntity.ok(consent);
    }

    @GetMapping("/stored")
    public ResponseEntity<List<ConsentResponse>> getStoredConsentForms() {
        List<ConsentResponse> consents = consentService.getStoredConsentForms();
        return ResponseEntity.ok(consents);
    }

    @GetMapping("/surgery/{surgeryId}/has-valid")
    public ResponseEntity<Boolean> hasValidConsent(@PathVariable Long surgeryId) {
        boolean hasValidConsent = consentService.hasValidConsent(surgeryId);
        return ResponseEntity.ok(hasValidConsent);
    }

    @GetMapping("/decision/{decision}")
    public ResponseEntity<List<ConsentResponse>> getConsentsByDecision(@PathVariable Consent.ConsentDecision decision) {
        List<ConsentResponse> consents = consentService.getConsentsByDecision(decision);
        return ResponseEntity.ok(consents);
    }
}