package com.example.patientcare.service;

import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse getPatientById(String id) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
        return convertToResponse(patient);
    }

    public PatientResponse getPatientByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with patient ID: " + patientId));
        return convertToResponse(patient);
    }

    public PatientResponse createPatient(PatientRequest request) {
        // Generate patient ID
        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .medicalHistory(request.getMedicalHistory())
                .allergies(request.getAllergies())
                .currentMedications(request.getCurrentMedications())
                .researchConsent(request.getResearchConsent())
                .researchConsentDate(request.getResearchConsent() != null && request.getResearchConsent() ? LocalDateTime.now() : null)
                .futureContactConsent(request.getFutureContactConsent())
                .anonymizedDataConsent(request.getAnonymizedDataConsent())
                .sampleStorageConsent(request.getSampleStorageConsent())
                .sampleTypes(request.getSampleTypes())
                .storageDuration(request.getStorageDuration())
                .futureResearchUseConsent(request.getFutureResearchUseConsent())
                .destructionConsent(request.getDestructionConsent())
                .consentData(request.getConsentData())
                .build();

        // Generate patient ID
        patient.generatePatientId();

        // Check if patient ID is unique
        if (patientRepository.existsByPatientId(patient.getPatientId())) {
            throw new RuntimeException("Patient ID already exists: " + patient.getPatientId());
        }

        Patient savedPatient = patientRepository.save(patient);
        return convertToResponse(savedPatient);
    }

    public PatientResponse updatePatient(String id, PatientRequest request) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setAllergies(request.getAllergies());
        patient.setCurrentMedications(request.getCurrentMedications());

        // Update consent fields
        if (request.getResearchConsent() != null && !request.getResearchConsent().equals(patient.getResearchConsent())) {
            patient.setResearchConsent(request.getResearchConsent());
            patient.setResearchConsentDate(request.getResearchConsent() ? LocalDateTime.now() : null);
        }

        patient.setFutureContactConsent(request.getFutureContactConsent());
        patient.setAnonymizedDataConsent(request.getAnonymizedDataConsent());
        patient.setSampleStorageConsent(request.getSampleStorageConsent());
        patient.setSampleTypes(request.getSampleTypes());
        patient.setStorageDuration(request.getStorageDuration());
        patient.setFutureResearchUseConsent(request.getFutureResearchUseConsent());
        patient.setDestructionConsent(request.getDestructionConsent());
        patient.setConsentData(request.getConsentData());

        Patient updatedPatient = patientRepository.save(patient);
        return convertToResponse(updatedPatient);
    }

    public void deletePatient(String id) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
        patientRepository.delete(patient);
    }

    public List<PatientResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse updateResearchConsent(String id, Boolean consent) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));

        patient.setResearchConsent(consent);
        patient.setResearchConsentDate(consent ? LocalDateTime.now() : null);

        Patient updatedPatient = patientRepository.save(patient);
        return convertToResponse(updatedPatient);
    }

    public List<PatientResponse> getPatientsWithResearchConsent() {
        return patientRepository.findByResearchConsentTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private PatientResponse convertToResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId().toString())
                .patientId(patient.getPatientId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .medicalHistory(patient.getMedicalHistory())
                .allergies(patient.getAllergies())
                .currentMedications(patient.getCurrentMedications())
                .researchConsent(patient.getResearchConsent())
                .researchConsentDate(patient.getResearchConsentDate())
                .futureContactConsent(patient.getFutureContactConsent())
                .anonymizedDataConsent(patient.getAnonymizedDataConsent())
                .sampleStorageConsent(patient.getSampleStorageConsent())
                .sampleTypes(patient.getSampleTypes())
                .storageDuration(patient.getStorageDuration())
                .futureResearchUseConsent(patient.getFutureResearchUseConsent())
                .destructionConsent(patient.getDestructionConsent())
                .consentData(patient.getConsentData())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}