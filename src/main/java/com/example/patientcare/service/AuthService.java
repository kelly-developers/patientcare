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
import org.springframework.dao.DataIntegrityViolationException;
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
            // Validate input
            if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Username is required");
            }
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername().trim(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String token = jwtService.generateJwtToken(authentication);

            if (token == null || token.trim().isEmpty()) {
                logger.error("Generated JWT token is null or empty for user: {}", loginRequest.getUsername());
                throw new RuntimeException("Token generation failed during login");
            }

            logger.info("JWT token generated successfully for user: {}", userPrincipal.getUsername());

            String refreshToken = jwtService.generateRefreshToken();

            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> {
                        logger.error("User not found after successful authentication: {}", userPrincipal.getId());
                        return new RuntimeException("User not found in database");
                    });

            logger.info("Login successful for user: {}", user.getUsername());
            return new AuthResponse(token, refreshToken, new UserResponse(user));
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for user: {}", loginRequest.getUsername());
            throw new UnauthorizedException("Invalid username or password");
        } catch (Exception e) {
            logger.error("Login failed for user {}: {}", loginRequest.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    public AuthResponse register(SignupRequest signupRequest) {
        logger.info("Attempting registration for user: {}", signupRequest.getUsername());
        logger.debug("Registration data - Username: {}, Email: {}, FirstName: {}, LastName: {}",
                signupRequest.getUsername(), signupRequest.getEmail(),
                signupRequest.getFirstName(), signupRequest.getLastName());

        // Validate input
        if (signupRequest.getUsername() == null || signupRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (signupRequest.getEmail() == null || signupRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (signupRequest.getPassword() == null || signupRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        String username = signupRequest.getUsername().trim();
        String email = signupRequest.getEmail().trim().toLowerCase();

        try {
            // Check if username exists
            if (userRepository.existsByUsername(username)) {
                logger.warn("Username already exists: {}", username);
                throw new DuplicateResourceException("Username '" + username + "' is already taken");
            }

            // Check if email exists
            if (userRepository.existsByEmail(email)) {
                logger.warn("Email already exists: {}", email);
                throw new DuplicateResourceException("Email '" + email + "' is already in use");
            }

            // Create new user
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setFirstName(signupRequest.getFirstName() != null ? signupRequest.getFirstName().trim() : "");
            user.setLastName(signupRequest.getLastName() != null ? signupRequest.getLastName().trim() : "");
            user.setPhone(signupRequest.getPhone() != null ? signupRequest.getPhone().trim() : null);

            // Set role
            if (signupRequest.getRole() != null) {
                user.setRole(signupRequest.getRole());
            } else {
                user.setRole(User.Role.PATIENT);
            }

            logger.info("Saving user to database: {}", user.getUsername());
            User savedUser;
            try {
                savedUser = userRepository.save(user);
                logger.info("User saved successfully with ID: {}", savedUser.getId());
            } catch (DataIntegrityViolationException e) {
                logger.error("Database integrity violation during user save: {}", e.getMessage(), e);
                throw new RuntimeException("Registration failed due to database constraints. The username or email may already exist.");
            }

            // Generate JWT token using the username directly
            logger.info("Generating JWT token for new user: {}", savedUser.getUsername());
            String token;
            try {
                token = jwtService.generateJwtTokenFromUsername(savedUser.getUsername());
                if (token == null || token.trim().isEmpty()) {
                    throw new RuntimeException("Generated JWT token is null or empty");
                }
                logger.debug("JWT token generated successfully for new user");
            } catch (Exception e) {
                logger.error("JWT token generation failed for new user: {}", e.getMessage(), e);
                throw new RuntimeException("Token generation failed during registration");
            }

            String refreshToken = jwtService.generateRefreshToken();

            logger.info("Registration completed successfully for user: {}", savedUser.getUsername());
            return new AuthResponse(token, refreshToken, new UserResponse(savedUser));

        } catch (DuplicateResourceException e) {
            // Re-throw duplicate exceptions to be handled by controller
            throw e;
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid argument during registration: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Registration failed for user {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public TokenRefreshResponse refreshToken(RefreshTokenRequest request) {
        logger.info("Attempting token refresh");

        try {
            if (request.getRefreshToken() == null || request.getRefreshToken().trim().isEmpty()) {
                throw new UnauthorizedException("Refresh token is required");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new UnauthorizedException("Authentication required for token refresh");
            }

            String username = authentication.getName();
            String newToken = jwtService.generateJwtTokenFromUsername(username);
            String newRefreshToken = jwtService.generateRefreshToken();

            logger.info("Token refresh successful for user: {}", username);
            return new TokenRefreshResponse(newToken, newRefreshToken);
        } catch (Exception e) {
            logger.error("Token refresh failed: {}", e.getMessage(), e);
            throw new UnauthorizedException("Token refresh failed: " + e.getMessage());
        }
    }

    public void logout() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                logger.info("Logging out user: {}", auth.getName());
            }
            SecurityContextHolder.clearContext();
            logger.info("User logged out successfully");
        } catch (Exception e) {
            logger.error("Logout error: {}", e.getMessage(), e);
            throw new RuntimeException("Logout failed: " + e.getMessage());
        }
    }

    public boolean verifyToken(String token) {
        try {
            boolean isValid = jwtService.validateJwtToken(token);
            logger.debug("Token verification result: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Token verification error: {}", e.getMessage());
            return false;
        }
    }
}