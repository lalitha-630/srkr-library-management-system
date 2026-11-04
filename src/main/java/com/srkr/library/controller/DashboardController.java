package com.srkr.library.controller;

import com.srkr.library.service.LoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final LoanService loanService;

    public DashboardController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/")
    public String showDashboard(HttpSession session, Model model) {
        String registrationNumber =
                (String) session.getAttribute("registrationNumber");
        String role = (String) session.getAttribute("role");

        model.addAttribute("registrationNumber", registrationNumber);
        model.addAttribute("role", role);

        if ("STUDENT".equals(role) && registrationNumber != null) {
            model.addAttribute(
                    "approvedLoans",
                    loanService.getApprovedLoansForStudent(registrationNumber)
            );
        }

        if ("ADMIN".equals(role)) {
            model.addAttribute(
                    "pendingRequests",
                    loanService.getPendingRequests()
            );
        }

        return "dashboard";
    }
}