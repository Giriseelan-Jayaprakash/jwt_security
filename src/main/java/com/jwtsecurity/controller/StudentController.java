package com.jwtsecurity.controller;

import com.jwtsecurity.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    public List<Student> student = new ArrayList<>(List.of(
            new Student(1, "Giriseelan", 75),
            new Student(2, "Vimal", 85)
    ));

    @GetMapping("/students")
    public List<Student> retrieveAllStudents() {
        return student;
    }

//    @PreAuthorize(("ADMIN"))
    @PostMapping("/create-student")
    public Student createStudent(@RequestBody Student students) {
        student.add(students);
        return students;
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
