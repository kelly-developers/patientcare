package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.PreOperativeRequest;
import com.example.PatientCareBackend.dto.response.PreOperativeResponse;
import com.example.PatientCareBackend.service.PreOperativeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preoperative")
@RequiredArgsConstructor
public class PreOperativeController {

    private final PreOperativeService preOperativeService;

    @PostMapping
    public ResponseEntity<PreOperativeResponse> submitChecklist(@Valid @RequestBody PreOperativeRequest checklistRequest) {
        PreOperativeResponse checklist = preOperativeService.submitChecklist(checklistRequest);
        return new ResponseEntity<>(checklist, HttpStatus.CREATED);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PreOperativeResponse> getChecklistByPatient(@PathVariable Long patientId) {
        PreOperativeResponse checklist = preOperativeService.getChecklistByPatient(patientId);
        return ResponseEntity.ok(checklist);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreOperativeResponse> getChecklistById(@PathVariable Long id) {
        PreOperativeResponse checklist = preOperativeService.getChecklistById(id);
        return ResponseEntity.ok(checklist);
    }

    @GetMapping
    public ResponseEntity<List<PreOperativeResponse>> getAllChecklists() {
        List<PreOperativeResponse> checklists = preOperativeService.getAllChecklists();
        return ResponseEntity.ok(checklists);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreOperativeResponse> updateChecklist(
            @PathVariable Long id,
            @Valid @RequestBody PreOperativeRequest checklistRequest) {
        PreOperativeResponse updatedChecklist = preOperativeService.updateChecklist(id, checklistRequest);
        return ResponseEntity.ok(updatedChecklist);
    }

    @GetMapping("/patient/{patientId}/complete")
    public ResponseEntity<Boolean> isChecklistComplete(@PathVariable Long patientId) {
        boolean isComplete = preOperativeService.isChecklistComplete(patientId);
        return ResponseEntity.ok(isComplete);
    }
}