package com.example.patientcare.service;

import com.example.patientcare.dto.request.LoginRequest;
import com.example.patientcare.dto.request.RefreshTokenRequest;
import com.example.patientcare.dto.request.SignupRequest;
import com.example.patientcare.dto.response.AuthResponse;
import com.example.patientcare.dto.response.TokenRefreshResponse;
import com.example.patientcare.dto.response.UserResponse;
import com.example.patientcare.entity.RefreshToken;
import com.example.patientcare.entity.User;
import com.example.patientcare.exception.UnauthorizedException;
import com.example.patientcare.repository.RefreshTokenRepository;
import com.example.patientcare.repository.UserRepository;
import com.example.patientcare.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        logger.info("Login attempt for user: {}", request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String jwt = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            saveRefreshToken(user, refreshToken);

            logger.info("Login successful for user: {}", user.getUsername());

            return AuthResponse.builder()
                    .token(jwt)
                    .refreshToken(refreshToken)
                    .user(mapToUserResponse(user))
                    .build();
        } catch (Exception e) {
            logger.error("Login failed for user: {}", request.getUsername(), e);
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    public AuthResponse signup(SignupRequest request) {
        logger.info("Signup attempt for user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        try {
            User.UserRole role;
            try {
                role = User.UserRole.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                role = User.UserRole.PATIENT; // Default role
            }

            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .passwordHash(passwordEncoder.encode(request.getPassword()))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phone(request.getPhone())
                    .role(role)
                    .build();

            User savedUser = userRepository.save(user);

            String jwt = jwtService.generateToken(savedUser);
            String refreshToken = jwtService.generateRefreshToken(savedUser);

            saveRefreshToken(savedUser, refreshToken);

            logger.info("Signup successful for user: {}", savedUser.getUsername());

            return AuthResponse.builder()
                    .token(jwt)
                    .refreshToken(refreshToken)
                    .user(mapToUserResponse(savedUser))
                    .build();
        } catch (Exception e) {
            logger.error("Signup failed for user: {}", request.getUsername(), e);
            throw new RuntimeException("Signup failed: " + e.getMessage());
        }
    }

    public TokenRefreshResponse refreshToken(RefreshTokenRequest request) {
        logger.info("Token refresh attempt");

        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestRefreshToken)
                .orElseThrow(() -> {
                    logger.warn("Invalid refresh token provided");
                    return new UnauthorizedException("Invalid refresh token");
                });

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            logger.warn("Refresh token expired");
            throw new UnauthorizedException("Refresh token expired");
        }

        User user = refreshToken.getUser();
        String newToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Delete old refresh token and save new one
        refreshTokenRepository.delete(refreshToken);
        saveRefreshToken(user, newRefreshToken);

        logger.info("Token refresh successful for user: {}", user.getUsername());

        return TokenRefreshResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void logout(String refreshToken) {
        logger.info("Logout attempt");
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    refreshTokenRepository.delete(token);
                    logger.info("Logout successful for user: {}", token.getUser().getUsername());
                });
    }

    public boolean verifyToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UnauthorizedException("Invalid token"));
            boolean isValid = jwtService.isTokenValid(token, user);
            logger.info("Token verification for user {}: {}", username, isValid);
            return isValid;
        } catch (Exception e) {
            logger.warn("Token verification failed: {}", e.getMessage());
            return false;
        }
    }

    private void saveRefreshToken(User user, String refreshToken) {
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(jwtService.getRefreshExpiration() / 1000);

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiresAt(expiresAt)
                .build();

        refreshTokenRepository.save(token);
        logger.debug("Refresh token saved for user: {}", user.getUsername());
    }

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