package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.response.PatientResponse;
import com.example.PatientCareBackend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/consent")
@RequiredArgsConstructor
public class ConsentFormController {

    private final PatientService patientService;
    private final String UPLOAD_DIR = "./uploads/consent-forms/";

    @PostMapping("/upload/{patientId}")
    public ResponseEntity<PatientResponse> uploadConsentForm(
            @PathVariable Long patientId,
            @RequestParam("file") MultipartFile file) {

        try {
            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Save file
            Files.copy(file.getInputStream(), filePath);

            // Update patient record with file path
            String filePathString = "/uploads/consent-forms/" + fileName;
            PatientResponse patient = patientService.updateConsentForm(patientId, filePathString);

            return ResponseEntity.ok(patient);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{patientId}")
    public ResponseEntity<byte[]> downloadConsentForm(@PathVariable Long patientId) {
        try {
            PatientResponse patient = patientService.getPatientById(patientId);

            if (patient.getConsentFormPath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get("." + patient.getConsentFormPath());
            byte[] fileContent = Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "attachment; filename=\"consent-form.pdf\"")
                    .body(fileContent);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}