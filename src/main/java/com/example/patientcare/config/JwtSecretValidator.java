package com.example.patientcare.config;

import com.example.patientcare.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretValidator implements CommandLineRunner {

    @Autowired
    private JwtService jwtService;

    @Override
    public void run(String... args) throws Exception {
        jwtService.validateJwtSecret();
    }
}