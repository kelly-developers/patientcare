package com.example.PatientCareBackend.controller;

import com.example.PatientCareBackend.dto.request.AuthRequest;
import com.example.PatientCareBackend.dto.request.RegisterRequest;
import com.example.PatientCareBackend.dto.response.AuthResponse;
import com.example.PatientCareBackend.dto.response.UserResponse;
import com.example.PatientCareBackend.model.User;
import com.example.PatientCareBackend.service.AuthService;
import com.example.PatientCareBackend.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.authenticate(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        User currentUser = jwtService.getCurrentUser();
        UserResponse response = new UserResponse(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getFirstName(),
                currentUser.getLastName(),
                currentUser.getRole(),
                currentUser.getSpecialty(),
                currentUser.getAvailable(),
                currentUser.getCreatedAt(),
                currentUser.getUpdatedAt()
        );
        return ResponseEntity.ok(response);
    }
}