package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.DoctorAnalysisRequest;
import com.example.PatientCareBackend.dto.response.DoctorAnalysisResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.*;
import com.example.PatientCareBackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorAnalysisService {

    private final DoctorAnalysisRepository doctorAnalysisRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final SurgeryRepository surgeryRepository;

    @Transactional
    public DoctorAnalysisResponse createAnalysis(DoctorAnalysisRequest analysisRequest) {
        // Find patient
        Patient patient = patientRepository.findById(analysisRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + analysisRequest.getPatientId()));

        // Find doctor (user)
        User doctor = userRepository.findById(analysisRequest.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + analysisRequest.getDoctorId()));

        // Create analysis
        DoctorAnalysis analysis = new DoctorAnalysis();
        analysis.setPatient(patient);
        analysis.setDoctor(doctor);
        analysis.setSymptoms(analysisRequest.getSymptoms());
        analysis.setDiagnosis(analysisRequest.getDiagnosis());
        analysis.setClinicalNotes(analysisRequest.getClinicalNotes());
        analysis.setRecommendSurgery(analysisRequest.getRecommendSurgery());
        analysis.setSurgeryType(analysisRequest.getSurgeryType());
        analysis.setSurgeryUrgency(mapStringToSurgeryUrgency(analysisRequest.getSurgeryUrgency()));
        analysis.setRequireLabTests(analysisRequest.getRequireLabTests());
        analysis.setLabTestsNeeded(analysisRequest.getLabTestsNeeded());
        analysis.setStatus(DoctorAnalysis.AnalysisStatus.fromString(analysisRequest.getStatus()));
        analysis.setCreatedAt(LocalDateTime.now());

        DoctorAnalysis savedAnalysis = doctorAnalysisRepository.save(analysis);

        // Create surgery record if surgery is recommended
        if (analysisRequest.getRecommendSurgery() && analysisRequest.getSurgeryType() != null) {
            createSurgeryFromAnalysis(patient, doctor, analysisRequest);
        }

        return mapToResponse(savedAnalysis);
    }

    @Transactional
    private void createSurgeryFromAnalysis(Patient patient, User doctor, DoctorAnalysisRequest analysisRequest) {
        // Check if surgery already exists for this patient with same procedure
        boolean surgeryExists = surgeryRepository.existsByPatientAndProcedureNameAndStatus(
                patient,
                analysisRequest.getSurgeryType(),
                Surgery.SurgeryStatus.PENDING_CONSENT
        );

        if (!surgeryExists) {
            Surgery surgery = new Surgery();
            surgery.setPatient(patient);
            surgery.setProcedureName(analysisRequest.getSurgeryType());
            surgery.setDiagnosis(analysisRequest.getDiagnosis());
            surgery.setUrgency(mapAnalysisUrgencyToSurgeryUrgency(analysisRequest.getSurgeryUrgency()));
            surgery.setStatus(Surgery.SurgeryStatus.PENDING_CONSENT);
            surgery.setRecommendedBy(doctor.getFirstName() + " " + doctor.getLastName());
            surgery.setScheduledDate(LocalDateTime.now().plusDays(getScheduledDays(analysisRequest.getSurgeryUrgency())));
            surgery.setCreatedAt(LocalDateTime.now());

            surgeryRepository.save(surgery);
        }
    }

    private Surgery.SurgeryUrgency mapAnalysisUrgencyToSurgeryUrgency(String analysisUrgency) {
        if (analysisUrgency == null) {
            return Surgery.SurgeryUrgency.ELECTIVE;
        }

        // First convert to DoctorAnalysis.SurgeryUrgency
        DoctorAnalysis.SurgeryUrgency doctorAnalysisUrgency = DoctorAnalysis.SurgeryUrgency.fromString(analysisUrgency);

        // Then map to Surgery.SurgeryUrgency
        return switch (doctorAnalysisUrgency) {
            case EMERGENT -> Surgery.SurgeryUrgency.EMERGENCY;
            case URGENT -> Surgery.SurgeryUrgency.URGENT;
            case SCHEDULED -> Surgery.SurgeryUrgency.SCHEDULED;
            case ELECTIVE -> Surgery.SurgeryUrgency.ELECTIVE;
            default -> Surgery.SurgeryUrgency.ELECTIVE;
        };
    }

    private DoctorAnalysis.SurgeryUrgency mapStringToSurgeryUrgency(String urgency) {
        return DoctorAnalysis.SurgeryUrgency.fromString(urgency);
    }

    private int getScheduledDays(String urgency) {
        if (urgency == null) {
            return 7;
        }

        // Convert to DoctorAnalysis.SurgeryUrgency enum
        DoctorAnalysis.SurgeryUrgency surgeryUrgency = DoctorAnalysis.SurgeryUrgency.fromString(urgency);

        return switch (surgeryUrgency) {
            case EMERGENT -> 1;
            case URGENT -> 3;
            case SCHEDULED -> 7;
            case ELECTIVE -> 14;
            default -> 7;
        };
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
        analysis.setSurgeryUrgency(mapStringToSurgeryUrgency(analysisRequest.getSurgeryUrgency()));
        analysis.setRequireLabTests(analysisRequest.getRequireLabTests());
        analysis.setLabTestsNeeded(analysisRequest.getLabTestsNeeded());

        if (analysisRequest.getStatus() != null) {
            analysis.setStatus(DoctorAnalysis.AnalysisStatus.fromString(analysisRequest.getStatus()));
        }

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

    @Transactional(readOnly = true)
    public List<DoctorAnalysisResponse> getAllAnalyses() {
        return doctorAnalysisRepository.findAll().stream()
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
                analysis.getStatus(),  // Pass the enum directly
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