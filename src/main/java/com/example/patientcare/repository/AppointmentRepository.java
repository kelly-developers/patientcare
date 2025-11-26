package com.example.patientcare.repository;

import com.example.patientcare.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Optional<Appointment> findByAppointmentId(String appointmentId);

    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(UUID patientId);

    List<Appointment> findByDoctorIdOrderByAppointmentDateDesc(UUID doctorId);

    List<Appointment> findByStatusOrderByAppointmentDateDesc(Appointment.AppointmentStatus status);

    Page<Appointment> findByPatientIdOrderByAppointmentDateDesc(UUID patientId, Pageable pageable);

    Page<Appointment> findByDoctorIdOrderByAppointmentDateDesc(UUID doctorId, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate BETWEEN :startDate AND :endDate ORDER BY a.appointmentDate")
    List<Appointment> findByDoctorIdAndDateRange(@Param("doctorId") UUID doctorId,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.appointmentDate BETWEEN :startDate AND :endDate ORDER BY a.appointmentDate")
    List<Appointment> findByPatientIdAndDateRange(@Param("patientId") UUID patientId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate ORDER BY a.appointmentDate")
    List<Appointment> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate >= :startDate AND a.status IN :statuses ORDER BY a.appointmentDate")
    List<Appointment> findUpcomingByDoctorIdAndStatus(@Param("doctorId") UUID doctorId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("statuses") List<Appointment.AppointmentStatus> statuses);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDate BETWEEN :startTime AND :endTime AND a.status IN :statuses")
    Long countConflictingAppointments(@Param("doctorId") UUID doctorId,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      @Param("statuses") List<Appointment.AppointmentStatus> statuses);

    @Query("SELECT a FROM Appointment a WHERE a.reminderSent = false AND a.appointmentDate BETWEEN :startTime AND :endTime AND a.status IN :statuses")
    List<Appointment> findAppointmentsForReminder(@Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime,
                                                  @Param("statuses") List<Appointment.AppointmentStatus> statuses);

    @Query("SELECT a FROM Appointment a WHERE " +
            "LOWER(a.patient.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.patient.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.doctor.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.doctor.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.appointmentId) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Appointment> searchAppointments(@Param("query") String query);
}