package com.example.patientcare.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
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

    @Value("${app.jwt.secret:defaultSecretKeyThatIsAtLeast32CharactersLong}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private int jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration-ms:604800000}")
    private int refreshTokenExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            logger.debug("Generating JWT token for user: {}", userPrincipal.getUsername());
            return generateJwtTokenFromUsername(userPrincipal.getUsername());
        } catch (Exception e) {
            logger.error("Error generating JWT token from authentication: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate JWT token from authentication", e);
        }
    }

    public String generateJwtTokenFromUsername(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty for JWT generation");
            }

            SecretKey key = getSigningKey();
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

            String token = Jwts.builder()
                    .subject(username)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            logger.debug("Generated JWT token for user: {}, expires: {}", username, expiryDate);
            return token;
        } catch (Exception e) {
            logger.error("Error generating JWT token for username {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Failed to generate JWT token for user: " + username, e);
        }
    }

    public String generateRefreshToken() {
        try {
            String refreshToken = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
            logger.debug("Generated refresh token");
            return refreshToken;
        } catch (Exception e) {
            logger.error("Error generating refresh token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate refresh token", e);
        }
    }

    public String getUsernameFromJwtToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                throw new IllegalArgumentException("Token cannot be null or empty");
            }

            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            logger.debug("Extracted username from JWT: {}", username);
            return username;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token expired but extracting username: {}", e.getMessage());
            return e.getClaims().getSubject();
        } catch (Exception e) {
            logger.error("Error extracting username from JWT: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token: " + e.getMessage(), e);
        }
    }

    public boolean validateJwtToken(String authToken) {
        if (authToken == null || authToken.trim().isEmpty()) {
            logger.error("JWT token is null or empty");
            return false;
        }

        // Basic JWT format validation
        String[] parts = authToken.split("\\.");
        if (parts.length != 3) {
            logger.error("Invalid JWT token format. Expected 3 parts, found: {}", parts.length);
            return false;
        }

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);

            // Additional validation: check expiration
            Date expiration = claimsJws.getPayload().getExpiration();
            if (expiration.before(new Date())) {
                logger.error("JWT token has expired: {}", expiration);
                return false;
            }

            logger.debug("JWT token validation successful");
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token format: {}", e.getMessage());
        } catch (SecurityException e) {
            logger.error("JWT signature validation failed: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty or invalid: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("JWT token validation error: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during JWT validation: {}", e.getMessage());
        }
        return false;
    }

    private SecretKey getSigningKey() {
        try {
            logger.debug("Attempting to create JWT signing key from secret (length: {})",
                    jwtSecret != null ? jwtSecret.length() : "null");

            if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
                throw new IllegalArgumentException("JWT secret cannot be null or empty");
            }

            // Try to decode as Base64 first
            try {
                byte[] keyBytes = Decoders.BASE64.decode(jwtSecret.trim());
                if (keyBytes.length >= 32) { // Check if it's at least 256 bits
                    logger.info("JWT secret successfully decoded as Base64. Key length: {} bytes", keyBytes.length);
                    return Keys.hmacShaKeyFor(keyBytes);
                } else {
                    logger.warn("Base64 decoded JWT secret is only {} bytes, need at least 32 bytes", keyBytes.length);
                    // Fall through to string method
                }
            } catch (DecodingException | IllegalArgumentException e) {
                logger.debug("JWT secret is not Base64 encoded: {}", e.getMessage());
            }

            // Use as plain text - ensure it's long enough
            logger.warn("Using JWT secret as plain text. Recommended: use Base64 encoded 32+ byte key");
            if (jwtSecret.length() < 32) {
                String errorMsg = String.format(
                        "JWT secret must be at least 32 characters when not Base64 encoded. Current length: %d. " +
                                "Consider setting JWT_SECRET environment variable to a longer string.",
                        jwtSecret.length()
                );
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            // Use first 32 characters for 256-bit key
            String keyString = jwtSecret.length() > 32 ? jwtSecret.substring(0, 32) : jwtSecret;
            return Keys.hmacShaKeyFor(keyString.getBytes());

        } catch (Exception e) {
            logger.error("Failed to create JWT signing key: {}", e.getMessage(), e);
            throw new RuntimeException("JWT configuration error: " + e.getMessage(), e);
        }
    }

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public int getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }

    public void validateJwtConfiguration() {
        try {
            logger.info("Validating JWT configuration...");
            logger.info("JWT expiration: {} ms", jwtExpirationMs);
            logger.info("Refresh token expiration: {} ms", refreshTokenExpirationMs);

            SecretKey key = getSigningKey();
            logger.info("JWT secret validation successful");

            // Test token generation and validation
            String testUsername = "test-user-" + System.currentTimeMillis();
            String testToken = generateJwtTokenFromUsername(testUsername);

            if (testToken == null || testToken.trim().isEmpty()) {
                throw new RuntimeException("Generated test token is null or empty");
            }

            boolean isValid = validateJwtToken(testToken);
            if (!isValid) {
                throw new RuntimeException("Test token validation failed");
            }

            String extractedUsername = getUsernameFromJwtToken(testToken);
            if (!testUsername.equals(extractedUsername)) {
                throw new RuntimeException(String.format(
                        "Username mismatch. Expected: %s, Got: %s", testUsername, extractedUsername));
            }

            logger.info("JWT configuration validation completed successfully");
        } catch (Exception e) {
            logger.error("JWT configuration validation failed: {}", e.getMessage(), e);
            throw new RuntimeException("JWT configuration error: " + e.getMessage(), e);
        }
    }
}