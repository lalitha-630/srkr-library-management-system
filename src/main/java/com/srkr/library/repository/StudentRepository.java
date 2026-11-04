package com.srkr.library.repository;

import com.srkr.library.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findFirstByRegistrationNumberOrderByIdAsc(String registrationNumber);
}