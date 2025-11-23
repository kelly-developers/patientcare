package com.example.patientcare.controller;

import com.example.patientcare.dto.request.ProcedureRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.ProcedureResponse;
import com.example.patientcare.service.ProcedureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procedures")
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProcedures() {
        List<ProcedureResponse> procedures = procedureService.getAllProcedures();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Procedures retrieved successfully")
                .data(procedures)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProcedureById(@PathVariable String id) {
        ProcedureResponse procedure = procedureService.getProcedureById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Procedure retrieved successfully")
                .data(procedure)
                .build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse> getProceduresByPatientId(@PathVariable String patientId) {
        List<ProcedureResponse> procedures = procedureService.getProceduresByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient procedures retrieved successfully")
                .data(procedures)
                .build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse> getProceduresByStatus(@PathVariable String status) {
        List<ProcedureResponse> procedures = procedureService.getProceduresByStatus(status);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Procedures by status retrieved successfully")
                .data(procedures)
                .build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse> createProcedure(@Valid @RequestBody ProcedureRequest request) {
        ProcedureResponse procedure = procedureService.createProcedure(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Procedure created successfully")
                .data(procedure)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse> updateProcedure(@PathVariable String id, @Valid @RequestBody ProcedureRequest request) {
        ProcedureResponse procedure = procedureService.updateProcedure(id, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Procedure updated successfully")
                .data(procedure)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProcedure(@PathVariable String id) {
        procedureService.deleteProcedure(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Procedure deleted successfully")
                .build());
    }
}