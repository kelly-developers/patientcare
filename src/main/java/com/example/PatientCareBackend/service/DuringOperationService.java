package com.example.PatientCareBackend.service;

import com.example.PatientCareBackend.dto.request.DuringOperationRequest;
import com.example.PatientCareBackend.dto.response.DuringOperationResponse;
import com.example.PatientCareBackend.exception.ResourceNotFoundException;
import com.example.PatientCareBackend.model.DuringOperation;
import com.example.PatientCareBackend.model.Patient;
import com.example.PatientCareBackend.model.Surgery;
import com.example.PatientCareBackend.repository.DuringOperationRepository;
import com.example.PatientCareBackend.repository.PatientRepository;
import com.example.PatientCareBackend.repository.SurgeryRepository;
import com.example.PatientCareBackend.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DuringOperationService {

    private final DuringOperationRepository duringOperationRepository;
    private final SurgeryRepository surgeryRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public DuringOperationResponse startOperation(DuringOperationRequest operationRequest) {
        Surgery surgery = surgeryRepository.findById(operationRequest.getSurgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + operationRequest.getSurgeryId()));

        Patient patient = patientRepository.findById(operationRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + operationRequest.getPatientId()));

        DuringOperation operation = new DuringOperation();
        operation.setSurgery(surgery);
        operation.setPatient(patient);
        operation.setStartTime(operationRequest.getStartTime());
        operation.setEndTime(operationRequest.getEndTime());
        operation.setStatus(operationRequest.getStatus());
        operation.setHeartRate(operationRequest.getHeartRate());
        operation.setBloodPressureSystolic(operationRequest.getBloodPressureSystolic());
        operation.setBloodPressureDiastolic(operationRequest.getBloodPressureDiastolic());
        operation.setOxygenSaturation(operationRequest.getOxygenSaturation());
        operation.setTemperature(operationRequest.getTemperature());
        operation.setBloodLoss(operationRequest.getBloodLoss());
        operation.setUrineOutput(operationRequest.getUrineOutput());

        // Convert Map objects to JSON strings
        operation.setSurgicalNotes(JsonUtil.toJson(operationRequest.getSurgicalNotes()));
        operation.setComplications(JsonUtil.toJson(operationRequest.getComplications()));
        operation.setMedications(JsonUtil.toJson(operationRequest.getMedications()));
        operation.setOutcomes(JsonUtil.toJson(operationRequest.getOutcomes()));
        operation.setSurgicalGoals(JsonUtil.toJson(operationRequest.getSurgicalGoals()));
        operation.setEquipmentCheck(JsonUtil.toJson(operationRequest.getEquipmentCheck()));
        operation.setClosureChecklist(JsonUtil.toJson(operationRequest.getClosureChecklist()));

        DuringOperation savedOperation = duringOperationRepository.save(operation);

        // Update surgery status
        surgery.setStatus(Surgery.SurgeryStatus.IN_PROGRESS);
        surgery.setActualDate(LocalDateTime.now());
        surgeryRepository.save(surgery);

        return mapToResponse(savedOperation);
    }

    @Transactional
    public DuringOperationResponse updateOperation(Long id, DuringOperationRequest operationRequest) {
        DuringOperation operation = duringOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("During operation record not found with id: " + id));

        Surgery surgery = surgeryRepository.findById(operationRequest.getSurgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Surgery not found with id: " + operationRequest.getSurgeryId()));

        Patient patient = patientRepository.findById(operationRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + operationRequest.getPatientId()));

        operation.setSurgery(surgery);
        operation.setPatient(patient);
        operation.setStartTime(operationRequest.getStartTime());
        operation.setEndTime(operationRequest.getEndTime());
        operation.setStatus(operationRequest.getStatus());
        operation.setHeartRate(operationRequest.getHeartRate());
        operation.setBloodPressureSystolic(operationRequest.getBloodPressureSystolic());
        operation.setBloodPressureDiastolic(operationRequest.getBloodPressureDiastolic());
        operation.setOxygenSaturation(operationRequest.getOxygenSaturation());
        operation.setTemperature(operationRequest.getTemperature());
        operation.setBloodLoss(operationRequest.getBloodLoss());
        operation.setUrineOutput(operationRequest.getUrineOutput());

        // Convert Map objects to JSON strings
        operation.setSurgicalNotes(JsonUtil.toJson(operationRequest.getSurgicalNotes()));
        operation.setComplications(JsonUtil.toJson(operationRequest.getComplications()));
        operation.setMedications(JsonUtil.toJson(operationRequest.getMedications()));
        operation.setOutcomes(JsonUtil.toJson(operationRequest.getOutcomes()));
        operation.setSurgicalGoals(JsonUtil.toJson(operationRequest.getSurgicalGoals()));
        operation.setEquipmentCheck(JsonUtil.toJson(operationRequest.getEquipmentCheck()));
        operation.setClosureChecklist(JsonUtil.toJson(operationRequest.getClosureChecklist()));

        DuringOperation updatedOperation = duringOperationRepository.save(operation);
        return mapToResponse(updatedOperation);
    }

    @Transactional
    public DuringOperationResponse completeOperation(Long id) {
        DuringOperation operation = duringOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("During operation record not found with id: " + id));

        operation.setStatus(DuringOperation.OperationStatus.COMPLETED);
        operation.setEndTime(LocalDateTime.now());

        DuringOperation updatedOperation = duringOperationRepository.save(operation);

        // Update surgery status
        Surgery surgery = operation.getSurgery();
        surgery.setStatus(Surgery.SurgeryStatus.COMPLETED);
        surgery.setCompletedDate(LocalDateTime.now());
        surgeryRepository.save(surgery);

        return mapToResponse(updatedOperation);
    }

    @Transactional
    public DuringOperationResponse updateVitals(Long id, Map<String, Object> vitals) {
        DuringOperation operation = duringOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("During operation record not found with id: " + id));

        // Update vital signs from the map
        if (vitals.containsKey("heartRate")) {
            operation.setHeartRate(((Number) vitals.get("heartRate")).intValue());
        }
        if (vitals.containsKey("bloodPressureSystolic")) {
            operation.setBloodPressureSystolic(((Number) vitals.get("bloodPressureSystolic")).intValue());
        }
        if (vitals.containsKey("bloodPressureDiastolic")) {
            operation.setBloodPressureDiastolic(((Number) vitals.get("bloodPressureDiastolic")).intValue());
        }
        if (vitals.containsKey("oxygenSaturation")) {
            operation.setOxygenSaturation(((Number) vitals.get("oxygenSaturation")).intValue());
        }
        if (vitals.containsKey("temperature")) {
            operation.setTemperature(((Number) vitals.get("temperature")).doubleValue());
        }
        if (vitals.containsKey("bloodLoss")) {
            operation.setBloodLoss(((Number) vitals.get("bloodLoss")).intValue());
        }
        if (vitals.containsKey("urineOutput")) {
            operation.setUrineOutput(((Number) vitals.get("urineOutput")).intValue());
        }

        DuringOperation updatedOperation = duringOperationRepository.save(operation);
        return mapToResponse(updatedOperation);
    }

    @Transactional
    public DuringOperationResponse addSurgicalNote(Long id, String note) {
        DuringOperation operation = duringOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("During operation record not found with id: " + id));

        // Parse existing surgical notes
        Map<String, Object> surgicalNotes = JsonUtil.fromJson(operation.getSurgicalNotes());
        if (surgicalNotes == null) {
            surgicalNotes = Map.of("notes", List.of(note));
        } else {
            // Add to existing notes
            @SuppressWarnings("unchecked")
            List<String> notes = (List<String>) surgicalNotes.getOrDefault("notes", List.of());
            notes.add(note);
            surgicalNotes.put("notes", notes);
        }

        // Convert back to JSON string
        operation.setSurgicalNotes(JsonUtil.toJson(surgicalNotes));
        DuringOperation updatedOperation = duringOperationRepository.save(operation);
        return mapToResponse(updatedOperation);
    }

    @Transactional
    public DuringOperationResponse addComplication(Long id, Map<String, Object> complication) {
        DuringOperation operation = duringOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("During operation record not found with id: " + id));

        // Parse existing complications
        Map<String, Object> complications = JsonUtil.fromJson(operation.getComplications());
        if (complications == null) {
            complications = Map.of("complications", List.of(complication));
        } else {
            // Add to existing complications
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> compList = (List<Map<String, Object>>) complications.getOrDefault("complications", List.of());
            compList.add(complication);
            complications.put("complications", compList);
        }

        // Convert back to JSON string
        operation.setComplications(JsonUtil.toJson(complications));
        DuringOperation updatedOperation = duringOperationRepository.save(operation);
        return mapToResponse(updatedOperation);
    }

    @Transactional(readOnly = true)
    public DuringOperationResponse getOperationById(Long id) {
        DuringOperation operation = duringOperationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("During operation record not found with id: " + id));
        return mapToResponse(operation);
    }

    @Transactional(readOnly = true)
    public DuringOperationResponse getOperationBySurgery(Long surgeryId) {
        DuringOperation operation = duringOperationRepository.findBySurgeryId(surgeryId)
                .orElseThrow(() -> new ResourceNotFoundException("During operation record not found for surgery id: " + surgeryId));
        return mapToResponse(operation);
    }

    @Transactional(readOnly = true)
    public List<DuringOperationResponse> getOperationsByStatus(DuringOperation.OperationStatus status) {
        return duringOperationRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DuringOperationResponse> getActiveOperations() {
        return duringOperationRepository.findActiveOperations().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DuringOperationResponse> getRecentOperationsByPatient(Long patientId) {
        return duringOperationRepository.findRecentOperationsByPatient(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DuringOperationResponse mapToResponse(DuringOperation operation) {
        DuringOperationResponse response = new DuringOperationResponse();
        response.setId(operation.getId());
        response.setSurgery(mapToSurgeryResponse(operation.getSurgery()));
        response.setPatient(mapToPatientResponse(operation.getPatient()));
        response.setStartTime(operation.getStartTime());
        response.setEndTime(operation.getEndTime());
        response.setStatus(operation.getStatus());
        response.setHeartRate(operation.getHeartRate());
        response.setBloodPressureSystolic(operation.getBloodPressureSystolic());
        response.setBloodPressureDiastolic(operation.getBloodPressureDiastolic());
        response.setOxygenSaturation(operation.getOxygenSaturation());
        response.setTemperature(operation.getTemperature());
        response.setBloodLoss(operation.getBloodLoss());
        response.setUrineOutput(operation.getUrineOutput());

        // Convert JSON strings back to Map objects
        response.setSurgicalNotes(JsonUtil.fromJson(operation.getSurgicalNotes()));
        response.setComplications(JsonUtil.fromJson(operation.getComplications()));
        response.setMedications(JsonUtil.fromJson(operation.getMedications()));
        response.setOutcomes(JsonUtil.fromJson(operation.getOutcomes()));
        response.setSurgicalGoals(JsonUtil.fromJson(operation.getSurgicalGoals()));
        response.setEquipmentCheck(JsonUtil.fromJson(operation.getEquipmentCheck()));
        response.setClosureChecklist(JsonUtil.fromJson(operation.getClosureChecklist()));
        response.setCreatedAt(operation.getCreatedAt());

        return response;
    }

    private com.example.PatientCareBackend.dto.response.SurgeryResponse mapToSurgeryResponse(Surgery surgery) {
        return new com.example.PatientCareBackend.dto.response.SurgeryResponse(
                surgery.getId(),
                mapToPatientResponse(surgery.getPatient()),
                surgery.getProcedureName(),
                surgery.getUrgency(),
                surgery.getRecommendedBy(),
                surgery.getDiagnosis(),
                surgery.getStatus(),
                surgery.getConsentDate(),
                surgery.getScheduledDate(),
                surgery.getActualDate(),
                surgery.getCompletedDate(),
                surgery.getSurgeonName(),
                surgery.getDurationMinutes(),
                surgery.getCreatedAt()
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
                patient.getConsentAccepted(),  // Changed from getResearchConsent()
                patient.getConsentFormPath(),  // Changed from getSampleStorageConsent()
                patient.getResearchConsent(),  // Added this line
                patient.getSampleStorageConsent(),  // Added this line
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}