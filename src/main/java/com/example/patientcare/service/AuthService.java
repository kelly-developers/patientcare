package com.example.patientcare.service;

import com.example.patientcare.dto.request.LoginRequest;
import com.example.patientcare.dto.request.RefreshTokenRequest;
import com.example.patientcare.dto.request.SignupRequest;
import com.example.patientcare.dto.response.AuthResponse;
import com.example.patientcare.dto.response.TokenRefreshResponse;
import com.example.patientcare.dto.response.UserResponse;
import com.example.patientcare.entity.RefreshToken;
import com.example.patientcare.entity.User;
import com.example.patientcare.exception.DuplicateResourceException;
import com.example.patientcare.exception.UnauthorizedException;
import com.example.patientcare.repository.RefreshTokenRepository;
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

import java.util.Optional;

@Service
@Transactional
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

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

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId());

            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return new AuthResponse(token, refreshToken.getToken(), new UserResponse(user));
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

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getId());

            return new AuthResponse(token, refreshToken.getToken(), new UserResponse(savedUser));
        } catch (Exception e) {
            logger.error("Registration failed for user {}: {}", signupRequest.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public TokenRefreshResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        logger.info("Attempting token refresh");

        Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByToken(requestRefreshToken);

        if (refreshTokenOpt.isEmpty()) {
            logger.error("Refresh token not found in database");
            throw new UnauthorizedException("Refresh token is not in database");
        }

        RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenOpt.get());
        User user = refreshToken.getUser();

        String token = jwtService.generateJwtToken(user.getUsername());

        // Validate new token structure
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Generated JWT token during refresh is null or empty");
        }

        // Create new refresh token
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
        logger.info("Token refresh successful for user: {}", user.getUsername());

        return new TokenRefreshResponse(token, newRefreshToken.getToken());
    }

    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            refreshTokenService.deleteByUserId(userPrincipal.getId());
            logger.info("User logged out: {}", userPrincipal.getUsername());
        }
        SecurityContextHolder.clearContext();
    }

    public void logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
        logger.info("User logged out via refresh token");
        SecurityContextHolder.clearContext();
    }

    public boolean verifyToken(String token) {
        boolean isValid = jwtService.validateJwtToken(token);
        logger.debug("Token verification result: {}", isValid);
        return isValid;
    }
}