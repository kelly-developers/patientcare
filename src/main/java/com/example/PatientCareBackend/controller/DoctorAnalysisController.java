package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.DoctorAnalysisRequest;
import com.example.PatientCareBackend.dto.response.DoctorAnalysisResponse;
import com.example.PatientCareBackend.service.DoctorAnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class DoctorAnalysisController {

    private final DoctorAnalysisService doctorAnalysisService;

    @PostMapping
    public ResponseEntity<DoctorAnalysisResponse> createAnalysis(@Valid @RequestBody DoctorAnalysisRequest analysisRequest) {
        DoctorAnalysisResponse createdAnalysis = doctorAnalysisService.createAnalysis(analysisRequest);
        return new ResponseEntity<>(createdAnalysis, HttpStatus.CREATED);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<DoctorAnalysisResponse>> getAnalysesByPatient(@PathVariable Long patientId) {
        List<DoctorAnalysisResponse> analyses = doctorAnalysisService.getAnalysesByPatient(patientId);
        return ResponseEntity.ok(analyses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorAnalysisResponse> getAnalysisById(@PathVariable Long id) {
        DoctorAnalysisResponse analysis = doctorAnalysisService.getAnalysisById(id);
        return ResponseEntity.ok(analysis);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorAnalysisResponse> updateAnalysis(
            @PathVariable Long id,
            @Valid @RequestBody DoctorAnalysisRequest analysisRequest) {
        DoctorAnalysisResponse updatedAnalysis = doctorAnalysisService.updateAnalysis(id, analysisRequest);
        return ResponseEntity.ok(updatedAnalysis);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        doctorAnalysisService.deleteAnalysis(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorAnalysisResponse>> getAnalysesByDoctor(@PathVariable Long doctorId) {
        List<DoctorAnalysisResponse> analyses = doctorAnalysisService.getAnalysesByDoctor(doctorId);
        return ResponseEntity.ok(analyses);
    }

    @GetMapping("/surgery-recommended")
    public ResponseEntity<List<DoctorAnalysisResponse>> getAnalysesRequiringSurgery() {
        List<DoctorAnalysisResponse> analyses = doctorAnalysisService.getAnalysesRequiringSurgery();
        return ResponseEntity.ok(analyses);
    }

    @GetMapping
    public ResponseEntity<List<DoctorAnalysisResponse>> getAllAnalyses() {
        List<DoctorAnalysisResponse> analyses = doctorAnalysisService.getAllAnalyses();
        return ResponseEntity.ok(analyses);
    }
}