package com.srkr.library.controller;

import com.srkr.library.model.Student;
import com.srkr.library.model.UserAccount;
import com.srkr.library.service.StudentService;
import com.srkr.library.service.UserAccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final StudentService studentService;
    private final UserAccountService userAccountService;

    public AuthController(StudentService studentService,
                          UserAccountService userAccountService) {
        this.studentService = studentService;
        this.userAccountService = userAccountService;
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/signup")
    public String registerStudent(
            @RequestParam String fullName,
            @RequestParam String registrationNumber,
            @RequestParam String department,
            @RequestParam int yearOfStudy,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password) {

        Student student = new Student();
        student.setFullName(fullName);
        student.setRegistrationNumber(registrationNumber);
        student.setDepartment(department);
        student.setYearOfStudy(yearOfStudy);
        student.setEmail(email);
        student.setPhone(phone);
        studentService.saveStudent(student);

        UserAccount account = new UserAccount();
        account.setRegistrationNumber(registrationNumber);
        account.setPassword(password);
        account.setRole("STUDENT");
        userAccountService.saveAccount(account);

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String registrationNumber,
            @RequestParam String password,
            HttpSession session) {

        return userAccountService.findByRegistrationNumber(registrationNumber)
                .filter(account -> account.getPassword().equals(password))
                .map(account -> {
                    session.setAttribute(
                            "registrationNumber",
                            account.getRegistrationNumber()
                    );
                    session.setAttribute("role", account.getRole());
                    return "redirect:/";
                })
                .orElse("redirect:/login?error");
    }

    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logoutGet(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}