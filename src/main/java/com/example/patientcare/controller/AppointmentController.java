package com.example.patientcare.controller;

import com.example.patientcare.dto.request.AppointmentRequest;
import com.example.patientcare.dto.response.ApiResponse;
import com.example.patientcare.dto.response.AppointmentResponse;
import com.example.patientcare.entity.Appointment;
import com.example.patientcare.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> createAppointment(@Valid @RequestBody AppointmentRequest request, Authentication authentication) {
        String createdByUserId = authentication.getName();
        AppointmentResponse appointment = appointmentService.createAppointment(request, createdByUserId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Appointment created successfully")
                .data(appointment)
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable String id) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Appointment retrieved successfully")
                .data(appointment)
                .build());
    }

    @GetMapping("/appointment-id/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getAppointmentByAppointmentId(@PathVariable String appointmentId) {
        AppointmentResponse appointment = appointmentService.getAppointmentByAppointmentId(appointmentId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Appointment retrieved successfully")
                .data(appointment)
                .build());
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getAppointmentsByPatientId(@PathVariable String patientId) {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient appointments retrieved successfully")
                .data(appointments)
                .build());
    }

    @GetMapping("/patient/{patientId}/paginated")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getAppointmentsByPatientIdPaginated(@PathVariable String patientId, Pageable pageable) {
        Page<AppointmentResponse> appointments = appointmentService.getAppointmentsByPatientIdPaginated(patientId, pageable);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Patient appointments retrieved successfully")
                .data(appointments)
                .build());
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse> getAppointmentsByDoctorId(@PathVariable String doctorId) {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Doctor appointments retrieved successfully")
                .data(appointments)
                .build());
    }

    @GetMapping("/doctor/{doctorId}/upcoming")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse> getUpcomingAppointmentsByDoctor(@PathVariable String doctorId,
                                                                       @RequestParam(defaultValue = "7") int days) {
        List<AppointmentResponse> appointments = appointmentService.getUpcomingAppointmentsByDoctor(doctorId, days);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Upcoming doctor appointments retrieved successfully")
                .data(appointments)
                .build());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getAppointmentsByStatus(@PathVariable Appointment.AppointmentStatus status) {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Appointments by status retrieved successfully")
                .data(appointments)
                .build());
    }

    @PatchMapping("/{appointmentId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse> updateAppointmentStatus(@PathVariable String appointmentId,
                                                               @RequestParam Appointment.AppointmentStatus status,
                                                               @RequestParam(required = false) String cancellationReason) {
        AppointmentResponse appointment = appointmentService.updateAppointmentStatus(appointmentId, status, cancellationReason);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Appointment status updated successfully")
                .data(appointment)
                .build());
    }

    @PutMapping("/{appointmentId}/reschedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> rescheduleAppointment(@PathVariable String appointmentId,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newAppointmentDate,
                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newEndTime,
                                                             Authentication authentication) {
        String updatedByUserId = authentication.getName();
        AppointmentResponse appointment = appointmentService.rescheduleAppointment(appointmentId, newAppointmentDate, newEndTime, updatedByUserId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Appointment rescheduled successfully")
                .data(appointment)
                .build());
    }

    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse> deleteAppointment(@PathVariable String appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Appointment deleted successfully")
                .build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> searchAppointments(@RequestParam String q) {
        List<AppointmentResponse> appointments = appointmentService.searchAppointments(q);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Appointments search completed")
                .data(appointments)
                .build());
    }

    @GetMapping("/reminders")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse> getAppointmentsForReminder() {
        List<AppointmentResponse> appointments = appointmentService.getAppointmentsForReminder();
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Appointments for reminder retrieved successfully")
                .data(appointments)
                .build());
    }
}