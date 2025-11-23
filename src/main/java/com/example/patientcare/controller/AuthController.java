package com.example.patientcare.controller;

import com.example.patientcare.dto.request.LoginRequest;
import com.example.patientcare.dto.request.RefreshTokenRequest;
import com.example.patientcare.dto.request.SignupRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.AuthResponse;
import com.example.patientcare.dto.response.TokenRefreshResponse;
import com.example.patientcare.exception.DuplicateResourceException;
import com.example.patientcare.exception.UnauthorizedException;
import com.example.patientcare.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) {
        try {
            logger.info("Signup request received for username: {}", signUpRequest.getUsername());

            // Check for validation errors
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                logger.warn("Validation errors during signup: {}", errors);
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Validation failed", errors));
            }

            AuthResponse response = authService.register(signUpRequest);
            logger.info("Signup successful for username: {}", signUpRequest.getUsername());
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", response));

        } catch (DuplicateResourceException e) {
            logger.warn("Duplicate resource during signup: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid argument during signup: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Signup failed for {}: {}", signUpRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during signup for {}: {}", signUpRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal server error during registration. Please try again."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        try {
            logger.info("Login request received for username: {}", loginRequest.getUsername());

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Validation failed", errors));
            }

            AuthResponse response = authService.login(loginRequest);
            logger.info("Login successful for username: {}", loginRequest.getUsername());
            return ResponseEntity.ok(new ApiResponse(true, "Login successful", response));

        } catch (UnauthorizedException e) {
            logger.warn("Unauthorized login attempt for {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Login failed for {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during login for {}: {}", loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal server error during login. Please try again."));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request, BindingResult bindingResult) {
        try {
            logger.info("Token refresh request received");

            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Invalid refresh token"));
            }

            TokenRefreshResponse response = authService.refreshToken(request);
            return ResponseEntity.ok(new ApiResponse(true, "Token refreshed successfully", response));

        } catch (UnauthorizedException e) {
            logger.warn("Unauthorized token refresh: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Token refresh failed: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during token refresh: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal server error during token refresh. Please try again."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        try {
            authService.logout();
            return ResponseEntity.ok(new ApiResponse(true, "Logout successful"));
        } catch (Exception e) {
            logger.error("Logout error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Internal server error during logout"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "Invalid authorization header"));
            }

            String token = authorizationHeader.substring(7);
            boolean isValid = authService.verifyToken(token);

            if (isValid) {
                return ResponseEntity.ok(new ApiResponse(true, "Token is valid"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "Token is invalid or expired"));
            }
        } catch (Exception e) {
            logger.error("Token verification error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Token verification failed"));
        }
    }

    // Add a health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("timestamp", java.time.LocalDateTime.now());
            health.put("service", "Auth Service");
            return ResponseEntity.ok(new ApiResponse(true, "Service is healthy", health));
        } catch (Exception e) {
            logger.error("Health check failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Service is unhealthy"));
        }
    }
}