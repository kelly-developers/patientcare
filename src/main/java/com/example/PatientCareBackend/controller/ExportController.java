package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/patients")
    public ResponseEntity<InputStreamResource> exportPatients(
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {

        ByteArrayInputStream data = exportService.exportPatients(format);

        String filename = "patients." + format.toLowerCase();
        String contentType = getContentType(format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(data));
    }

    @GetMapping("/surgeries")
    public ResponseEntity<InputStreamResource> exportSurgeries(
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {

        ByteArrayInputStream data = exportService.exportSurgeries(format);

        String filename = "surgeries." + format.toLowerCase();
        String contentType = getContentType(format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(data));
    }

    @GetMapping("/appointments")
    public ResponseEntity<InputStreamResource> exportAppointments(
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {

        ByteArrayInputStream data = exportService.exportAppointments(format);

        String filename = "appointments." + format.toLowerCase();
        String contentType = getContentType(format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(data));
    }

    @GetMapping("/lab-tests")
    public ResponseEntity<InputStreamResource> exportLabTests(@RequestParam(defaultValue = "csv") String format) {
        ByteArrayInputStream data = exportService.exportLabTests(format);

        String filename = "lab_tests." + format.toLowerCase();
        String contentType = getContentType(format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(data));
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<InputStreamResource> exportPrescriptions(@RequestParam(defaultValue = "csv") String format) {
        ByteArrayInputStream data = exportService.exportPrescriptions(format);

        String filename = "prescriptions." + format.toLowerCase();
        String contentType = getContentType(format);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(data));
    }

    private String getContentType(String format) {
        return "csv".equalsIgnoreCase(format) ? "text/csv" : "application/json";
    }
}