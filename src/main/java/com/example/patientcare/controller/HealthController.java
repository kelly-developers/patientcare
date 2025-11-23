package com.example.patientcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.OPTIONS})
public class HealthController {

    @GetMapping
    public ResponseEntity<HealthResponse> healthCheck() {
        return ResponseEntity.ok(new HealthResponse("OK", "Server is running"));
    }

    @GetMapping("/detailed")
    public ResponseEntity<DetailedHealthResponse> detailedHealthCheck() {
        return ResponseEntity.ok(new DetailedHealthResponse(
                "OK",
                "PatientCare Backend Service",
                System.currentTimeMillis(),
                "1.0.0"
        ));
    }

    // Handle OPTIONS requests for preflight
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok().build();
    }

    public static class HealthResponse {
        private String status;
        private String message;

        public HealthResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class DetailedHealthResponse {
        private String status;
        private String service;
        private long timestamp;
        private String version;

        public DetailedHealthResponse(String status, String service, long timestamp, String version) {
            this.status = status;
            this.service = service;
            this.timestamp = timestamp;
            this.version = version;
        }

        // Getters and setters
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getService() { return service; }
        public void setService(String service) { this.service = service; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }
}