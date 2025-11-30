package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.LabTestRequest;
import com.example.PatientCareBackend.dto.response.LabTestResponse;
import com.example.PatientCareBackend.model.LabTest;
import com.example.PatientCareBackend.service.LabTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lab-tests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestService labTestService;

    @PostMapping
    public ResponseEntity<LabTestResponse> orderLabTest(@Valid @RequestBody LabTestRequest labTestRequest) {
        LabTestResponse labTest = labTestService.orderLabTest(labTestRequest);
        return new ResponseEntity<>(labTest, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LabTestResponse>> getAllLabTests() {
        List<LabTestResponse> labTests = labTestService.getAllLabTests();
        return ResponseEntity.ok(labTests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabTestResponse> getLabTestById(@PathVariable Long id) {
        LabTestResponse labTest = labTestService.getLabTestById(id);
        return ResponseEntity.ok(labTest);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<LabTestResponse> updateTestStatus(
            @PathVariable Long id,
            @RequestParam LabTest.TestStatus status,
            @RequestParam(required = false) String results) {
        LabTestResponse labTest = labTestService.updateTestStatus(id, status, results);
        return ResponseEntity.ok(labTest);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabTestResponse>> getLabTestsByPatient(@PathVariable Long patientId) {
        List<LabTestResponse> labTests = labTestService.getLabTestsByPatient(patientId);
        return ResponseEntity.ok(labTests);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LabTestResponse>> getLabTestsByStatus(@PathVariable LabTest.TestStatus status) {
        List<LabTestResponse> labTests = labTestService.getLabTestsByStatus(status);
        return ResponseEntity.ok(labTests);
    }

    @GetMapping("/urgent")
    public ResponseEntity<List<LabTestResponse>> getUrgentLabTests() {
        List<LabTestResponse> labTests = labTestService.getUrgentLabTests();
        return ResponseEntity.ok(labTests);
    }

    @GetMapping("/type/{testType}")
    public ResponseEntity<List<LabTestResponse>> getLabTestsByType(@PathVariable String testType) {
        List<LabTestResponse> labTests = labTestService.getLabTestsByType(testType);
        return ResponseEntity.ok(labTests);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<LabTestResponse>> getLabTestsOrderedBetween(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<LabTestResponse> labTests = labTestService.getLabTestsOrderedBetween(startDate, endDate);
        return ResponseEntity.ok(labTests);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabTestResponse> updateLabTest(
            @PathVariable Long id,
            @Valid @RequestBody LabTestRequest labTestRequest) {
        LabTestResponse labTest = labTestService.updateLabTest(id, labTestRequest);
        return ResponseEntity.ok(labTest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabTest(@PathVariable Long id) {
        labTestService.deleteLabTest(id);
        return ResponseEntity.noContent().build();
    }
}