package com.example.patientcare.service;

import com.example.patientcare.entity.RefreshToken;
import com.example.patientcare.entity.User;
import com.example.patientcare.exception.UnauthorizedException;
import com.example.patientcare.repository.RefreshTokenRepository;
import com.example.patientcare.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    @Value("${app.jwt.refresh-expiration-ms:604800000}") // 7 days default
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String userId) {
        try {
            // Delete existing refresh tokens for this user
            refreshTokenRepository.deleteByUserId(userId);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setToken(UUID.randomUUID().toString());

            // Fixed: Use Duration.ofMillis() instead of plusMillis()
            refreshToken.setExpiryDate(LocalDateTime.now().plus(Duration.ofMillis(refreshTokenDurationMs)));

            RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
            logger.info("Created refresh token for user: {}", userId);
            return savedToken;
        } catch (Exception e) {
            logger.error("Error creating refresh token for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to create refresh token: " + e.getMessage());
        }
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new UnauthorizedException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void deleteByUserId(String userId) {
        try {
            refreshTokenRepository.deleteByUserId(userId);
            logger.info("Deleted refresh tokens for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error deleting refresh tokens for user {}: {}", userId, e.getMessage());
        }
    }

    public void deleteByToken(String token) {
        try {
            refreshTokenRepository.deleteByToken(token);
            logger.info("Deleted refresh token: {}", token);
        } catch (Exception e) {
            logger.error("Error deleting refresh token {}: {}", token, e.getMessage());
        }
    }
}