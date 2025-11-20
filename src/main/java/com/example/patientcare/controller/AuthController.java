package com.example.patientcare.controller;

import com.example.patientcare.dto.request.LoginRequest;
import com.example.patientcare.dto.request.RefreshTokenRequest;
import com.example.patientcare.dto.request.SignupRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.AuthResponse;
import com.example.patientcare.dto.response.TokenRefreshResponse;
import com.example.patientcare.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
// REMOVE THIS: @CrossOrigin(origins = {"https://patientcares.netlify.app", "http://localhost:3000"})
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new ApiResponse(true, "User authenticated successfully", authResponse));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        AuthResponse authResponse = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", authResponse));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenRefreshResponse tokenRefreshResponse = authService.refreshToken(request);
        return ResponseEntity.ok(new ApiResponse(true, "Token refreshed successfully", tokenRefreshResponse));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        authService.logoutUser();
        return ResponseEntity.ok(new ApiResponse(true, "User logged out successfully"));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken() {
        return ResponseEntity.ok(new ApiResponse(true, "Token is valid", null));
    }
}