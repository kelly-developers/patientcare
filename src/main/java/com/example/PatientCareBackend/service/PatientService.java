package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.PatientRequest;
import com.example.PatientCareBackend.dto.response.PatientResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    // Generate unique patient ID: HOSP-YYYY-XXXXX
    private String generatePatientId() {
        String year = String.valueOf(Year.now().getValue());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "HOSP-" + year + "-" + uniqueId;
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return mapToResponse(patient);
    }

    @Transactional
    public PatientResponse createPatient(PatientRequest patientRequest) {
        Patient patient = new Patient();

        // Generate unique patient ID
        patient.setPatientId(generatePatientId());

        // Map all fields
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
        patient.setConsentAccepted(patientRequest.getConsentAccepted());
        patient.setConsentFormPath(patientRequest.getConsentFormPath());
        patient.setResearchConsent(patientRequest.getResearchConsent() != null ? patientRequest.getResearchConsent() : false);
        patient.setSampleStorageConsent(patientRequest.getSampleStorageConsent() != null ? patientRequest.getSampleStorageConsent() : false);

        Patient savedPatient = patientRepository.save(patient);
        return mapToResponse(savedPatient);
    }

    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest patientRequest) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        // Update all fields
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
        patient.setConsentAccepted(patientRequest.getConsentAccepted());

        if (patientRequest.getConsentFormPath() != null) {
            patient.setConsentFormPath(patientRequest.getConsentFormPath());
        }

        if (patientRequest.getResearchConsent() != null) {
            patient.setResearchConsent(patientRequest.getResearchConsent());
        }

        if (patientRequest.getSampleStorageConsent() != null) {
            patient.setSampleStorageConsent(patientRequest.getSampleStorageConsent());
        }

        Patient updatedPatient = patientRepository.save(patient);
        return mapToResponse(updatedPatient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));
        return mapToResponse(patient);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return patientRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public long getTotalPatients() {
        return patientRepository.count();
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> getPatientsWithConsent() {
        return patientRepository.findPatientsWithConsent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PatientResponse updateConsentForm(Long id, String filePath) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        patient.setConsentFormPath(filePath);
        patient.setConsentAccepted(true);

        Patient updatedPatient = patientRepository.save(patient);
        return mapToResponse(updatedPatient);
    }

    private PatientResponse mapToResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getEmergencyContactName(),
                patient.getEmergencyContactPhone(),
                patient.getMedicalHistory(),
                patient.getAllergies(),
                patient.getCurrentMedications(),
                patient.getConsentAccepted(),
                patient.getConsentFormPath(),
                patient.getResearchConsent() != null ? patient.getResearchConsent() : false,
                patient.getSampleStorageConsent() != null ? patient.getSampleStorageConsent() : false,
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}