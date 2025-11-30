package com.example.PatientCareBackend.dto.response;

import com.example.PatientCareBackend.model.User.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String specialty;
    private Boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}