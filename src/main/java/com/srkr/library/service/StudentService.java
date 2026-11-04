package com.srkr.library.service;

import com.srkr.library.model.Student;
import com.srkr.library.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> findByRegistrationNumber(String registrationNumber) {
        return studentRepository.findFirstByRegistrationNumberOrderByIdAsc(registrationNumber);
    }
}