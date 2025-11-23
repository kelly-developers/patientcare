package com.example.patientcare.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:900000}")
    private int jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration-ms:604800000}")
    private int refreshTokenExpirationMs;

    // Method for Authentication object
    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateJwtToken(userPrincipal.getUsername());
    }

    // Method for username string
    public String generateJwtToken(String username) {
        try {
            SecretKey key = getSigningKey();

            String token = Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            logger.debug("Generated JWT token for user: {}", username);
            return token;
        } catch (Exception e) {
            logger.error("Error generating JWT token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
    }

    public String getUsernameFromJwtToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            logger.error("Error extracting username from JWT: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("JWT token validation error: {}", e.getMessage());
        }
        return false;
    }

    private SecretKey getSigningKey() {
        try {
            // Try to decode as Base64 first
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            logger.debug("JWT secret successfully decoded as Base64");
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.warn("JWT secret is not Base64 encoded, using as plain text. Length: {}", jwtSecret.length());

            // If not Base64, use the string directly but ensure it's long enough
            if (jwtSecret.length() < 32) {
                String errorMsg = "JWT secret must be at least 32 characters when not Base64 encoded. Current length: " + jwtSecret.length();
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            // Use first 32 characters for 256-bit key
            String keyString = jwtSecret.length() > 32 ? jwtSecret.substring(0, 32) : jwtSecret;
            return Keys.hmacShaKeyFor(keyString.getBytes());
        }
    }

    public int getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }

    // Method to validate your JWT secret (call this during startup)
    public void validateJwtSecret() {
        try {
            SecretKey key = getSigningKey();
            logger.info("JWT secret validation successful");

            // Test token generation
            String testToken = generateJwtToken("test");
            logger.info("JWT token generation test successful");
        } catch (Exception e) {
            logger.error("JWT secret validation failed: {}", e.getMessage());
            throw new RuntimeException("JWT secret configuration error", e);
        }
    }
}