package com.example.patientcare.controller;

import com.example.patientcare.dto.request.LoginRequest;
import com.example.patientcare.dto.request.SignupRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.AuthResponse;
import com.example.patientcare.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) {
        try {
            logger.info("Signup request received for username: {}", signUpRequest.getUsername());

            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Validation failed: " + bindingResult.getFieldError().getDefaultMessage()));
            }

            AuthResponse response = authService.register(signUpRequest);
            logger.info("Signup successful for username: {}", signUpRequest.getUsername());
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", response));

        } catch (RuntimeException e) {
            logger.error("Signup failed for {}: {}", signUpRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during signup for {}: {}", signUpRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal server error during registration"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        try {
            logger.info("Login request received for username: {}", loginRequest.getUsername());

            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Validation failed: " + bindingResult.getFieldError().getDefaultMessage()));
            }

            AuthResponse response = authService.login(loginRequest);
            logger.info("Login successful for username: {}", loginRequest.getUsername());
            return ResponseEntity.ok(new ApiResponse(true, "Login successful", response));

        } catch (RuntimeException e) {
            logger.error("Login failed for {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during login for {}: {}", loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal server error during login"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            return ResponseEntity.ok(new ApiResponse(true, "Auth service is healthy"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Auth service is unhealthy"));
        }
    }
}