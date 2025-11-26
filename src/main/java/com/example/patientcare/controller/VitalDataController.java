package com.example.patientcare.controller;

import com.example.patientcare.dto.request.VitalDataRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.VitalDataResponse;
import com.example.patientcare.service.VitalDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vital-data")
@RequiredArgsConstructor
public class VitalDataController {

    private final VitalDataService vitalDataService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> recordVitalData(@Valid @RequestBody VitalDataRequest request, Authentication authentication) {
        String recordedByUserId = authentication.getName(); // Get current user ID
        VitalDataResponse vitalData = vitalDataService.recordVitalData(request, recordedByUserId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Vital data recorded successfully")
                .data(vitalData)
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getVitalDataById(@PathVariable String id) {
        VitalDataResponse vitalData = vitalDataService.getVitalDataById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Vital data retrieved successfully")
                .data(vitalData)
                .build());
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getVitalDataByPatientId(@PathVariable String patientId) {
        List<VitalDataResponse> vitalDataList = vitalDataService.getVitalDataByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient vital data retrieved successfully")
                .data(vitalDataList)
                .build());
    }

    @GetMapping("/patient/{patientId}/paginated")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getVitalDataByPatientIdPaginated(@PathVariable String patientId, Pageable pageable) {
        Page<VitalDataResponse> vitalDataPage = vitalDataService.getVitalDataByPatientIdPaginated(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient vital data retrieved successfully")
                .data(vitalDataPage)
                .build());
    }

    @GetMapping("/patient/{patientId}/latest")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getLatestVitalDataByPatientId(@PathVariable String patientId) {
        VitalDataResponse vitalData = vitalDataService.getLatestVitalDataByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Latest vital data retrieved successfully")
                .data(vitalData)
                .build());
    }

    @GetMapping("/patient/{patientId}/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getVitalDataCountByPatientId(@PathVariable String patientId) {
        Long count = vitalDataService.getVitalDataCountByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Vital data count retrieved successfully")
                .data(count)
                .build());
    }

    @GetMapping("/recorded-by-me")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getVitalDataRecordedByCurrentUser(Authentication authentication) {
        String recordedByUserId = authentication.getName();
        List<VitalDataResponse> vitalDataList = vitalDataService.getVitalDataByRecordedBy(recordedByUserId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Vital data recorded by current user retrieved successfully")
                .data(vitalDataList)
                .build());
    }

    @GetMapping("/patient/{patientId}/date-range")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getVitalDataByDateRange(
            @PathVariable String patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<VitalDataResponse> vitalDataList = vitalDataService.getVitalDataByPatientAndDateRange(patientId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Vital data for date range retrieved successfully")
                .data(vitalDataList)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse> deleteVitalData(@PathVariable String id) {
        vitalDataService.deleteVitalData(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Vital data deleted successfully")
                .build());
    }
}