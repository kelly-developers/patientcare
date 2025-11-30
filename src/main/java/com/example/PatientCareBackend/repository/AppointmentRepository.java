package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate = :date AND a.doctor.id = :doctorId")
    List<Appointment> findByDateAndDoctor(@Param("date") LocalDate date, @Param("doctorId") Long doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate")
    List<Appointment> findAppointmentsBetweenDates(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = 'SCHEDULED'")
    List<Appointment> findUpcomingAppointmentsByPatient(@Param("patientId") Long patientId);
}