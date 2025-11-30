package com.example.PatientCareBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Patient Care Backend API");
        response.put("status", "running");
        response.put("timestamp", java.time.Instant.now().toString());
        response.put("docs", "/swagger-ui.html");
        response.put("health", "/api/health");
        return ResponseEntity.ok(response);
    }
}