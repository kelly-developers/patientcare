package com.example.patientcare.service;

import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.exception.DuplicateResourceException;
import com.example.patientcare.exception.ResourceNotFoundException;
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
        return patientRepository.findAll().stream()
                .map(this::mapToPatientResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse getPatientById(String id) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return mapToPatientResponse(patient);
    }

    public PatientResponse getPatientByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with patient ID: " + patientId));
        return mapToPatientResponse(patient);
    }

    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsByPatientId(request.getPatientId())) {
            throw new DuplicateResourceException("Patient ID already exists: " + request.getPatientId());
        }

        Patient patient = Patient.builder()
                .patientId(request.getPatientId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender() != null ? Patient.Gender.valueOf(request.getGender().toUpperCase()) : null)
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
                .build();

        Patient savedPatient = patientRepository.save(patient);
        return mapToPatientResponse(savedPatient);
    }

    public PatientResponse updatePatient(String id, PatientRequest request) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        // Check if patient ID is being changed and if it already exists
        if (!patient.getPatientId().equals(request.getPatientId()) &&
                patientRepository.existsByPatientId(request.getPatientId())) {
            throw new DuplicateResourceException("Patient ID already exists: " + request.getPatientId());
        }

        patient.setPatientId(request.getPatientId());
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender() != null ? Patient.Gender.valueOf(request.getGender().toUpperCase()) : null);
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setAllergies(request.getAllergies());
        patient.setCurrentMedications(request.getCurrentMedications());

        Patient updatedPatient = patientRepository.save(patient);
        return mapToPatientResponse(updatedPatient);
    }

    public void deletePatient(String id) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    public List<PatientResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(this::mapToPatientResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse updateResearchConsent(String id, Boolean researchConsent) {
        Patient patient = patientRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        patient.setResearchConsent(researchConsent);
        patient.setResearchConsentDate(researchConsent ? LocalDateTime.now() : null);

        Patient updatedPatient = patientRepository.save(patient);
        return mapToPatientResponse(updatedPatient);
    }

    public List<PatientResponse> getPatientsWithResearchConsent() {
        return patientRepository.findByResearchConsentTrue().stream()
                .map(this::mapToPatientResponse)
                .collect(Collectors.toList());
    }

    private PatientResponse mapToPatientResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId().toString())
                .patientId(patient.getPatientId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .gender(patient.getGender() != null ? patient.getGender().name() : null)
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
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}