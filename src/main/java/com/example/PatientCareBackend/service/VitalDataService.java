package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.VitalDataRequest;
import com.example.PatientCareBackend.dto.response.VitalDataResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.VitalData;
import com.example.PatientCareBackend.repository.PatientRepository;
import com.example.PatientCareBackend.repository.VitalDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VitalDataService {

    private final VitalDataRepository vitalDataRepository;
    private final PatientRepository patientRepository;
    private final JwtService jwtService;

    @Transactional
    public VitalDataResponse recordVitalData(VitalDataRequest vitalDataRequest) {
        Patient patient = patientRepository.findById(vitalDataRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + vitalDataRequest.getPatientId()));

        VitalData vitalData = new VitalData();
        vitalData.setPatient(patient);
        vitalData.setSystolicBp(vitalDataRequest.getSystolicBp());
        vitalData.setDiastolicBp(vitalDataRequest.getDiastolicBp());
        vitalData.setHeartRate(vitalDataRequest.getHeartRate());
        vitalData.setRespiratoryRate(vitalDataRequest.getRespiratoryRate());
        vitalData.setTemperature(vitalDataRequest.getTemperature());
        vitalData.setOxygenSaturation(vitalDataRequest.getOxygenSaturation());
        vitalData.setHeight(vitalDataRequest.getHeight());
        vitalData.setWeight(vitalDataRequest.getWeight());
        vitalData.setBloodGlucose(vitalDataRequest.getBloodGlucose());
        vitalData.setPainLevel(vitalDataRequest.getPainLevel());

        // Calculate BMI if height and weight are provided
        if (vitalDataRequest.getHeight() != null && vitalDataRequest.getWeight() != null) {
            BigDecimal heightInMeters = vitalDataRequest.getHeight().divide(BigDecimal.valueOf(100));
            BigDecimal bmi = vitalDataRequest.getWeight().divide(heightInMeters.pow(2), 2, BigDecimal.ROUND_HALF_UP);
            vitalData.setBmi(bmi);
        }

        vitalData.setRiskLevel(calculateRiskLevel(vitalData));
        vitalData.setNotes(vitalDataRequest.getNotes());
        vitalData.setRecordedBy(vitalDataRequest.getRecordedBy());

        VitalData savedVitalData = vitalDataRepository.save(vitalData);
        return mapToResponse(savedVitalData);
    }

    @Transactional(readOnly = true)
    public List<VitalDataResponse> getPatientVitals(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return vitalDataRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VitalDataResponse> getVitalsRecordedByCurrentUser() {
        String currentUsername = jwtService.getCurrentUser().getUsername();
        return vitalDataRepository.findByRecordedBy(currentUsername).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VitalDataResponse> getCriticalVitals() {
        return vitalDataRepository.findCriticalVitals().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VitalDataResponse getVitalDataById(Long id) {
        VitalData vitalData = vitalDataRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vital data not found with id: " + id));
        return mapToResponse(vitalData);
    }

    @Transactional(readOnly = true)
    public List<VitalDataResponse> getRecentVitals(Long patientId, int hours) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        return vitalDataRepository.findByPatientAndTimeRange(patientId, startTime, LocalDateTime.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VitalDataResponse> getLatestVitals(Long patientId, int limit) {
        return vitalDataRepository.findRecentByPatient(patientId, limit).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private VitalData.RiskLevel calculateRiskLevel(VitalData vitalData) {
        // Critical conditions
        if (vitalData.getSystolicBp() < 90 || vitalData.getSystolicBp() > 180 ||
                vitalData.getDiastolicBp() < 60 || vitalData.getDiastolicBp() > 120 ||
                vitalData.getHeartRate() < 40 || vitalData.getHeartRate() > 140 ||
                vitalData.getOxygenSaturation() < 90) {
            return VitalData.RiskLevel.CRITICAL;
        }

        // High risk conditions
        if (vitalData.getSystolicBp() < 100 || vitalData.getSystolicBp() > 160 ||
                vitalData.getDiastolicBp() < 70 || vitalData.getDiastolicBp() > 100 ||
                vitalData.getHeartRate() < 50 || vitalData.getHeartRate() > 120 ||
                vitalData.getOxygenSaturation() < 94) {
            return VitalData.RiskLevel.HIGH;
        }

        // Medium risk conditions
        if (vitalData.getSystolicBp() < 110 || vitalData.getSystolicBp() > 140 ||
                vitalData.getDiastolicBp() < 75 || vitalData.getDiastolicBp() > 90 ||
                vitalData.getHeartRate() < 60 || vitalData.getHeartRate() > 100) {
            return VitalData.RiskLevel.MEDIUM;
        }

        return VitalData.RiskLevel.LOW;
    }

    private VitalDataResponse mapToResponse(VitalData vitalData) {
        return new VitalDataResponse(
                vitalData.getId(),
                mapToPatientResponse(vitalData.getPatient()),
                vitalData.getSystolicBp(),
                vitalData.getDiastolicBp(),
                vitalData.getHeartRate(),
                vitalData.getRespiratoryRate(),
                vitalData.getTemperature(),
                vitalData.getOxygenSaturation(),
                vitalData.getHeight(),
                vitalData.getWeight(),
                vitalData.getBloodGlucose(),
                vitalData.getPainLevel(),
                vitalData.getBmi(),
                vitalData.getRiskLevel(),
                vitalData.getNotes(),
                vitalData.getRecordedBy(),
                vitalData.getCreatedAt()
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
                patient.getResearchConsent(),
                patient.getSampleStorageConsent(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}