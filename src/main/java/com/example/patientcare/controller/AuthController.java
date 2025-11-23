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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest request) {
        try {
            AuthResponse response = authService.signup(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("User registered successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            TokenRefreshResponse response = authService.refreshToken(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Token refreshed successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String refreshToken = authHeader.substring(7); // Remove "Bearer " prefix
            authService.logout(refreshToken);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Logout successful")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            boolean isValid = authService.verifyToken(token);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Token verification completed")
                    .data(isValid)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> healthCheck() {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Authentication service is running")
                .data("OK")
                .build());
    }
}