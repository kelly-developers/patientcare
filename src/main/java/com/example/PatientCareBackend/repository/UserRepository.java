package com.example.PatientCareBackend.repository;

import com.example.PatientCareBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findByRole(User.Role role);
    List<User> findByAvailableTrue();

    @Query("SELECT u FROM User u WHERE u.specialty = :specialty AND u.available = true")
    List<User> findAvailableBySpecialty(@Param("specialty") String specialty);

    @Query("SELECT u FROM User u WHERE u.role = 'DOCTOR' AND u.available = true")
    List<User> findAvailableDoctors();
}