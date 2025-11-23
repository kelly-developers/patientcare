package com.example.patientcare.controller;

import com.example.patientcare.dto.request.LoginRequest;
import com.example.patientcare.dto.request.RefreshTokenRequest;
import com.example.patientcare.dto.request.SignupRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.AuthResponse;
import com.example.patientcare.dto.response.TokenRefreshResponse;
import com.example.patientcare.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/auth", "/auth"}) // Handle both paths
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest request) {
        logger.info("Signup request for user: {}", request.getUsername());
        try {
            AuthResponse response = authService.signup(request);
            logger.info("Signup successful for user: {}", request.getUsername());
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("User registered successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            logger.error("Signup failed for user: {}", request.getUsername(), e);
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Login request for user: {}", request.getUsername());
        try {
            AuthResponse response = authService.login(request);
            logger.info("Login successful for user: {}", request.getUsername());
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .data(response)
                    .build());
        } catch (Exception e) {
            logger.error("Login failed for user: {}", request.getUsername(), e);
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .build());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        logger.info("Token refresh request");
        try {
            TokenRefreshResponse response = authService.refreshToken(request);
            logger.info("Token refresh successful");
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Token refreshed successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            logger.error("Token refresh failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("Logout request");
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String refreshToken = authHeader.substring(7);
                authService.logout(refreshToken);
            }
            logger.info("Logout successful");
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Logout successful")
                    .build());
        } catch (Exception e) {
            logger.error("Logout failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("Token verification request");
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                boolean isValid = authService.verifyToken(token);
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("Token verification completed")
                        .data(isValid)
                        .build());
            } else {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No token provided")
                        .data(false)
                        .build());
            }
        } catch (Exception e) {
            logger.error("Token verification failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> healthCheck() {
        logger.info("Health check request");
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Authentication service is running")
                .data("OK")
                .build());
    }
}