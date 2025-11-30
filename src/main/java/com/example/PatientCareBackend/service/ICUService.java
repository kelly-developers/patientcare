package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.ICURequest;
import com.example.PatientCareBackend.dto.response.ICUResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.ICU;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.repository.ICURepository;
import com.example.PatientCareBackend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ICUService {

    private final ICURepository icuRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public ICUResponse addICURecord(ICURequest icuRequest) {
        Patient patient = patientRepository.findById(icuRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + icuRequest.getPatientId()));

        ICU icuRecord = new ICU();
        icuRecord.setPatient(patient);

        // Hemodynamics
        icuRecord.setHeartRate(icuRequest.getHeartRate());
        icuRecord.setBloodPressureSystolic(icuRequest.getBloodPressureSystolic());
        icuRecord.setBloodPressureDiastolic(icuRequest.getBloodPressureDiastolic());
        icuRecord.setMeanArterialPressure(icuRequest.getMeanArterialPressure());
        icuRecord.setCentralVenousPressure(icuRequest.getCentralVenousPressure());

        // Respiratory
        icuRecord.setRespiratoryRate(icuRequest.getRespiratoryRate());
        icuRecord.setOxygenSaturation(icuRequest.getOxygenSaturation());
        icuRecord.setFio2(icuRequest.getFio2());
        icuRecord.setPeep(icuRequest.getPeep());
        icuRecord.setTidalVolume(icuRequest.getTidalVolume());
        icuRecord.setVentilationMode(icuRequest.getVentilationMode());

        // Neurological
        icuRecord.setGcsTotal(icuRequest.getGcsTotal());
        icuRecord.setGcsEyes(icuRequest.getGcsEyes());
        icuRecord.setGcsVerbal(icuRequest.getGcsVerbal());
        icuRecord.setGcsMotor(icuRequest.getGcsMotor());
        icuRecord.setPupilSizeLeft(icuRequest.getPupilSizeLeft());
        icuRecord.setPupilSizeRight(icuRequest.getPupilSizeRight());
        icuRecord.setPupilReactionLeft(icuRequest.getPupilReactionLeft());
        icuRecord.setPupilReactionRight(icuRequest.getPupilReactionRight());

        // Metabolic
        icuRecord.setTemperature(icuRequest.getTemperature());
        icuRecord.setBloodGlucose(icuRequest.getBloodGlucose());
        icuRecord.setUrineOutput(icuRequest.getUrineOutput());

        // Lab Results
        icuRecord.setAbgPh(icuRequest.getAbgPh());
        icuRecord.setAbgPao2(icuRequest.getAbgPao2());
        icuRecord.setAbgPaco2(icuRequest.getAbgPaco2());
        icuRecord.setAbgHco3(icuRequest.getAbgHco3());

        // Metadata
        icuRecord.setRecordedBy(icuRequest.getRecordedBy());

        ICU savedRecord = icuRepository.save(icuRecord);
        return mapToResponse(savedRecord);
    }

    @Transactional(readOnly = true)
    public List<ICUResponse> getPatientICUData(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        return icuRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ICUResponse updateICURecord(Long id, ICURequest icuRequest) {
        ICU icuRecord = icuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ICU record not found with id: " + id));

        Patient patient = patientRepository.findById(icuRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + icuRequest.getPatientId()));

        icuRecord.setPatient(patient);

        // Update all fields
        icuRecord.setHeartRate(icuRequest.getHeartRate());
        icuRecord.setBloodPressureSystolic(icuRequest.getBloodPressureSystolic());
        icuRecord.setBloodPressureDiastolic(icuRequest.getBloodPressureDiastolic());
        icuRecord.setMeanArterialPressure(icuRequest.getMeanArterialPressure());
        icuRecord.setCentralVenousPressure(icuRequest.getCentralVenousPressure());
        icuRecord.setRespiratoryRate(icuRequest.getRespiratoryRate());
        icuRecord.setOxygenSaturation(icuRequest.getOxygenSaturation());
        icuRecord.setFio2(icuRequest.getFio2());
        icuRecord.setPeep(icuRequest.getPeep());
        icuRecord.setTidalVolume(icuRequest.getTidalVolume());
        icuRecord.setVentilationMode(icuRequest.getVentilationMode());
        icuRecord.setGcsTotal(icuRequest.getGcsTotal());
        icuRecord.setGcsEyes(icuRequest.getGcsEyes());
        icuRecord.setGcsVerbal(icuRequest.getGcsVerbal());
        icuRecord.setGcsMotor(icuRequest.getGcsMotor());
        icuRecord.setPupilSizeLeft(icuRequest.getPupilSizeLeft());
        icuRecord.setPupilSizeRight(icuRequest.getPupilSizeRight());
        icuRecord.setPupilReactionLeft(icuRequest.getPupilReactionLeft());
        icuRecord.setPupilReactionRight(icuRequest.getPupilReactionRight());
        icuRecord.setTemperature(icuRequest.getTemperature());
        icuRecord.setBloodGlucose(icuRequest.getBloodGlucose());
        icuRecord.setUrineOutput(icuRequest.getUrineOutput());
        icuRecord.setAbgPh(icuRequest.getAbgPh());
        icuRecord.setAbgPao2(icuRequest.getAbgPao2());
        icuRecord.setAbgPaco2(icuRequest.getAbgPaco2());
        icuRecord.setAbgHco3(icuRequest.getAbgHco3());
        icuRecord.setRecordedBy(icuRequest.getRecordedBy());

        ICU updatedRecord = icuRepository.save(icuRecord);
        return mapToResponse(updatedRecord);
    }

    @Transactional
    public ICUResponse updateVitals(Long id, Map<String, Object> vitals) {
        ICU icuRecord = icuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ICU record not found with id: " + id));

        // Update only vital signs from the map
        if (vitals.containsKey("heartRate")) {
            icuRecord.setHeartRate(((Number) vitals.get("heartRate")).intValue());
        }
        if (vitals.containsKey("bloodPressureSystolic")) {
            icuRecord.setBloodPressureSystolic(((Number) vitals.get("bloodPressureSystolic")).intValue());
        }
        if (vitals.containsKey("bloodPressureDiastolic")) {
            icuRecord.setBloodPressureDiastolic(((Number) vitals.get("bloodPressureDiastolic")).intValue());
        }
        if (vitals.containsKey("respiratoryRate")) {
            icuRecord.setRespiratoryRate(((Number) vitals.get("respiratoryRate")).intValue());
        }
        if (vitals.containsKey("oxygenSaturation")) {
            icuRecord.setOxygenSaturation(((Number) vitals.get("oxygenSaturation")).intValue());
        }
        if (vitals.containsKey("temperature")) {
            icuRecord.setTemperature((java.math.BigDecimal) vitals.get("temperature"));
        }

        ICU updatedRecord = icuRepository.save(icuRecord);
        return mapToResponse(updatedRecord);
    }

    @Transactional
    public ICUResponse addMedication(Long id, Map<String, Object> medication) {
        // This would typically update a separate medication table
        // For now, we'll just return the current record
        ICU icuRecord = icuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ICU record not found with id: " + id));

        // In a real implementation, you would add medication to a separate table
        // and potentially update the ICU record with medication information

        return mapToResponse(icuRecord);
    }

    @Transactional(readOnly = true)
    public ICUResponse getLatestRecordByPatient(Long patientId) {
        ICU icuRecord = icuRepository.findLatestByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("No ICU records found for patient id: " + patientId));
        return mapToResponse(icuRecord);
    }

    @Transactional(readOnly = true)
    public List<ICUResponse> getCriticalPatients() {
        return icuRepository.findCriticalReadings().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ICUResponse> getPatientRecordsByTimeRange(Long patientId, LocalDateTime startTime, LocalDateTime endTime) {
        return icuRepository.findByPatientAndTimeRange(patientId, startTime, endTime).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPatientStats(Long patientId) {
        List<ICU> records = icuRepository.findByPatientId(patientId);

        if (records.isEmpty()) {
            throw new ResourceNotFoundException("No ICU records found for patient id: " + patientId);
        }

        double avgHeartRate = records.stream().mapToInt(ICU::getHeartRate).average().orElse(0);
        double avgSystolicBP = records.stream().mapToInt(ICU::getBloodPressureSystolic).average().orElse(0);
        double avgDiastolicBP = records.stream().mapToInt(ICU::getBloodPressureDiastolic).average().orElse(0);
        double avgOxygenSaturation = records.stream().mapToInt(ICU::getOxygenSaturation).average().orElse(0);

        return Map.of(
                "totalRecords", records.size(),
                "averageHeartRate", Math.round(avgHeartRate),
                "averageSystolicBP", Math.round(avgSystolicBP),
                "averageDiastolicBP", Math.round(avgDiastolicBP),
                "averageOxygenSaturation", Math.round(avgOxygenSaturation),
                "latestRecord", mapToResponse(records.get(records.size() - 1))
        );
    }

    private ICUResponse mapToResponse(ICU icu) {
        return new ICUResponse(
                icu.getId(),
                mapToPatientResponse(icu.getPatient()),
                icu.getHeartRate(),
                icu.getBloodPressureSystolic(),
                icu.getBloodPressureDiastolic(),
                icu.getMeanArterialPressure(),
                icu.getCentralVenousPressure(),
                icu.getRespiratoryRate(),
                icu.getOxygenSaturation(),
                icu.getFio2(),
                icu.getPeep(),
                icu.getTidalVolume(),
                icu.getVentilationMode(),
                icu.getGcsTotal(),
                icu.getGcsEyes(),
                icu.getGcsVerbal(),
                icu.getGcsMotor(),
                icu.getPupilSizeLeft(),
                icu.getPupilSizeRight(),
                icu.getPupilReactionLeft(),
                icu.getPupilReactionRight(),
                icu.getTemperature(),
                icu.getBloodGlucose(),
                icu.getUrineOutput(),
                icu.getAbgPh(),
                icu.getAbgPao2(),
                icu.getAbgPaco2(),
                icu.getAbgHco3(),
                icu.getRecordedBy(),
                icu.getCreatedAt()
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