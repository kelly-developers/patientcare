package com.example.patientcare.service;

import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PatientOnboardingService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmailService emailService; // You'll need to implement this

    @Transactional
    public PatientResponse onboardNewPatient(PatientRequest patientRequest) {
        // Validate patient data
        validatePatientData(patientRequest);

        // Create patient
        PatientResponse patientResponse = createPatient(patientRequest);

        // Send welcome email (if email provided)
        if (patientRequest.getEmail() != null && !patientRequest.getEmail().isEmpty()) {
            sendWelcomeEmail(patientResponse);
        }

        // Generate patient portal credentials (if needed)
        generatePortalCredentials(patientResponse);

        return patientResponse;
    }

    private void validatePatientData(PatientRequest patientRequest) {
        // Check for required fields
        if (patientRequest.getPatientId() == null || patientRequest.getPatientId().trim().isEmpty()) {
            throw new RuntimeException("Patient ID is required");
        }

        if (patientRequest.getFirstName() == null || patientRequest.getFirstName().trim().isEmpty()) {
            throw new RuntimeException("First name is required");
        }

        if (patientRequest.getLastName() == null || patientRequest.getLastName().trim().isEmpty()) {
            throw new RuntimeException("Last name is required");
        }

        if (patientRequest.getDateOfBirth() == null) {
            throw new RuntimeException("Date of birth is required");
        }

        // Check if patient ID already exists
        if (patientRepository.existsByPatientId(patientRequest.getPatientId())) {
            throw new RuntimeException("Patient ID already exists: " + patientRequest.getPatientId());
        }

        // Check if email already exists
        if (patientRequest.getEmail() != null &&
                patientRepository.existsByEmail(patientRequest.getEmail())) {
            throw new RuntimeException("Email already exists: " + patientRequest.getEmail());
        }
    }

    private PatientResponse createPatient(PatientRequest patientRequest) {
        Patient patient = new Patient();
        patient.setPatientId(patientRequest.getPatientId());
        patient.setFirstName(patientRequest.getFirstName());
        patient.setLastName(patientRequest.getLastName());
        patient.setDateOfBirth(patientRequest.getDateOfBirth());
        patient.setGender(patientRequest.getGender());
        patient.setPhone(patientRequest.getPhone());
        patient.setEmail(patientRequest.getEmail());
        patient.setAddress(patientRequest.getAddress());
        patient.setEmergencyContactName(patientRequest.getEmergencyContactName());
        patient.setEmergencyContactPhone(patientRequest.getEmergencyContactPhone());
        patient.setMedicalHistory(patientRequest.getMedicalHistory());
        patient.setAllergies(patientRequest.getAllergies());
        patient.setCurrentMedications(patientRequest.getCurrentMedications());
        patient.setResearchConsent(patientRequest.getResearchConsent());

        if (patientRequest.getResearchConsent() != null && patientRequest.getResearchConsent()) {
            patient.setResearchConsentDate(LocalDateTime.now());
        }

        Patient savedPatient = patientRepository.save(patient);
        return new PatientResponse(savedPatient);
    }

    private void sendWelcomeEmail(PatientResponse patient) {
        // Implement email sending logic
        String subject = "Welcome to Cardiovascular Management System";
        String body = String.format(
                "Dear %s %s,\n\nWelcome to our healthcare system. Your patient ID is: %s\n\n" +
                        "Please keep this information for your records.\n\nBest regards,\nCVMS Team",
                patient.getFirstName(), patient.getLastName(), patient.getPatientId()
        );

        // emailService.sendEmail(patient.getEmail(), subject, body);
    }

    private void generatePortalCredentials(PatientResponse patient) {
        // Implement portal credential generation logic
        // This could create a user account for the patient to access their records
    }

    public PatientResponse getOnboardingStatus(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));
        return new PatientResponse(patient);
    }
}