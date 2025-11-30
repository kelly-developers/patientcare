package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.SurgicalDecisionRequest;
import com.example.PatientCareBackend.dto.response.SurgicalDecisionResponse;
import com.example.PatientCareBackend.service.SurgicalDecisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/surgical-decisions")
@RequiredArgsConstructor
public class SurgicalDecisionController {

    private final SurgicalDecisionService surgicalDecisionService;

    @PostMapping
    public ResponseEntity<SurgicalDecisionResponse> submitDecision(@Valid @RequestBody SurgicalDecisionRequest decisionRequest) {
        SurgicalDecisionResponse decision = surgicalDecisionService.submitDecision(decisionRequest);
        return new ResponseEntity<>(decision, HttpStatus.CREATED);
    }

    @GetMapping("/surgery/{surgeryId}")
    public ResponseEntity<List<SurgicalDecisionResponse>> getDecisionsBySurgery(@PathVariable Long surgeryId) {
        List<SurgicalDecisionResponse> decisions = surgicalDecisionService.getDecisionsBySurgery(surgeryId);
        return ResponseEntity.ok(decisions);
    }

    @GetMapping("/consensus/{surgeryId}")
    public ResponseEntity<Map<String, Object>> getDecisionConsensus(@PathVariable Long surgeryId) {
        Map<String, Object> consensus = surgicalDecisionService.getDecisionConsensus(surgeryId);
        return ResponseEntity.ok(consensus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurgicalDecisionResponse> getDecisionById(@PathVariable Long id) {
        SurgicalDecisionResponse decision = surgicalDecisionService.getDecisionById(id);
        return ResponseEntity.ok(decision);
    }

    @GetMapping("/surgeon/{surgeonName}")
    public ResponseEntity<List<SurgicalDecisionResponse>> getDecisionsBySurgeon(@PathVariable String surgeonName) {
        List<SurgicalDecisionResponse> decisions = surgicalDecisionService.getDecisionsBySurgeon(surgeonName);
        return ResponseEntity.ok(decisions);
    }

    @GetMapping("/{surgeryId}/has-consensus")
    public ResponseEntity<Boolean> hasConsensusForSurgery(@PathVariable Long surgeryId) {
        boolean hasConsensus = surgicalDecisionService.hasConsensusForSurgery(surgeryId);
        return ResponseEntity.ok(hasConsensus);
    }
}