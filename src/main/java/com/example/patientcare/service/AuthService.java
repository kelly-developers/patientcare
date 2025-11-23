package com.example.patientcare.service;

import com.example.patientcare.dto.request.LoginRequest;
import com.example.patientcare.dto.request.RefreshTokenRequest;
import com.example.patientcare.dto.request.SignupRequest;
import com.example.patientcare.dto.response.AuthResponse;
import com.example.patientcare.dto.response.TokenRefreshResponse;
import com.example.patientcare.dto.response.UserResponse;
import com.example.patientcare.entity.User;
import com.example.patientcare.exception.DuplicateResourceException;
import com.example.patientcare.exception.UnauthorizedException;
import com.example.patientcare.repository.UserRepository;
import com.example.patientcare.security.JwtService;
import com.example.patientcare.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponse login(LoginRequest loginRequest) {
        logger.info("Attempting login for user: {}", loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String token = jwtService.generateJwtToken(authentication);

            // Validate token structure immediately
            if (token == null || token.trim().isEmpty()) {
                throw new RuntimeException("Generated JWT token is null or empty");
            }

            logger.info("JWT token generated successfully for user: {}", userPrincipal.getUsername());

            // Generate simple refresh token without database dependency for now
            String refreshToken = jwtService.generateRefreshToken();

            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return new AuthResponse(token, refreshToken, new UserResponse(user));
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials for user: {}", loginRequest.getUsername());
            throw new UnauthorizedException("Invalid username or password");
        } catch (Exception e) {
            logger.error("Login failed for user {}: {}", loginRequest.getUsername(), e.getMessage());
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    public AuthResponse register(SignupRequest signupRequest) {
        logger.info("Attempting registration for user: {}", signupRequest.getUsername());

        // Check if username exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new DuplicateResourceException("Username is already taken");
        }

        // Check if email exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new DuplicateResourceException("Email is already in use");
        }

        try {
            // Create new user
            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setFirstName(signupRequest.getFirstName());
            user.setLastName(signupRequest.getLastName());
            user.setPhone(signupRequest.getPhone());

            // Set role - handle null case properly
            if (signupRequest.getRole() != null) {
                user.setRole(signupRequest.getRole());
            } else {
                user.setRole(User.Role.PATIENT); // Default role
            }

            User savedUser = userRepository.save(user);
            logger.info("User registered successfully: {}", savedUser.getUsername());

            // Generate tokens using username directly
            String token = jwtService.generateJwtToken(savedUser.getUsername());

            // Validate token structure
            if (token == null || token.trim().isEmpty()) {
                throw new RuntimeException("Generated JWT token is null or empty");
            }

            // Generate simple refresh token without database dependency
            String refreshToken = jwtService.generateRefreshToken();

            return new AuthResponse(token, refreshToken, new UserResponse(savedUser));
        } catch (Exception e) {
            logger.error("Registration failed for user {}: {}", signupRequest.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public TokenRefreshResponse refreshToken(RefreshTokenRequest request) {
        logger.info("Attempting token refresh");

        try {
            // For now, just generate a new token without validating the refresh token
            // In production, you should validate the refresh token against a database
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new UnauthorizedException("Authentication required for token refresh");
            }

            String username = authentication.getName();
            String newToken = jwtService.generateJwtToken(username);
            String newRefreshToken = jwtService.generateRefreshToken();

            logger.info("Token refresh successful for user: {}", username);
            return new TokenRefreshResponse(newToken, newRefreshToken);
        } catch (Exception e) {
            logger.error("Token refresh failed: {}", e.getMessage());
            throw new UnauthorizedException("Token refresh failed: " + e.getMessage());
        }
    }

    public void logout() {
        try {
            SecurityContextHolder.clearContext();
            logger.info("User logged out successfully");
        } catch (Exception e) {
            logger.error("Logout error: {}", e.getMessage());
            throw new RuntimeException("Logout failed");
        }
    }

    public boolean verifyToken(String token) {
        boolean isValid = jwtService.validateJwtToken(token);
        logger.debug("Token verification result: {}", isValid);
        return isValid;
    }
}