package com.example.patientcare.service;

import com.example.patientcare.dto.request.PatientRequest;
import com.example.patientcare.dto.response.PatientResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.exception.DuplicateResourceException;
import com.example.patientcare.exception.ResourceNotFoundException;
import com.example.patientcare.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(PatientResponse::new)
                .collect(Collectors.toList());
    }

    public Page<PatientResponse> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(PatientResponse::new);
    }

    public PatientResponse getPatientById(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return new PatientResponse(patient);
    }

    public PatientResponse getPatientByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with patient ID: " + patientId));
        return new PatientResponse(patient);
    }

    public PatientResponse createPatient(PatientRequest patientRequest) {
        // Check if patient ID already exists
        if (patientRepository.existsByPatientId(patientRequest.getPatientId())) {
            throw new DuplicateResourceException("Patient ID already exists: " + patientRequest.getPatientId());
        }

        // Check if email already exists
        if (patientRequest.getEmail() != null && patientRepository.existsByEmail(patientRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + patientRequest.getEmail());
        }

        Patient patient = new Patient();
        updatePatientFromRequest(patient, patientRequest);

        Patient savedPatient = patientRepository.save(patient);
        return new PatientResponse(savedPatient);
    }

    public PatientResponse updatePatient(String id, PatientRequest patientRequest) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        // Check if email is being changed and conflicts with another patient
        if (patientRequest.getEmail() != null &&
                !patientRequest.getEmail().equals(patient.getEmail()) &&
                patientRepository.existsByEmail(patientRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + patientRequest.getEmail());
        }

        updatePatientFromRequest(patient, patientRequest);
        Patient updatedPatient = patientRepository.save(patient);
        return new PatientResponse(updatedPatient);
    }

    public void deletePatient(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(patient);
    }

    public List<PatientResponse> searchPatients(String query) {
        return patientRepository.searchPatients(query).stream()
                .map(PatientResponse::new)
                .collect(Collectors.toList());
    }

    public PatientResponse updateResearchConsent(String id, Boolean researchConsent) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        patient.setResearchConsent(researchConsent);
        if (researchConsent && patient.getResearchConsentDate() == null) {
            patient.setResearchConsentDate(LocalDateTime.now());
        }

        Patient updatedPatient = patientRepository.save(patient);
        return new PatientResponse(updatedPatient);
    }

    public List<PatientResponse> getPatientsWithConsent() {
        return patientRepository.findByResearchConsentTrue().stream()
                .map(PatientResponse::new)
                .collect(Collectors.toList());
    }

    public List<PatientResponse> getPatientsByIds(List<String> ids) {
        return patientRepository.findByIdIn(ids).stream()
                .map(PatientResponse::new)
                .collect(Collectors.toList());
    }

    private void updatePatientFromRequest(Patient patient, PatientRequest request) {
        if (request.getPatientId() != null) patient.setPatientId(request.getPatientId());
        if (request.getFirstName() != null) patient.setFirstName(request.getFirstName());
        if (request.getLastName() != null) patient.setLastName(request.getLastName());
        if (request.getDateOfBirth() != null) patient.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) patient.setGender(request.getGender());
        if (request.getPhone() != null) patient.setPhone(request.getPhone());
        if (request.getEmail() != null) patient.setEmail(request.getEmail());
        if (request.getAddress() != null) patient.setAddress(request.getAddress());
        if (request.getEmergencyContactName() != null) patient.setEmergencyContactName(request.getEmergencyContactName());
        if (request.getEmergencyContactPhone() != null) patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        if (request.getMedicalHistory() != null) patient.setMedicalHistory(request.getMedicalHistory());
        if (request.getAllergies() != null) patient.setAllergies(request.getAllergies());
        if (request.getCurrentMedications() != null) patient.setCurrentMedications(request.getCurrentMedications());
        if (request.getResearchConsent() != null) patient.setResearchConsent(request.getResearchConsent());
    }
}