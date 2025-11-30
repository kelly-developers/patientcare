package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.AuthRequest;
import com.example.PatientCareBackend.dto.request.RegisterRequest;
import com.example.PatientCareBackend.dto.response.AuthResponse;
import com.example.PatientCareBackend.exception.AuthenticationException;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.exception.ValidationException;
import com.example.PatientCareBackend.model.User;
import com.example.PatientCareBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get the UserDetails from the authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Load the actual User entity from the database
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userDetails.getUsername()));

            String jwt = jwtService.generateToken(user);

            return new AuthResponse(
                    jwt,
                    "Bearer",
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole(),
                    user.getSpecialty()
            );
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException("Invalid username or password");
        }
    }

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ValidationException("Username is already taken");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ValidationException("Email is already registered");
        }

        // Only validate admin access if trying to create admin user or if it's not a self-registration
        if (registerRequest.getRole() == User.Role.ADMIN) {
            validateAdminAccess();
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setRole(registerRequest.getRole());
        user.setSpecialty(registerRequest.getSpecialty());
        user.setAvailable(true);

        User savedUser = userRepository.save(user);
        String jwt = jwtService.generateToken(savedUser);

        return new AuthResponse(
                jwt,
                "Bearer",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRole(),
                savedUser.getSpecialty()
        );
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User not authenticated");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    public void validateAdminAccess() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new AuthenticationException("Access denied. Admin privileges required.");
        }
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        // Implement token refresh logic if needed
        throw new UnsupportedOperationException("Token refresh not implemented");
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}