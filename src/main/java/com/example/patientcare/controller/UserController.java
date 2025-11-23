package com.example.patientcare.controller;

import com.example.patientcare.dto.request.UserUpdateRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.UserResponse;
import com.example.patientcare.security.UserPrincipal;
import com.example.patientcare.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserResponse user = userService.getCurrentUser(userPrincipal.getId());
        return ResponseEntity.ok(new ApiResponse(true, "User profile retrieved successfully", user));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(
            Authentication authentication,
            @Valid @RequestBody UserUpdateRequest updateRequest) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserResponse user = userService.updateUserProfile(userPrincipal.getId(), updateRequest);
        return ResponseEntity.ok(new ApiResponse(true, "User profile updated successfully", user));
    }
}