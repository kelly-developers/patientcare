package com.example.patientcare.config;

import com.example.patientcare.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretValidator implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(JwtSecretValidator.class);

    @Autowired
    private JwtService jwtService;

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("Validating JWT configuration on startup...");
            jwtService.validateJwtConfiguration(); // This is the correct method name
            logger.info("JWT configuration validation completed successfully");
        } catch (Exception e) {
            logger.error("JWT configuration validation failed: {}", e.getMessage());
            // Don't throw exception to allow application to start, but log the error
        }
    }
}