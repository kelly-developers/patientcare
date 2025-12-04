package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.PharmacyRequest;
import com.example.PatientCareBackend.dto.response.PharmacyResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.Pharmacy;
import com.example.PatientCareBackend.model.User;
import com.example.PatientCareBackend.repository.PatientRepository;
import com.example.PatientCareBackend.repository.PharmacyRepository;
import com.example.PatientCareBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Transactional
    public PharmacyResponse createPrescription(PharmacyRequest pharmacyRequest) {
        Patient patient = patientRepository.findById(pharmacyRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + pharmacyRequest.getPatientId()));

        User doctor = userRepository.findById(pharmacyRequest.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + pharmacyRequest.getDoctorId()));

        Pharmacy prescription = new Pharmacy();
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setMedicationName(pharmacyRequest.getMedicationName());
        prescription.setDosage(pharmacyRequest.getDosage());
        prescription.setFrequency(pharmacyRequest.getFrequency());
        prescription.setDuration(pharmacyRequest.getDuration());
        prescription.setInstructions(pharmacyRequest.getInstructions());
        prescription.setStatus(pharmacyRequest.getStatus());

        Pharmacy savedPrescription = pharmacyRepository.save(prescription);
        return mapToResponse(savedPrescription);
    }

    @Transactional(readOnly = true)
    public List<PharmacyResponse> getAllPrescriptions() {
        return pharmacyRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PharmacyResponse> getPrescriptionsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return pharmacyRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PharmacyResponse updatePrescriptionStatus(Long id, Pharmacy.PrescriptionStatus status) {
        Pharmacy prescription = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));

        prescription.setStatus(status);

        // Update timestamps based on status
        if (status == Pharmacy.PrescriptionStatus.DISPENSED) {
            prescription.setDispensedAt(LocalDateTime.now());
        } else if (status == Pharmacy.PrescriptionStatus.COLLECTED) {
            prescription.setCollectedAt(LocalDateTime.now());
        }

        Pharmacy updatedPrescription = pharmacyRepository.save(prescription);
        return mapToResponse(updatedPrescription);
    }

    @Transactional(readOnly = true)
    public PharmacyResponse getPrescriptionById(Long id) {
        Pharmacy prescription = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        return mapToResponse(prescription);
    }

    @Transactional(readOnly = true)
    public List<PharmacyResponse> getPendingPrescriptions() {
        return pharmacyRepository.findPendingPrescriptions().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PharmacyResponse> getPrescriptionsByDoctor(Long doctorId) {
        if (!userRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }

        return pharmacyRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PharmacyResponse> getActivePrescriptionsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return pharmacyRepository.findActivePrescriptionsByPatient(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PharmacyResponse> searchByMedicationName(String medicationName) {
        return pharmacyRepository.findByMedicationNameContaining(medicationName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePrescription(Long id) {
        Pharmacy prescription = pharmacyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
        pharmacyRepository.delete(prescription);
    }

    private PharmacyResponse mapToResponse(Pharmacy prescription) {
        return new PharmacyResponse(
                prescription.getId(),
                mapToPatientResponse(prescription.getPatient()),
                mapToUserResponse(prescription.getDoctor()),
                prescription.getMedicationName(),
                prescription.getDosage(),
                prescription.getFrequency(),
                prescription.getDuration(),
                prescription.getInstructions(),
                prescription.getStatus(),
                prescription.getDispensedAt(),
                prescription.getCollectedAt(),
                prescription.getCreatedAt()
        );
    }

    private com.example.PatientCareBackend.dto.response.PatientResponse mapToPatientResponse(Patient patient) {
        return new com.example.PatientCareBackend.dto.response.PatientResponse(
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
                patient.getResearchConsent(),
                patient.getSampleStorageConsent(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }

    private com.example.PatientCareBackend.dto.response.UserResponse mapToUserResponse(User user) {
        return new com.example.PatientCareBackend.dto.response.UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getSpecialty(),
                user.getAvailable(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}