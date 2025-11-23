package com.example.patientcare.service;

import com.example.patientcare.dto.request.UserUpdateRequest;
import com.example.patientcare.dto.response.UserResponse;
import com.example.patientcare.entity.User;
import com.example.patientcare.exception.DuplicateResourceException;
import com.example.patientcare.exception.ResourceNotFoundException;
import com.example.patientcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getCurrentUser(String userId) {
        // Convert String to UUID
        UUID userUuid = UUID.fromString(userId);
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Use the builder pattern instead of constructor
        return mapToUserResponse(user);
    }

    public UserResponse updateUserProfile(String userId, UserUpdateRequest updateRequest) {
        // Convert String to UUID
        UUID userUuid = UUID.fromString(userId);
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if email is being changed and conflicts with another user
        if (updateRequest.getEmail() != null &&
                !updateRequest.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(updateRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + updateRequest.getEmail());
        }

        // Update user fields
        if (updateRequest.getFirstName() != null) {
            user.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            user.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPhone() != null) {
            user.setPhone(updateRequest.getPhone());
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    // Helper method to convert User entity to UserResponse DTO
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}