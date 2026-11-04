package com.srkr.library.service;

import com.srkr.library.model.Loan;
import com.srkr.library.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loan> getApprovedLoansForStudent(String registrationNumber) {
        return loanRepository.findByStudent_RegistrationNumberAndStatus(
                registrationNumber, "APPROVED");
    }

    public List<Loan> getPendingRequests() {
        return loanRepository.findByStatus("PENDING");
    }

    public void approveRequest(Long requestId, LocalDate issueDate, LocalDate dueDate) {
        Loan loan = loanRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        loan.setStatus("APPROVED");
        loan.setIssueDate(issueDate);
        loan.setDueDate(dueDate);

        loanRepository.save(loan);
    }

    public void declineRequest(Long requestId) {
        Loan loan = loanRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        loan.setStatus("DECLINED");

        loanRepository.save(loan);
    }
}