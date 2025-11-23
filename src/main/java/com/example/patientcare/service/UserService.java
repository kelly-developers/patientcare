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

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return new UserResponse(user);
    }

    public UserResponse updateUserProfile(String userId, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Check if email is being changed and conflicts with another user
        if (updateRequest.getEmail() != null &&
                !updateRequest.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(updateRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + updateRequest.getEmail());
        }

        if (updateRequest.getFirstName() != null) user.setFirstName(updateRequest.getFirstName());
        if (updateRequest.getLastName() != null) user.setLastName(updateRequest.getLastName());
        if (updateRequest.getEmail() != null) user.setEmail(updateRequest.getEmail());
        if (updateRequest.getPhone() != null) user.setPhone(updateRequest.getPhone());

        User updatedUser = userRepository.save(user);
        return new UserResponse(updatedUser);
    }
}