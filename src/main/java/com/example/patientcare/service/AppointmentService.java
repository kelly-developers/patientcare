package com.example.patientcare.service;

import com.example.patientcare.dto.request.AppointmentRequest;
import com.example.patientcare.dto.response.AppointmentResponse;
import com.example.patientcare.entity.Appointment;
import com.example.patientcare.entity.Patient;
import com.example.patientcare.entity.User;
import com.example.patientcare.exception.ResourceNotFoundException;
import com.example.patientcare.repository.AppointmentRepository;
import com.example.patientcare.repository.PatientRepository;
import com.example.patientcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    private final List<Appointment.AppointmentStatus> ACTIVE_STATUSES = Arrays.asList(
            Appointment.AppointmentStatus.SCHEDULED,
            Appointment.AppointmentStatus.CONFIRMED,
            Appointment.AppointmentStatus.IN_PROGRESS
    );

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request, String createdByUserId) {
        try {
            // Validate patient
            Patient patient = patientRepository.findByPatientId(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + request.getPatientId()));

            // Validate doctor
            User doctor = userRepository.findById(UUID.fromString(request.getDoctorId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId()));

            // Validate doctor role
            if (doctor.getRole() != User.UserRole.DOCTOR) {
                throw new IllegalArgumentException("User is not a doctor: " + request.getDoctorId());
            }

            // Validate created by user
            User createdBy = userRepository.findById(UUID.fromString(createdByUserId))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + createdByUserId));

            // Validate appointment duration
            if (!request.isValidDuration()) {
                throw new IllegalArgumentException("Appointment duration must be between 15 minutes and 4 hours");
            }

            // Check for scheduling conflicts
            if (hasSchedulingConflict(request.getDoctorId(), request.getAppointmentDate(), request.getEndTime())) {
                throw new IllegalArgumentException("Doctor has conflicting appointment during this time");
            }

            // Validate appointment time (must be in future)
            if (request.getAppointmentDate().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Appointment date must be in the future");
            }

            Appointment appointment = mapToEntity(request, patient, doctor, createdBy);
            appointment.generateAppointmentId();

            // Set default status if not provided
            if (request.getStatus() == null) {
                appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
            }

            Appointment savedAppointment = appointmentRepository.save(appointment);
            return mapToResponse(savedAppointment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create appointment: " + e.getMessage(), e);
        }
    }

    public AppointmentResponse getAppointmentById(String id) {
        Appointment appointment = appointmentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
        return mapToResponse(appointment);
    }

    public AppointmentResponse getAppointmentByAppointmentId(String appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));
        return mapToResponse(appointment);
    }

    public List<AppointmentResponse> getAppointmentsByPatientId(String patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patient.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateDesc(UUID.fromString(doctorId))
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByStatus(Appointment.AppointmentStatus status) {
        return appointmentRepository.findByStatusOrderByAppointmentDateDesc(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<AppointmentResponse> getAppointmentsByPatientIdPaginated(String patientId, Pageable pageable) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));

        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patient.getId(), pageable)
                .map(this::mapToResponse);
    }

    public Page<AppointmentResponse> getAppointmentsByDoctorIdPaginated(String doctorId, Pageable pageable) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateDesc(UUID.fromString(doctorId), pageable)
                .map(this::mapToResponse);
    }

    public List<AppointmentResponse> getUpcomingAppointmentsByDoctor(String doctorId, int days) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(days);

        return appointmentRepository.findByDoctorIdAndDateRange(UUID.fromString(doctorId), startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppointmentResponse updateAppointmentStatus(String appointmentId, Appointment.AppointmentStatus status, String cancellationReason) {
        try {
            Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

            appointment.setStatus(status);

            if (status == Appointment.AppointmentStatus.CANCELLED && cancellationReason != null) {
                appointment.setCancellationReason(cancellationReason);
            }

            Appointment updatedAppointment = appointmentRepository.save(appointment);
            return mapToResponse(updatedAppointment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update appointment status: " + e.getMessage(), e);
        }
    }

    @Transactional
    public AppointmentResponse rescheduleAppointment(String appointmentId, LocalDateTime newAppointmentDate, LocalDateTime newEndTime, String updatedByUserId) {
        try {
            Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

            // Validate new appointment time
            if (newAppointmentDate.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("New appointment date must be in the future");
            }

            // Check for scheduling conflicts
            if (hasSchedulingConflict(appointment.getDoctor().getId().toString(), newAppointmentDate, newEndTime)) {
                throw new IllegalArgumentException("Doctor has conflicting appointment during this time");
            }

            appointment.setAppointmentDate(newAppointmentDate);
            appointment.setEndTime(newEndTime);
            appointment.setStatus(Appointment.AppointmentStatus.RESCHEDULED);

            Appointment rescheduledAppointment = appointmentRepository.save(appointment);
            return mapToResponse(rescheduledAppointment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reschedule appointment: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteAppointment(String appointmentId) {
        try {
            Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));
            appointmentRepository.delete(appointment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete appointment: " + e.getMessage(), e);
        }
    }

    public List<AppointmentResponse> searchAppointments(String query) {
        return appointmentRepository.searchAppointments(query)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsForReminder() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(24); // Next 24 hours

        return appointmentRepository.findAppointmentsForReminder(startTime, endTime, ACTIVE_STATUSES)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private boolean hasSchedulingConflict(String doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        Long conflictCount = appointmentRepository.countConflictingAppointments(
                UUID.fromString(doctorId),
                startTime,
                endTime,
                ACTIVE_STATUSES
        );
        return conflictCount > 0;
    }

    private Appointment mapToEntity(AppointmentRequest request, Patient patient, User doctor, User createdBy) {
        return Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .createdBy(createdBy)
                .appointmentDate(request.getAppointmentDate())
                .endTime(request.getEndTime())
                .status(request.getStatus() != null ? request.getStatus() : Appointment.AppointmentStatus.SCHEDULED)
                .appointmentType(request.getAppointmentType())
                .reason(request.getReason())
                .symptoms(request.getSymptoms())
                .notes(request.getNotes())
                .isUrgent(request.getIsUrgent())
                .reminderSent(false)
                .build();
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .appointmentId(appointment.getAppointmentId())
                .patientId(appointment.getPatient().getPatientId())
                .patientName(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName())
                .doctorId(appointment.getDoctor().getId().toString())
                .doctorName(appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName())
                .createdByName(appointment.getCreatedBy() != null ?
                        appointment.getCreatedBy().getFirstName() + " " + appointment.getCreatedBy().getLastName() : null)
                .appointmentDate(appointment.getAppointmentDate())
                .endTime(appointment.getEndTime())
                .status(appointment.getStatus())
                .appointmentType(appointment.getAppointmentType())
                .reason(appointment.getReason())
                .symptoms(appointment.getSymptoms())
                .notes(appointment.getNotes())
                .cancellationReason(appointment.getCancellationReason())
                .isUrgent(appointment.getIsUrgent())
                .reminderSent(appointment.getReminderSent())
                .durationInMinutes(appointment.getDurationInMinutes())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
}