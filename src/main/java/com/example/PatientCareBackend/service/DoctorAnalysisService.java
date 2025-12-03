package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.DoctorAnalysisRequest;
import com.example.PatientCareBackend.dto.response.DoctorAnalysisResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.*;
import com.example.PatientCareBackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorAnalysisService {

    private final DoctorAnalysisRepository doctorAnalysisRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final SurgeryRepository surgeryRepository;

    @Transactional
    public DoctorAnalysisResponse createAnalysis(DoctorAnalysisRequest analysisRequest) {
        log.info("Creating analysis with request: {}", analysisRequest);

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

        // Map surgery urgency - handle null case
        if (analysisRequest.getSurgeryUrgency() != null) {
            DoctorAnalysis.SurgeryUrgency urgency = DoctorAnalysis.SurgeryUrgency.fromString(analysisRequest.getSurgeryUrgency());
            analysis.setSurgeryUrgency(urgency);
        }

        analysis.setRequireLabTests(analysisRequest.getRequireLabTests());
        analysis.setLabTestsNeeded(analysisRequest.getLabTestsNeeded());
        analysis.setStatus(DoctorAnalysis.AnalysisStatus.fromString(analysisRequest.getStatus()));
        analysis.setCreatedAt(LocalDateTime.now());

        DoctorAnalysis savedAnalysis = doctorAnalysisRepository.save(analysis);
        log.info("Created analysis with ID: {}", savedAnalysis.getId());

        // Create surgery record if surgery is recommended
        if (Boolean.TRUE.equals(analysisRequest.getRecommendSurgery()) &&
                analysisRequest.getSurgeryType() != null &&
                !analysisRequest.getSurgeryType().trim().isEmpty()) {

            log.info("Creating surgery from analysis. RecommendSurgery: true, SurgeryType: {}", analysisRequest.getSurgeryType());
            createSurgeryFromAnalysis(savedAnalysis);
        } else {
            log.info("Surgery not created. RecommendSurgery: {}, SurgeryType: {}",
                    analysisRequest.getRecommendSurgery(), analysisRequest.getSurgeryType());
        }

        return mapToResponse(savedAnalysis);
    }

    @Transactional
    private void createSurgeryFromAnalysis(DoctorAnalysis analysis) {
        try {
            Patient patient = analysis.getPatient();
            String procedureName = analysis.getSurgeryType();

            log.info("Checking if surgery exists for patient {} with procedure {}", patient.getId(), procedureName);

            // Check if surgery already exists for this patient with same procedure
            boolean surgeryExists = surgeryRepository.existsByPatientAndProcedureNameAndStatus(
                    patient,
                    procedureName,
                    Surgery.SurgeryStatus.PENDING_CONSENT
            );

            if (!surgeryExists) {
                Surgery surgery = new Surgery();
                surgery.setPatient(patient);
                surgery.setProcedureName(procedureName);
                surgery.setDiagnosis(analysis.getDiagnosis());

                // Map urgency from DoctorAnalysis to Surgery
                if (analysis.getSurgeryUrgency() != null) {
                    Surgery.SurgeryUrgency surgeryUrgency = mapAnalysisUrgencyToSurgeryUrgency(analysis.getSurgeryUrgency().name());
                    surgery.setUrgency(surgeryUrgency);
                } else {
                    surgery.setUrgency(Surgery.SurgeryUrgency.ELECTIVE);
                }

                surgery.setStatus(Surgery.SurgeryStatus.PENDING_CONSENT);
                surgery.setRecommendedBy(analysis.getDoctor().getFirstName() + " " + analysis.getDoctor().getLastName());
                surgery.setScheduledDate(LocalDateTime.now().plusDays(getScheduledDays(analysis.getSurgeryUrgency() != null ? analysis.getSurgeryUrgency().name() : null)));
                surgery.setCreatedAt(LocalDateTime.now());

                Surgery savedSurgery = surgeryRepository.save(surgery);
                log.info("SUCCESS: Created surgery with ID: {} for patient: {}, procedure: {}, status: {}",
                        savedSurgery.getId(), patient.getId(), procedureName, savedSurgery.getStatus());
            } else {
                log.info("Surgery already exists for patient {} with procedure {} and status PENDING_CONSENT",
                        patient.getId(), procedureName);
            }
        } catch (Exception e) {
            log.error("ERROR creating surgery from analysis: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create surgery from analysis: " + e.getMessage(), e);
        }
    }

    private Surgery.SurgeryUrgency mapAnalysisUrgencyToSurgeryUrgency(String analysisUrgency) {
        if (analysisUrgency == null) {
            return Surgery.SurgeryUrgency.ELECTIVE;
        }

        // Convert to uppercase for case-insensitive comparison
        String urgencyUpper = analysisUrgency.toUpperCase();

        // Map DoctorAnalysis.SurgeryUrgency to Surgery.SurgeryUrgency
        if (urgencyUpper.contains("EMERGENT") || urgencyUpper.contains("EMERGENCY")) {
            return Surgery.SurgeryUrgency.EMERGENCY;
        } else if (urgencyUpper.contains("URGENT")) {
            return Surgery.SurgeryUrgency.URGENT;
        } else if (urgencyUpper.contains("SCHEDULED") || urgencyUpper.contains("ROUTINE")) {
            return Surgery.SurgeryUrgency.SCHEDULED;
        } else if (urgencyUpper.contains("ELECTIVE")) {
            return Surgery.SurgeryUrgency.ELECTIVE;
        } else {
            return Surgery.SurgeryUrgency.ELECTIVE;
        }
    }

    private int getScheduledDays(String urgency) {
        if (urgency == null) {
            return 7;
        }

        String urgencyUpper = urgency.toUpperCase();

        if (urgencyUpper.contains("EMERGENT") || urgencyUpper.contains("EMERGENCY")) {
            return 1;
        } else if (urgencyUpper.contains("URGENT")) {
            return 3;
        } else if (urgencyUpper.contains("SCHEDULED") || urgencyUpper.contains("ROUTINE")) {
            return 7;
        } else if (urgencyUpper.contains("ELECTIVE")) {
            return 14;
        } else {
            return 7;
        }
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

        if (analysisRequest.getSurgeryUrgency() != null) {
            DoctorAnalysis.SurgeryUrgency urgency = DoctorAnalysis.SurgeryUrgency.fromString(analysisRequest.getSurgeryUrgency());
            analysis.setSurgeryUrgency(urgency);
        }

        analysis.setRequireLabTests(analysisRequest.getRequireLabTests());
        analysis.setLabTestsNeeded(analysisRequest.getLabTestsNeeded());

        if (analysisRequest.getStatus() != null) {
            analysis.setStatus(DoctorAnalysis.AnalysisStatus.fromString(analysisRequest.getStatus()));
        }

        DoctorAnalysis updatedAnalysis = doctorAnalysisRepository.save(analysis);

        // If surgery recommendation changed to true, create surgery
        if (Boolean.TRUE.equals(analysisRequest.getRecommendSurgery()) &&
                analysisRequest.getSurgeryType() != null &&
                !analysisRequest.getSurgeryType().trim().isEmpty()) {
            createSurgeryFromAnalysis(updatedAnalysis);
        }

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
        List<DoctorAnalysis> analyses = doctorAnalysisRepository.findByRecommendSurgeryTrue();
        log.info("Found {} analyses requiring surgery", analyses.size());
        return analyses.stream()
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