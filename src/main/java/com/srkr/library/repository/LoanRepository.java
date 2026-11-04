package com.srkr.library.repository;

import com.srkr.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStudent_RegistrationNumberAndStatus(
            String registrationNumber, String status);

    List<Loan> findByStatus(String status);
}