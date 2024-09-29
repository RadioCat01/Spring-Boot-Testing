package com.BEM.BookManagement.controller;

import com.BEM.BookManagement.entities.StudentResponseDTO;
import com.BEM.BookManagement.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Stu")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/students")
    public List<StudentResponseDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/student/{id}")
    public StudentResponseDTO getAllStudent(@PathVariable("id") int id) {
        return studentService.getStudentById(id);
    }

}
