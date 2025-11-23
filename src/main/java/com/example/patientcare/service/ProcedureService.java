package com.example.patientcare.service;

import com.example.patientcare.dto.request.ProcedureRequest;
import com.example.patientcare.dto.response.ProcedureResponse;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.entity.SurgicalProcedure;
import com.example.patientcare.exception.ResourceNotFoundException;
import com.example.patientcare.repository.PatientRepository;
import com.example.patientcare.repository.SurgicalProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcedureService {

    private final SurgicalProcedureRepository procedureRepository;
    private final PatientRepository patientRepository;

    public List<ProcedureResponse> getAllProcedures() {
        return procedureRepository.findAll().stream()
                .map(this::mapToProcedureResponse)
                .collect(Collectors.toList());
    }

    public ProcedureResponse getProcedureById(String id) {
        SurgicalProcedure procedure = procedureRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Procedure not found with id: " + id));
        return mapToProcedureResponse(procedure);
    }

    public List<ProcedureResponse> getProceduresByPatientId(String patientId) {
        return procedureRepository.findByPatientId(UUID.fromString(patientId)).stream()
                .map(this::mapToProcedureResponse)
                .collect(Collectors.toList());
    }

    public List<ProcedureResponse> getProceduresByStatus(String status) {
        SurgicalProcedure.ProcedureStatus procedureStatus =
                SurgicalProcedure.ProcedureStatus.valueOf(status.toUpperCase());
        return procedureRepository.findByStatus(procedureStatus).stream()
                .map(this::mapToProcedureResponse)
                .collect(Collectors.toList());
    }

    public ProcedureResponse createProcedure(ProcedureRequest request) {
        Patient patient = patientRepository.findById(UUID.fromString(request.getPatientId()))
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

        SurgicalProcedure procedure = SurgicalProcedure.builder()
                .patient(patient)
                .procedureName(request.getProcedureName())
                .procedureType(request.getProcedureType())
                .scheduledDate(request.getScheduledDate())
                .actualDate(request.getActualDate())
                .durationMinutes(request.getDurationMinutes())
                .surgeonName(request.getSurgeonName())
                .assistantSurgeon(request.getAssistantSurgeon())
                .anesthesiaType(request.getAnesthesiaType())
                .preOperativeNotes(request.getPreOperativeNotes())
                .operativeNotes(request.getOperativeNotes())
                .postOperativeNotes(request.getPostOperativeNotes())
                .complications(request.getComplications())
                .status(SurgicalProcedure.ProcedureStatus.valueOf(request.getStatus().toUpperCase()))
                .build();

        SurgicalProcedure savedProcedure = procedureRepository.save(procedure);
        return mapToProcedureResponse(savedProcedure);
    }

    public ProcedureResponse updateProcedure(String id, ProcedureRequest request) {
        SurgicalProcedure procedure = procedureRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Procedure not found with id: " + id));

        if (request.getPatientId() != null) {
            Patient patient = patientRepository.findById(UUID.fromString(request.getPatientId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));
            procedure.setPatient(patient);
        }

        if (request.getProcedureName() != null) {
            procedure.setProcedureName(request.getProcedureName());
        }
        if (request.getProcedureType() != null) {
            procedure.setProcedureType(request.getProcedureType());
        }
        if (request.getScheduledDate() != null) {
            procedure.setScheduledDate(request.getScheduledDate());
        }
        if (request.getActualDate() != null) {
            procedure.setActualDate(request.getActualDate());
        }
        if (request.getDurationMinutes() != null) {
            procedure.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getSurgeonName() != null) {
            procedure.setSurgeonName(request.getSurgeonName());
        }
        if (request.getAssistantSurgeon() != null) {
            procedure.setAssistantSurgeon(request.getAssistantSurgeon());
        }
        if (request.getAnesthesiaType() != null) {
            procedure.setAnesthesiaType(request.getAnesthesiaType());
        }
        if (request.getPreOperativeNotes() != null) {
            procedure.setPreOperativeNotes(request.getPreOperativeNotes());
        }
        if (request.getOperativeNotes() != null) {
            procedure.setOperativeNotes(request.getOperativeNotes());
        }
        if (request.getPostOperativeNotes() != null) {
            procedure.setPostOperativeNotes(request.getPostOperativeNotes());
        }
        if (request.getComplications() != null) {
            procedure.setComplications(request.getComplications());
        }
        if (request.getStatus() != null) {
            procedure.setStatus(SurgicalProcedure.ProcedureStatus.valueOf(request.getStatus().toUpperCase()));
        }

        SurgicalProcedure updatedProcedure = procedureRepository.save(procedure);
        return mapToProcedureResponse(updatedProcedure);
    }

    public void deleteProcedure(String id) {
        SurgicalProcedure procedure = procedureRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Procedure not found with id: " + id));
        procedureRepository.delete(procedure);
    }

    private ProcedureResponse mapToProcedureResponse(SurgicalProcedure procedure) {
        return ProcedureResponse.builder()
                .id(procedure.getId().toString())
                .patientId(procedure.getPatient().getId().toString())
                .patientName(procedure.getPatient().getFirstName() + " " + procedure.getPatient().getLastName())
                .procedureName(procedure.getProcedureName())
                .procedureType(procedure.getProcedureType())
                .scheduledDate(procedure.getScheduledDate())
                .actualDate(procedure.getActualDate())
                .durationMinutes(procedure.getDurationMinutes())
                .surgeonName(procedure.getSurgeonName())
                .assistantSurgeon(procedure.getAssistantSurgeon())
                .anesthesiaType(procedure.getAnesthesiaType())
                .preOperativeNotes(procedure.getPreOperativeNotes())
                .operativeNotes(procedure.getOperativeNotes())
                .postOperativeNotes(procedure.getPostOperativeNotes())
                .complications(procedure.getComplications())
                .status(procedure.getStatus().name())
                .createdAt(procedure.getCreatedAt())
                .updatedAt(procedure.getUpdatedAt())
                .build();
    }
}