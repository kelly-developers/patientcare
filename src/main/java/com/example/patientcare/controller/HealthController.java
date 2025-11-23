package com.example.patientcare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api/health", "/health"})
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("Health check endpoint called");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "PatientCare Backend");
        response.put("timestamp", System.currentTimeMillis());
        response.put("version", "1.0.0");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealthCheck() {
        logger.info("Detailed health check endpoint called");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "PatientCare Backend");
        response.put("timestamp", System.currentTimeMillis());
        response.put("version", "1.0.0");

        // System information
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("availableProcessors", runtime.availableProcessors());
        systemInfo.put("freeMemory", runtime.freeMemory());
        systemInfo.put("maxMemory", runtime.maxMemory());
        systemInfo.put("totalMemory", runtime.totalMemory());

        response.put("system", systemInfo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}