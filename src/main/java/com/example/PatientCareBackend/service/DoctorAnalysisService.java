package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.DoctorAnalysisRequest;
import com.example.PatientCareBackend.dto.response.DoctorAnalysisResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.DoctorAnalysis;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.User;
import com.example.PatientCareBackend.repository.DoctorAnalysisRepository;
import com.example.PatientCareBackend.repository.PatientRepository;
import com.example.PatientCareBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorAnalysisService {

    private final DoctorAnalysisRepository doctorAnalysisRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Transactional
    public DoctorAnalysisResponse createAnalysis(DoctorAnalysisRequest analysisRequest) {
        Patient patient = patientRepository.findById(analysisRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + analysisRequest.getPatientId()));

        User doctor = userRepository.findById(analysisRequest.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + analysisRequest.getDoctorId()));

        DoctorAnalysis analysis = new DoctorAnalysis();
        analysis.setPatient(patient);
        analysis.setDoctor(doctor);
        analysis.setSymptoms(analysisRequest.getSymptoms());
        analysis.setDiagnosis(analysisRequest.getDiagnosis());
        analysis.setClinicalNotes(analysisRequest.getClinicalNotes());
        analysis.setRecommendSurgery(analysisRequest.getRecommendSurgery());
        analysis.setSurgeryType(analysisRequest.getSurgeryType());
        analysis.setSurgeryUrgency(analysisRequest.getSurgeryUrgency());
        analysis.setRequireLabTests(analysisRequest.getRequireLabTests());
        analysis.setLabTestsNeeded(analysisRequest.getLabTestsNeeded());
        analysis.setStatus(analysisRequest.getStatus());

        DoctorAnalysis savedAnalysis = doctorAnalysisRepository.save(analysis);
        return mapToResponse(savedAnalysis);
    }

    @Transactional(readOnly = true)
    public List<DoctorAnalysisResponse> getAnalysesByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return doctorAnalysisRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoctorAnalysisResponse getAnalysisById(Long id) {
        DoctorAnalysis analysis = doctorAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor analysis not found with id: " + id));
        return mapToResponse(analysis);
    }

    @Transactional
    public DoctorAnalysisResponse updateAnalysis(Long id, DoctorAnalysisRequest analysisRequest) {
        DoctorAnalysis analysis = doctorAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor analysis not found with id: " + id));

        Patient patient = patientRepository.findById(analysisRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + analysisRequest.getPatientId()));

        User doctor = userRepository.findById(analysisRequest.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + analysisRequest.getDoctorId()));

        analysis.setPatient(patient);
        analysis.setDoctor(doctor);
        analysis.setSymptoms(analysisRequest.getSymptoms());
        analysis.setDiagnosis(analysisRequest.getDiagnosis());
        analysis.setClinicalNotes(analysisRequest.getClinicalNotes());
        analysis.setRecommendSurgery(analysisRequest.getRecommendSurgery());
        analysis.setSurgeryType(analysisRequest.getSurgeryType());
        analysis.setSurgeryUrgency(analysisRequest.getSurgeryUrgency());
        analysis.setRequireLabTests(analysisRequest.getRequireLabTests());
        analysis.setLabTestsNeeded(analysisRequest.getLabTestsNeeded());
        analysis.setStatus(analysisRequest.getStatus());

        DoctorAnalysis updatedAnalysis = doctorAnalysisRepository.save(analysis);
        return mapToResponse(updatedAnalysis);
    }

    @Transactional
    public void deleteAnalysis(Long id) {
        DoctorAnalysis analysis = doctorAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor analysis not found with id: " + id));
        doctorAnalysisRepository.delete(analysis);
    }

    @Transactional(readOnly = true)
    public List<DoctorAnalysisResponse> getAnalysesByDoctor(Long doctorId) {
        if (!userRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }

        return doctorAnalysisRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorAnalysisResponse> getAnalysesRequiringSurgery() {
        return doctorAnalysisRepository.findByRecommendSurgeryTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DoctorAnalysisResponse mapToResponse(DoctorAnalysis analysis) {
        return new DoctorAnalysisResponse(
                analysis.getId(),
                mapToPatientResponse(analysis.getPatient()),
                mapToUserResponse(analysis.getDoctor()),
                analysis.getSymptoms(),
                analysis.getDiagnosis(),
                analysis.getClinicalNotes(),
                analysis.getRecommendSurgery(),
                analysis.getSurgeryType(),
                analysis.getSurgeryUrgency(),
                analysis.getRequireLabTests(),
                analysis.getLabTestsNeeded(),
                analysis.getStatus(),
                analysis.getCreatedAt()
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
                patient.getConsentAccepted(),  // ✅ Position 15
                patient.getConsentFormPath(),  // ✅ Position 16
                patient.getResearchConsent(),  // ✅ Position 17
                patient.getSampleStorageConsent(),  // ✅ Position 18
                patient.getCreatedAt(),        // ✅ Position 19
                patient.getUpdatedAt()         // ✅ Position 20
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